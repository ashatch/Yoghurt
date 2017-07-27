package net.andrewhatch.java8antlr4.parser.interpreter.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedirectService implements SocketHandler {
  private static final Logger log = LoggerFactory.getLogger(RedirectService.class);

  private final InetAddress address;
  private final int port;
  private final ExecutorService pool;

  public RedirectService(final InetAddress address, int port) {
    log.info("Establishing redirection to {}:{}", address, port);
    this.address = address;
    this.port = port;
    this.pool = Executors.newFixedThreadPool(2);
  }

  @Override
  public void handleNewConnection(Socket socket) throws IOException {
    log.info("Accepted socket for redirection: {}", socket.getRemoteSocketAddress());
    final Socket target = new Socket(this.address, this.port);

    final InputStream targetInputStream = target.getInputStream();
    final OutputStream targetOutputStream = target.getOutputStream();
    final InputStream sourceInputStream = socket.getInputStream();
    final OutputStream sourceOutputStream = socket.getOutputStream();

    pool.execute(new StreamCopier(targetInputStream, sourceOutputStream));
    pool.execute(new StreamCopier(sourceInputStream, targetOutputStream));

    try {
      pool.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static class StreamCopier implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(StreamCopier.class);

    private final InputStream inputStream;
    private final OutputStream outputStream;

    public StreamCopier(final InputStream inputStream, final OutputStream outputStream) {
      this.inputStream = inputStream;
      this.outputStream = outputStream;
    }

    @Override
    public void run() {
      log.info("Copying stream");
      try {
        final byte[] buffer = new byte[1024];
        int read;

        try {
          while ((read = this.inputStream.read(buffer)) != -1) {
            log.debug("Stream processed {} bytes", read);
            this.outputStream.write(buffer, 0, read);
            this.outputStream.flush();
          }
        } catch (final Throwable t) {
        } finally {
          this.inputStream.close();
          this.outputStream.close();
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}

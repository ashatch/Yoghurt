package net.andrewhatch.yoghurt.parser.interpreter.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortBindingService implements Runnable {
  private static final Logger log = LoggerFactory.getLogger(PortBindingService.class);

  private final ServerSocket serverSocket;
  private final ExecutorService pool;
  private final SocketHandler handler;

  public PortBindingService(int port, int poolSize, SocketHandler handler)
      throws IOException {
    log.info("Setting up port binding service for {}", port);
    this.serverSocket = new ServerSocket(port);
    this.pool = Executors.newFixedThreadPool(poolSize);
    this.handler = handler;
  }

  public void run() { // run the service
    try {
      while(true) {
        pool.execute(new Handler(this.handler, serverSocket.accept()));
      }
    } catch (IOException ex) {
      pool.shutdown();
    }
  }

  private static class Handler implements Runnable {
    private final SocketHandler handler;
    private final Socket socket;

    public Handler(final SocketHandler handler,
                   final Socket socket) {
      this.handler = handler;
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        handler.handleNewConnection(this.socket);
      } catch (final Throwable t) {
        throw new RuntimeException(t);
      }
    }
  }
}

package net.andrewhatch.java8antlr4.parser.handlers;

import net.andrewhatch.java8antlr4.parser.interpreter.network.SocketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryListingHandler implements SocketHandler {
  private static final Logger log = LoggerFactory.getLogger(DirectoryListingHandler.class);

  private final String directory;

  public DirectoryListingHandler(final String directory) {
    log.info("DirectoryListingHandler serving from {}", directory);
    this.directory = directory;
  }

  @Override
  public void handleNewConnection(final Socket socket) throws IOException {
    log.info("Handling connection from {}", socket.getRemoteSocketAddress());
    try (final PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
      final Path path = Paths.get(this.directory);
      Files.list(path).map(Path::toString).forEach(p -> writer.println(p));
    }
  }
}

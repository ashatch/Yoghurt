package net.andrewhatch.yoghurt.parser.interpreter.network;

import java.io.IOException;
import java.net.Socket;

public interface SocketHandler {
  void handleNewConnection(final Socket socket) throws IOException;
}

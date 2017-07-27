package net.andrewhatch.java8antlr4.parser.interpreter;

import com.google.common.collect.Lists;

import net.andrewhatch.java8antlr4.YoghurtBaseListener;
import net.andrewhatch.java8antlr4.YoghurtParser;
import net.andrewhatch.java8antlr4.parser.interpreter.network.PortBindingService;
import net.andrewhatch.java8antlr4.parser.interpreter.network.RedirectService;
import net.andrewhatch.java8antlr4.parser.interpreter.network.SocketHandler;
import net.andrewhatch.java8antlr4.parser.handlers.DirectoryListingHandler;

import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ParseListener extends YoghurtBaseListener {
  private static final Logger log = LoggerFactory.getLogger(ParseListener.class);

  @Override
  public void exitPortBinding(@NotNull YoghurtParser.PortBindingContext ctx) {
    try {
      final int port = new Integer(ctx.IntegerNumber().getText());
      log.info("Setting up port binding for [{}]", port);
      final SocketHandler handler = handlerForFunctionDefinition(port, ctx.functionDefinition());
      spawnBinding(port, handler);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void exitPortRedirect(@NotNull YoghurtParser.PortRedirectContext ctx) {
    try {
      final int port = Integer.parseInt(ctx.IntegerNumber().getText());
      final InetAddress redirectionAddress = InetAddress.getByName(
          ctx.ipAndPort().ipAddress().getText());
      final int redirectionPort = Integer.parseInt(ctx.ipAndPort().portNumber().getText());
      spawnBinding(port, new RedirectService(redirectionAddress, redirectionPort));

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private SocketHandler handlerForFunctionDefinition(
      final int port,
      final YoghurtParser.FunctionDefinitionContext functionDefinitionContext) {
    if ("list".equals(functionDefinitionContext.Identifier().getText())) {
      log.info("Binding the list function to {}", port);
      final List<String> argumentList = Lists.newArrayList();
      populateArgumentList(argumentList, functionDefinitionContext.funcArg());

      return new DirectoryListingHandler(argumentList.get(0));
    }
    throw new RuntimeException(
        String.format("Cannot find function definition for %s",
            functionDefinitionContext.Identifier().getText()));
  }

  private void populateArgumentList(final List<String> list, YoghurtParser.FuncArgContext funcArgContext) {
    list.add(unquote(funcArgContext.variable().getText()));
    if (!funcArgContext.funcArg().isEmpty()) {
      populateArgumentList(list, funcArgContext.funcArg(0));
    }
  }

  private String unquote(String text) {
    return text.replaceAll("'", "");
  }

  private void spawnBinding(final int port, final SocketHandler handler) throws IOException {
    final Thread thread = new Thread(new PortBindingService(port, 10, handler));
    thread.start();
  }
}


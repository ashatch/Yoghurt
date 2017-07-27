package net.andrewhatch.java8antlr4.parser;

import com.google.inject.Guice;

import net.andrewhatch.java8antlr4.parser.interpreter.Parser;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

public class Yoghurt {

  private final Parser parser;
  private final InputStream inputStream;

  @Inject
  public Yoghurt(final Parser parser,
                 final InputStream inputStream) {
    this.parser = parser;
    this.inputStream = inputStream;
  }

  private void run() throws IOException {
    parser.parse(inputStream);
  }

  public static void main(String[] args) throws IOException {
    Guice.createInjector(new YoghurtModule(args))
        .getInstance(Yoghurt.class)
        .run();
  }
}

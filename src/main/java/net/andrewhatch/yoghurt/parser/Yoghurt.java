package net.andrewhatch.yoghurt.parser;

import com.google.inject.Guice;

import net.andrewhatch.yoghurt.parser.interpreter.Parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

public class Yoghurt {
  private static final Logger log = LoggerFactory.getLogger(Yoghurt.class);

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
    if (args.length != 1) {
      log.info("Must supply a file argument. Exiting.");
      System.exit(1);
    }
    Guice.createInjector(new YoghurtModule(args))
        .getInstance(Yoghurt.class)
        .run();
  }
}

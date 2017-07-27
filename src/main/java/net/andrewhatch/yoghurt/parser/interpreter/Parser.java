package net.andrewhatch.yoghurt.parser.interpreter;


import net.andrewhatch.yoghurt.YoghurtLexer;
import net.andrewhatch.yoghurt.YoghurtParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Parser {
  private static final Logger log = LoggerFactory.getLogger(Parser.class);

  public boolean parse(final InputStream inputStream) throws IOException {
    log.info("Beginning parse");
    final ANTLRInputStream antlrInputStream = new ANTLRInputStream(inputStream);
    final YoghurtLexer lexer = new YoghurtLexer(antlrInputStream);
    final CommonTokenStream tokens = new CommonTokenStream(lexer);
    final YoghurtParser parser = new YoghurtParser(tokens);
    ParseTreeWalker.DEFAULT.walk(new ParseListener(), parser.compilationUnit());
    return true;
  }
}

package net.andrewhatch.java8antlr4.parser;

import com.google.common.io.Resources;

import net.andrewhatch.java8antlr4.parser.interpreter.Parser;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {
  @Test
  public void test() throws IOException {
    boolean result = new Parser()
        .parse(
            Resources.getResource("example.¥").openStream());

    assertThat(result).isEqualTo(true);
  }
}
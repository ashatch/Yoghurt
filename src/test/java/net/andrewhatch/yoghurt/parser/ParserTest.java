package net.andrewhatch.yoghurt.parser;

import com.google.common.io.Resources;

import net.andrewhatch.yoghurt.parser.interpreter.Parser;

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
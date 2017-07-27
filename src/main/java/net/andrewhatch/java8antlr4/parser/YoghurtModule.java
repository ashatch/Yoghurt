package net.andrewhatch.java8antlr4.parser;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class YoghurtModule extends AbstractModule {

  private final String[] commandLineArgs;

  public YoghurtModule(final String[] commandLineArgs) {
    this.commandLineArgs = commandLineArgs;
  }
  
  @Override
  protected void configure() {}

  @Provides
  public InputStream inputStream() throws IOException {
    return Files.newInputStream(Paths.get(this.commandLineArgs[0]));
  }
}

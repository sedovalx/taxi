package config

import javax.inject.Singleton

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import utils.slick.{SourceCodeGenerator, SourceCodeGeneratorImpl}

class ControllerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[SourceCodeGenerator].to[SourceCodeGeneratorImpl].in[Singleton]

  }
}

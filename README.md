# sbt-jflex

A plugin for sbt 0.13 that generates code based on a [JFlex](http://jflex.de/) specification.

It is based on the [sbt-antlr plugin](https://github.com/stefri/sbt-antlr/).

This project is (C)opyright 2011 Steffen Fritzsche and (C)opyright 2013 Hanns Holger Rutz, and published under the [Apache 2.0 License](https://raw.github.com/Sciss/sbt-jflex/master/LICENSE).

## Usage

Add a dependency to the plugin in `./project/plugins.sbt`:

    addSbtPlugin("de.sciss" % "sbt-jflex" % "0.3.0")

Place your JFlex grammar files in `src/main/jflex` and they will be included in your next build. Note, `sbt-jflex` generates the source code only once as long as your grammar file didn't change it does not re-generate the java source files.

Include the following line in `build.sbt` to import the JFlex plugin settings:

    seq(jflexSettings: _*)

# sbt-jflex

A plugin for sbt 0.13.x and 1.x that generates code based on a [JFlex](http://jflex.de/) specification.

It is based on the [sbt-antlr plugin](https://github.com/stefri/sbt-antlr/).

This project is (C)opyright 2011 Steffen Fritzsche and (C)opyright 2013—2018 Hanns Holger Rutz, 
and published under the [Apache 2.0 License](https://git.iem.at/sciss/sbt-jflex/raw/master/LICENSE).

## Usage

Add a dependency to the plugin in `./project/plugins.sbt`:

    addSbtPlugin("de.sciss" % "sbt-jflex" % "0.4.0")

Place your JFlex grammar files in `src/main/jflex` and they will be included in your next build.
Note, `sbt-jflex` generates the source code only once as long as your grammar file did not change;
it does not re-generate the java source files.

Include the following line in `build.sbt` (sbt 1.x) to import the JFlex plugin settings:

    SbtJFlexPlugin.jflexSettings

## Building

Note that cross-sbt plugin compilation and publishing requires `^` magic cookie. So `sbt ^compile` for example to compile
both for sbt 0.13.x and sbt 1.x.

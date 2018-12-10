/*
 * Copyright 2011 Steffen Fritzsche.
 * Copyright 2013 Hanns Holger Rutz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sbtjflex

import jflex.{Main => JFlexMain, Options => JFlexOptions}
import sbt.Keys._
import sbt._

object SbtJFlexPlugin extends AutoPlugin {

  final case class JFlexToolConfiguration(
    dot    : Boolean = false,
    dump   : Boolean = false,
    verbose: Boolean = false
  )

  final case class PluginConfiguration(
    grammarSuffix: String = ".flex"
  )

  // Note: for sbt 1.0, config id must be capitalised
  lazy val Jflex                    = config("jflex")
  lazy val generate                 = TaskKey   [Seq[File]]             ("generate")
  lazy val jflexDependency          = SettingKey[ModuleID]              ("jflex-dependency")
  lazy val jflexToolConfiguration   = SettingKey[JFlexToolConfiguration]("jflex-tool-configuration")
  lazy val jflexPluginConfiguration = SettingKey[PluginConfiguration]   ("jflex-plugin-configuration")
  lazy val jflexTokensResource      = SettingKey[File]                  ("jflex-tokens-resource-directory")

  lazy val jflexSettings: Seq[Def.Setting[_]] = inConfig(Jflex)(Seq(
    jflexToolConfiguration   := JFlexToolConfiguration(),
    jflexPluginConfiguration := PluginConfiguration(),
    jflexDependency          := "de.jflex" % "jflex" % "1.7.0",

    sourceDirectory          := (sourceDirectory in Compile).value / "jflex",
    javaSource               := (sourceManaged   in Compile).value,
    jflexTokensResource      := (sourceManaged   in Compile).value,

    managedClasspath := Classpaths.managedJars(Jflex, (classpathTypes in Jflex).value, update.value),

    generate := {
      val out           = streams.value
      val options       = (jflexPluginConfiguration in Jflex).value
      val cachedCompile = FileFunction.cached(out.cacheDirectory / "flex",
        inStyle   = FilesInfo.lastModified,
        outStyle  = FilesInfo.exists) { in =>

        generateWithJFlex(in, (target in Jflex).value, (jflexToolConfiguration in Jflex).value, options, out.log)
      }
      cachedCompile(((sourceDirectory in Jflex).value ** ("*" + options.grammarSuffix)).get.toSet).toSeq
    }

  )) ++ Seq(
    unmanagedSourceDirectories in Compile += (sourceDirectory in Jflex).value,
    sourceGenerators           in Compile += (generate        in Jflex).taskValue,
    cleanFiles                            += (javaSource      in Jflex).value,
//    libraryDependencies                   += (jflexDependency in jflex).value,
    ivyConfigurations                      += Jflex
  )

  private def generateWithJFlex(sources: Set[File], target: File, tool: JFlexToolConfiguration,
                                options: PluginConfiguration, log: Logger): Set[File] = {
    printJFlexOptions(log, tool)

    // prepare target
    target.mkdirs()

    // configure jflex tool
    log.info("JFlex: Using JFlex version %s to generate source files.".format(JFlexMain.version))
    JFlexOptions.dot     = tool.dot
    JFlexOptions.verbose = tool.verbose
    JFlexOptions.dump    = tool.dump
    JFlexOptions.setDir(target.getPath)

    // process grammars
    val grammars = sources
    log.info("JFlex: Generating source files for %d grammars.".format(grammars.size))

    // add each grammar file into the jflex tool's list of grammars to process
    grammars.foreach { g =>
      log.info("JFlex: Grammar file '%s' detected.".format(g.getPath))
      JFlexMain.generate(g)
    }

    (target ** "*.java").get.toSet
  }

  private def printJFlexOptions(log: Logger, options: JFlexToolConfiguration) {
    log.debug("JFlex: dump                : " + options.dump)
    log.debug("JFlex: dot                 : " + options.dot)
    log.debug("JFlex: verbose             : " + options.verbose)
  }
}

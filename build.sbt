sbtPlugin := true

name := "sbt-jflex"

organization := "de.sciss"

version := "0.3.0-SNAPSHOT"

scalaVersion := "2.9.2"

// crossScalaVersions := Seq("2.9.2", "2.9.1")

scalacOptions := Seq("-deprecation", "-unchecked")

description := "An sbt plugin that generates code based on a JFlex specification"

homepage := Some(url("https://github.com/Sciss/sbt-jflex"))

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

// ---- publishing ----

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  Some(if (v endsWith "-SNAPSHOT")
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra <<= name { n =>
<scm>
  <url>git@github.com:Sciss/{n}.git</url>
  <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
</scm>
<developers>
   <developer>
      <id>stefri</id>
      <name>Steffen Fritzsche</name>
      <url>http://scriptroom.de/</url>
   </developer>
   <developer>
      <id>sciss</id>
      <name>Hanns Holger Rutz</name>
      <url>http://www.sciss.de</url>
   </developer>
</developers>
}

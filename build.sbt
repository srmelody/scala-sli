organization := "net.wgen"

name := "My Scalatra Web App"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

seq(webSettings :_*)

classpathTypes ~= (_ + "orbit")

port in container.Configuration := 8081

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "repo.codahale.com" at "http://repo.codahale.com"

libraryDependencies ++= Seq(
  "org.scalatra" % "scalatra" % "2.1.1",
  "org.scalatra" % "scalatra-scalate" % "2.1.1",
  "org.scalatra" % "scalatra-specs2" % "2.1.1" % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container;test",
  "io.spray" % "spray-can" % "1.0-M7",
  "org.apache.httpcomponents" % "httpclient" % "4.2.2",
  "com.codahale" % "jerkson_2.9.1" % "0.5.0",
  "com.github.kevinsawicki" % "http-request" % "3.1" ,
  "net.databinder.dispatch" %% "dispatch-core" % "0.9.5",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
)


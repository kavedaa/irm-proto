name := "irm-proto"

scalaVersion := "3.1.0"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= {
  val akkaHttpV      = "10.2.9"
  val akkaV          = "2.6.19"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-xml" % akkaHttpV
  ).map(_.cross(CrossVersion.for3Use2_13))
}

libraryDependencies += "no.vedaadata" %% "html-util" % "0.1-SNAPSHOT"

val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "dname_changer",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "com.google.api-client" % "google-api-client" % "2.0.0",
      "com.google.oauth-client" % "google-oauth-client-jetty" % "1.34.1",
      "com.google.apis" % "google-api-services-drive" % "v3-rev20220815-2.0.0",
    )
  )


  //  implementation 'com.google.api-client:google-api-client:2.0.0'
  //   implementation 'com.google.oauth-client:google-oauth-client-jetty:1.34.1'
  //   implementation 'com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0'
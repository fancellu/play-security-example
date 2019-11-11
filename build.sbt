name := "play-security-example"
 
version := "1.0" 
      
lazy val `playsecurityexample` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq( ws , guice )

//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      
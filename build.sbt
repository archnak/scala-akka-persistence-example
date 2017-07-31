name := "akka-persistence-trial"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-persistence" % "2.5.3",
  "org.iq80.leveldb"            % "leveldb"          % "0.7",
"org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8")


    
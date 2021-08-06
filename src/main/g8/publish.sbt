val publication = "$name$"

ThisBuild / developers := List(
  Developer(
    id    = "mihai.surdeanu",
    name  = "Mihai Surdeanu",
    email = "mihai@surdeanu.info",
    url   = url("https://www.cs.arizona.edu/person/mihai-surdeanu")
  )
)
ThisBuild / homepage := Some(url(s"https://github.com/clulab/\$publication"))
ThisBuild / licenses := List(
  "Apache License, Version 2.0" ->
      url("http://www.apache.org/licenses/LICENSE-2.0.html")
)
ThisBuild / organization := "org.clulab"
ThisBuild / organizationHomepage := Some(url("http://clulab.org/"))
ThisBuild / organizationName := "Computational Language Understanding (CLU) Lab"
// The sonatype plugin seems to overwrite these two values.
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true
$if(artifactory.truthy)$
ThisBuild / publishTo := {
  // For artifactory, use this code.
  val artifactory = "http://artifactory.cs.arizona.edu:8081/artifactory/"
  val repository = "sbt-release-local"
  val details =
      if (isSnapshot.value) ";build.timestamp=" + new java.util.Date().getTime
      else ""
  val location = artifactory + repository + details
  val realm = "Artifactory Realm"

  Some(realm at location)
}
//ThisBuild / publishTo := {
//  // For maven, use this code.
//  val nexus = "https://oss.sonatype.org/"
//  if (isSnapshot.value)
//    Some("snapshots" at nexus + "content/repositories/snapshots")
//  else
//    Some("releases" at nexus + "service/local/staging/deploy/maven2")
//}
$else$
//ThisBuild / publishTo := {
//  // For artifactory, use this code.
//  val artifactory = "http://artifactory.cs.arizona.edu:8081/artifactory/"
//  val repository = "sbt-release-local"
//  val details =
//    if (isSnapshot.value) ";build.timestamp=" + new java.util.Date().getTime
//    else ""
//  val location = artifactory + repository + details
//  val realm = "Artifactory Realm"
//
//  Some(realm at location)
//}
ThisBuild / publishTo := {
  // For maven, use this code.
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
$endif$
ThisBuild / scmInfo := Some(
  ScmInfo(
    url(s"https://github.com/clulab/\$publication"),
    s"scm:git@github.com:clulab/\$publication.git"
  )
)

ThisBuild / Compile / packageBin / publishArtifact := true  // Do include the resources.
ThisBuild / Compile / packageDoc / publishArtifact := false // There is no documentation.
ThisBuild / Compile / packageSrc / publishArtifact := false // There is no source code.
ThisBuild / Test    / packageBin / publishArtifact := false

// Please add your credentials to ~/.sbt/.credentials rather than recording them in this build.sbt file.
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
// The credentials are recorded in a separate file, without the leading "// ", as such:
// realm=Artifactory Realm
// host=artifactory.cs.arizona.edu
// user=<user>
// password=<password>

// Please do not used them in a line like this:
// credentials += Credentials("Artifactory Realm", "artifactory.cs.arizona.edu", "me", "my_exposed_password")

# sbt-resource

This is a [Giter8](http://foundweekends.org/giter8) template for creating an `sbt` project that manages publication of resources for the [CLU Lab](http://github.com/clulab).  Being for a resource, there is no building involved beyond the zipping up of the files into a jar so that it can be readily accessed by Scala/Java.  It is specifically for the CLU Lab because it includes URLs to the lab's [Artifactory](http://artifactory.cs.arizona.edu:8081/artifactory/webapp/#/home) and [Sonatype]() servers, which wouldn't work for other groups.

To use this project as a template for an `sbt` project, do not clone it, but instead run
```
$ sbt new clulab/sbt-resource
```
and when prompted, give values for `name` and specify whether it is `artifactory` or `maven` that should be used to publish the resource.  This should result in an `sbt` project called `<name>` in the current directory of your hard drive.  Copy your resource to the appropriate directory, something under `src/main/resources`.  Files from there will be added to the `jar` file that gets published.  The project's `README.md` file should explain the steps of that process.

If you want to modify how the template gets generated, then do clone the project from `github` and change what you'd like.  Test it by running
```
$ sbt g8
```
and finding the results in the `./target/g8` directory.

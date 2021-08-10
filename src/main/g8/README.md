# $name$

The contents of this directory constitute an `sbt` project intended to release resources to either maven or artifactory, the later particularly when the resource is quite large (over 1GB).

Instructions:

1. Copy your resource(s) to ./src/main/resources/$package;format="packaged"$ or a subdirectory.
2. Update version.sbt and CHANGES.md and check on the LICENSE.
3. Inspect publish.sbt and replace values where necessary with those of your own project.
4. If not done previously, follow instructions in publish.sbt regarding credentials.
5. Replace this README.md file with something particular to your project.
6. In `sbt` issue the release command.

# $name$

$explanation$

This directory contains an `sbt` project intended to release resources to either maven or artifactory, the later particularly when the resource is quite large (over 1GB).

Instructions:

1. Overwrite the placeholder resource at `./src/main/resources/$package;format="packaged"$/$resource$` with a copy of your own resource.
2. Run `sbt test` to ensure that the resource is accessible.
3. Update `version.sbt` and `CHANGES.md` and check on the `LICENSE`.
4. Inspect `publish.sbt` and replace values where necessary with those of your own project.
5. If not done previously, follow instructions in `publish.sbt` regarding credentials.
6. Replace this `README.md` file with something particular to your project.
7. Issue the`sbt release` command and follow prompts.

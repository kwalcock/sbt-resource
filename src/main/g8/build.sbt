name := "$name$"
description := "$explanation$"

val scala11 = "2.11.12" // up to 2.11.12
val scala12 = "2.12.13" // up to 2.12.13
val scala13 = "2.13.5"  // up to 2.13.5

ThisBuild / crossScalaVersions := Seq(scala12, scala11)
ThisBuild / scalaVersion := crossScalaVersions.value.head

// Do not depend on Scala libraries since there is no code here.
ThisBuild / autoScalaLibrary := false
// This is only a resource.
ThisBuild / crossPaths := false

Compile / packageBin / mappings := {

  def mkMapping(filename: String): (File, String) = {
    // package;format="packaged" results in backlashes and
    // syntax errors on Windows, so this is converted manually.
    file(filename) -> ("$package$".replace('.', '/') + s"/\$filename")
  }

  // Remove placeholder files (.gitempty).
  val filtered = (mappings in (Compile, packageBin)).value.filter {
    case (_, name) => !name.endsWith(".gitempty")
  }

  // Put these files next to the model, in part so they don't conflict with other dependencies.
  filtered ++ Seq(
    mkMapping("README.md"),
    mkMapping("CHANGES.md"),
    mkMapping("LICENSE")
  )
}

// See https://github.com/earldouglas/xsbt-web-plugin/issues/115.
Compile / packageBin := {
  //  val log = streams.value.log
  //  val log = sLog.value

  ((Compile / packageBin).map { file: File =>
    // This is inside the map because otherwise there is an error message
    // [error] java.lang.IllegalArgumentException: Could not find proxy for val compress: Boolean
    val compress = $if(compress.truthy)$true$else$false$endif$

    if (compress)
      file
    else {
      import java.io.{FileInputStream, FileOutputStream, ByteArrayOutputStream}
      import java.util.zip.{CRC32, ZipEntry, ZipInputStream, ZipOutputStream}

      println(s" Start (re)packaging \${file.getName} with zero compression...")
      val zipInputStream = new ZipInputStream(new FileInputStream(file))
      val tmpFile = new File(file.getAbsolutePath + "_decompressed")
      val zipOutputStream = new ZipOutputStream(new FileOutputStream(tmpFile))
      zipOutputStream.setMethod(ZipOutputStream.STORED)
      Iterator
        .continually(zipInputStream.getNextEntry)
        .takeWhile(zipEntry => zipEntry != null)
        .foreach { zipEntry =>
          val byteArrayOutputStream = new ByteArrayOutputStream
          Iterator
            .continually(zipInputStream.read())
            .takeWhile(-1 !=)
            .foreach(byteArrayOutputStream.write)
          val bytes = byteArrayOutputStream.toByteArray
          zipEntry.setMethod(ZipEntry.STORED)
          zipEntry.setSize(byteArrayOutputStream.size)
          zipEntry.setCompressedSize(byteArrayOutputStream.size)
          val crc = new CRC32
          crc.update(bytes)
          zipEntry.setCrc(crc.getValue)
          zipOutputStream.putNextEntry(zipEntry)
          zipOutputStream.write(bytes)
          zipOutputStream.closeEntry
          zipInputStream.closeEntry
        }
      zipOutputStream.close
      zipInputStream.close
      if (!file.delete())
        println(s"The file \${file.getName} could not be deleted!")
      else if (!tmpFile.renameTo(file))
        println(s"The file \${file.getName} could not be renamed!")
      else
        println(s"Finish (re)packaging \${file.getName} with zero compression...")
      file
    }
  }).value
}

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.5" % Test // up to 3.2.5

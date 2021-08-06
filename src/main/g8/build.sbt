name := "$name$"
description := "$description$"

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
  // Remove placeholder files (.gitempty).
  val filtered = (mappings in (Compile, packageBin)).value.filter {
    case (file, name) => !name.endsWith(".gitempty")
  }

  // Put these files next to the model, in part so they don't conflict with other dependencies.
  filtered ++ Seq(
    file("./README.md") -> "org/clulab/glove/README.md",
    file("./CHANGES.md") -> "org/clulab/glove/CHANGES.md",
    file("./LICENSE") -> "org/clulab/glove/LICENSE",

    file(s"./resources/glove.840B.300d.10f.kryo") -> "org/clulab/glove/glove.840B.300d.10f.kryo"
  )
}

// See https://github.com/earldouglas/xsbt-web-plugin/issues/115.
Compile / packageBin := {
//  val log = streams.value.log
//  val log = sLog.value
  ((Compile / packageBin).map { file: File =>
    import java.io.{FileInputStream, FileOutputStream, ByteArrayOutputStream}
    import java.util.zip.{CRC32, ZipEntry, ZipInputStream, ZipOutputStream}

    println(s" Start (re)packaging ${file.getName} with zero compression...")
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
      println(s"The file ${file.getName} could not be deleted!")
    else
      if (!tmpFile.renameTo(file))
        println(s"The file ${file.getName} could not be renamed!")
      else
        println(s"Finish (re)packaging ${file.getName} with zero compression...")
    file
  }).value
}

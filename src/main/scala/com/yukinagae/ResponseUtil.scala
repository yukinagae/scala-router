package com.yukinagae

case class FileData(content: java.io.File, contentLength: Long, lastModified: java.util.Date)

object ResponseUtil {

  def fileResponse(filePath: String): Option[Response] = {
    val file = findFile(filePath)
    file.map { f =>
      val data = findData(f)
      Response(200,//
        Map("Content-Length" -> data.contentLength.toString,//
            "Last-Modified" -> data.lastModified.toString,//
            "Content-Type" -> guessContentType(data.content)//
        ),//
        scala.io.Source.fromFile(data.content).getLines.mkString)
    }
  }

  def guessContentType(file: java.io.File): String = {
    "text/plain" // TODO not implemented
  }

  def findData(file: java.io.File): FileData = FileData(file, file.length, lastModifiedDate(file))

  def lastModifiedDate(file: java.io.File): java.util.Date = new java.util.Date(file.lastModified)

  def findFile(path: String, root: Option[String] = None): Option[java.io.File] = {
    val file = safelyFindFile(path, root)
    file.map { f =>
      if(f.exists) Some(f) else None
    }.getOrElse(None)
  }

  def safelyFindFile(path: String, root: Option[String] = None): Option[java.io.File] = {
    root.map { r =>
      safePath(r, path) match {
        case true => Some(new java.io.File(r, path))
        case false => None
      }
    }.getOrElse(Some(new java.io.File(path)))
  }

  def safePath(root: String, path: String): Boolean = new java.io.File(root, path).getCanonicalPath().startsWith(new java.io.File(root).getCanonicalPath())

}


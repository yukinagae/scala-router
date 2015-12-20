package com.yukinagae

class Routes(val rs: Method*)

object Routes {
  def apply(rs: Method*): Request => Response = {
    ScalaRouter.routes(rs)
  }
}

object ScalaRouter {

  def routes(rs: Seq[Method]): Request => Response = {
    (request: Request) => {
      val params = buildParams(request)
      val response = rs.find { r =>
        methodMatched(request, r) && pathMatched(request, r.path)._1
      } match {
        case Some(best) => {
          val result = pathMatched(request, best.path) // TODO calling the same method twice!
	  val ps = params ++ result._2.getOrElse(Map.empty)
	  val body = best.handler(ps).toString // TODO should be converted regarding Types
	  Response.render(body).getOrElse(NOT_FOUND(request))
        }
        case None => NOT_FOUND(request)
      }
      response
    }
  }

  def methodMatched(request: Request, targetMethod: Method): Boolean = targetMethod == ANY || request.requestMethod == targetMethod.name

  def pathMatched(request: Request, targetPath: String): (Boolean, Option[Map[String, String]]) = {
    val result = Scalout.routeMatches(targetPath, request)
    result
  }

  def buildParams(r: Request): Map[String, String] = {
    if(r.URI.contains('?')) {
      val a = r.URI.split('?').last.split('&')
      val map = a.map(e => e.split("=").head -> e.split("=").last).toMap
      map
    } else {
      Map.empty
    }
  }

  def NOT_FOUND(request: Request): Response = Response(400, Map("Content-Type" -> "text/plain"), "Not Found")
}

trait Method {
  val path: String
  val handler: Map[String, String] => Any
  val name: String
}

case class GET(path: String, handler: Map[String, String] => Any, name: String = "GET") extends Method
case class POST(path: String, handler: Map[String, String] => Any, name: String = "POST") extends Method
case class ANY(path: String, handler: Map[String, String] => Any, name: String = "ANY") extends Method


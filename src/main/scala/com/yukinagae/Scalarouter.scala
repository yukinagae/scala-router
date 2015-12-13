package com.yukinagae

object ScalaRouter {

  type Action = Map[String, String] =>  Any

  def routes(rs: Seq[(Method, String, Action)]): Request => Response = {
    (request: Request) => {
      val params = buildParams(request)
      val response = rs.find { r =>
        methodMatched(request, r._1) && pathMatched(request, r._2)._1
      } match {
        case Some(best) => {
          val result = pathMatched(request, best._2) // TODO calling the same method twice!
	  val ps = params ++ result._2.getOrElse(Map.empty)
	  val body = best._3(ps).toString // TODO should be converted regarding Types
	  Response.render(body).getOrElse(NOT_FOUND(request))
        }
        case None => NOT_FOUND(request)
      }
      response
    }
  }

  def methodMatched(request: Request, targetMethod: Method): Boolean = targetMethod == Method.ANY || request.requestMethod == targetMethod.name

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

object Method {
  case object GET extends Method("GET")
  case object POST extends Method("POST")
  case object ANY extends Method("ANY")
}

sealed abstract class Method(val name: String) {
    // val name = name
}


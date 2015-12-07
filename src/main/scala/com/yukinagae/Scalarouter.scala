package com.yukinagae

object ScalaRouter {

	type Action = Map[String, String] =>  Any

  def routes(rs: Seq[(Method, String, Action)])(request: Request): Response = {

  	val params = buildParams(request)

		val response = rs.find(r => methodMatched(request, r._1) && pathMatched(request, r._2)) match {
			case Some(best) => {
				val body = best._3(params).toString // TODO should be converted regarding Types
				Response(200, Map("Content-Type" -> "text/plain"), body)
			}
			case None => NOT_FOUND(request)
		}
		response
	}

	def methodMatched(request: Request, targetMethod: Method): Boolean = request.requestMethod == targetMethod.name

	def pathMatched(request: Request, targetPath: String): Boolean = {
		println(targetPath)
		request.URI.startsWith(targetPath)
	}

	def buildParams(r: Request): Map[String, String] = {
		val a = r.URI.split('?').last.split('&')
		val map = a.map(e => e.split("=").head -> e.split("=").last).toMap
		map
	}

	def NOT_FOUND(request: Request): Response = Response(400, Map("Content-Type" -> "text/plain"), "Not Found")

}

object Method {
	case object GET extends Method("GET")
	case object POST extends Method("POST")
}

sealed abstract class Method(val name: String) {
    // val name = name
}

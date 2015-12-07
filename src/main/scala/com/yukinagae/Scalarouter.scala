package com.yukinagae

object ScalaRouter {

  def routes(rs: Seq[(Method, String, () => String)])(request: Request): Response = {
		val responses = rs.map { r =>
			ifMethod(r._1, r._3)(request)
		}
		responses.filter(_ != null).head
	}

	def HelloWorld(request: Request): Response = Response(200, Map("Content-Type" -> "text/plain"), "Hello World")

	def indexRoute(request: Request): Option[Response] = {
		if(request.requestMethod == Method.GET.name) {
  			Some(Response(200, Map("Content-Type" -> "text/plain"), "index page"))
  		} else {
  			None
  		}
	}

	def NOT_FOUND(request: Request): Response = Response(400, Map("Content-Type" -> "text/plain"), "Not Found")

	def ifMethod(method: Method, handler: () => String): Request => Response = {
		(req: Request) => {
			if(req.requestMethod == method.name) {
				val body = handler()
				Response(200, Map("Content-Type" -> "text/plain"), body)
			} else {
				null
			}
		}
	}

}

object Method {
	case object GET extends Method("GET")
	case object POST extends Method("POST")
}

sealed abstract class Method(val name: String) {
    // val name = name
}

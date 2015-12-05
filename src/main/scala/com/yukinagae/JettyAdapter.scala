package com.yukinagae

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.handler.DefaultHandler


object JettyAdapter {

  def run(handler: Request => Response): Server = {
  	val server = new Server(8080)
    val func = proxyHandler(handler)
  	server.setHandler(func)
  	try {
  		server.start
  		server
  	} catch {
      case ex: Exception => {
  			server.stop
  			throw ex
  		}
  	}
  }

  def proxyHandler(handler: Request => Response): AbstractHandler = {
    new JettyHandler(handler)
  }

}

class JettyHandler(handler: Request => Response) extends AbstractHandler {
  import javax.servlet.http.HttpServletRequest
  import javax.servlet.http.HttpServletResponse
  def handle(target: String, baseRequest: org.eclipse.jetty.server.Request, request: HttpServletRequest, response: HttpServletResponse) {

    val req = ServletUtil.buildRequest(request)
    println(req)
    val res = handler(req)

    response.setStatus(res.status)
    res.headers.foreach { case (k, v) =>
      response.setHeader(k, v)
    }
    response.getWriter().print(res.body)

    baseRequest.setHandled(true)
  }
}

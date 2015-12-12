package com.yukinagae

case class Response(//
	status: Int,//
	headers: Map[String, String],//
	body: String//
	)

object Response {

  val defaultHeaders = Map("Content-Type" -> "text/html; charset=utf-8")

  def render(body: String): Option[Response] = {
    if(body == null || body.isEmpty) {
      None
    } else {
      Some(Response(200, defaultHeaders, body))
    }
  }
}

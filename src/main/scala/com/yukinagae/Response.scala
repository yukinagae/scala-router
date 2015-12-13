package com.yukinagae

case class Response(//
	status: Int,//
	headers: Map[String, String],//
	body: String//
	)

object Response {

  val defaultHeaders = Map("Content-Type" -> "text/html; charset=utf-8")

  def render(body: String = ""): Option[Response] = if(body.isEmpty) None else Some(Response(200, defaultHeaders, body))

  def render(body: Seq[String]): Option[Response] = render(body.mkString)
  
  def render(body: => Response): Option[Response] = Some(body)

  import scala.concurrent.{Future, Await}
  import scala.util.{Success, Failure, Try}
  import scala.concurrent.duration._
  def render(body: Future[Response])(implicit ec: scala.concurrent.ExecutionContext): Option[Response] = {
    val result = Await.ready(body, Duration.Inf).value.get
    result match {
      case Success(result) => Some(result)
      case Failure(e) => None
    }
  }

  def render(body: java.io.File): Option[Response] = {
    body.exists match {
      case true => ResponseUtil.fileResponse(body.getCanonicalPath)
      case false => None
    }
  }

}


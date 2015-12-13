package com.yukinagae

import org.scalatest.FlatSpec

class ResponseSpec extends FlatSpec {

  val expectedResponse = Response(200, Map("Content-Type" -> "text/html; charset=utf-8"), "<h1>Foo</h1>")

  "Response" should "with empty" in {
    assert(Response.render("") == None)
  }

  "Response" should "with string" in {
    assert(Response.render("<h1>Foo</h1>") == Some(expectedResponse))
  }

  "Response" should "with string seq" in {
    assert(Response.render(Seq("<h1>", "Foo", "</h1>")) == Some(expectedResponse))
  }

  "Response" should "with handler function" in {
    assert(Response.render({ expectedResponse }) == Some(expectedResponse))
  }

  "Response" should "with future handler function" in {
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global
    val future = Future { expectedResponse }
    assert(Response.render(future) == Some(expectedResponse))
  }

  "Response" should "with file" in {
    val response = Response.render(new java.io.File("src/test/scala/com/yukinagae/test.txt")).get
    assert(response.headers.get("Content-Length").get.toLong == 7)
    assert(response.headers.get("Content-Type").get == "text/plain")
    assert(!response.headers.get("Last-Modified").get.isEmpty)
    assert(response.body == "foobar")
  }

}


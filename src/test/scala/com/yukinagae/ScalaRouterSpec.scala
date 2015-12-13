package com.yukinagae

import org.scalatest.FlatSpec
//import com.yukinagae.Method._
import org.eclipse.jetty.server.Server
import scalaj.http._

class ScalaRouterSpec extends FlatSpec {

  def FAIL = fail

  "ScalaRouter" should "method matching" in {
    
    def foo = "foo page"

    val routes = ScalaRouter.routes(Seq(//
      POST("/foo", p => FAIL),//
      GET("/foo", p => foo),//
      ANY("/foo", p => FAIL)))// 
    val request = Request(8080, "localhost", "127.0.0.1", "/foo", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

  "ScalaRouter" should "vector arguments" in {

    def foo(x: String) = {
      assert(x == "1")
      "foo page"
    }

    val routes = ScalaRouter.routes(Seq(//
      GET("/foo", p => foo(p.get("x").get)),//
      GET("/bar", p => FAIL)))
    val request = Request(8080, "localhost", "127.0.0.1", "/foo?x=1", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

  "ScalaRouter" should "one symbol matching" in {

    def foo(ps: Map[String, String]) = {
      assert(ps == Map(":x" -> "foo", "x" -> "bar", "y" -> "baz"))
      "foo page"
    }

    val routes = ScalaRouter.routes(Seq(//
      GET("/:x", p => foo(p)),//
      GET("/foo", p => FAIL)))
    val request = Request(8080, "localhost", "127.0.0.1", "/foo?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

  "ScalaRouter" should "two symbol matching" in {

    def foo(ps: Map[String, String]) = {
      assert(ps == Map(":a" -> "foo", ":b" -> "2","x" -> "bar", "y" -> "baz"))
      "foo page"
    }

    val routes = ScalaRouter.routes(Seq(//
      GET("/:a/:b", p => foo(p)),//
      GET("/foo", p => FAIL)))
    val request = Request(8080, "localhost", "127.0.0.1", "/foo/2?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

  "ScalaRouter" should "routes" in {
    
    def foo(x: String, y: String) = {
      assert(x == "bar")
      assert(y == "baz")
      "foo page"
    }

    val routes = ScalaRouter.routes(Seq(//
      GET("/foo", p => foo(p.get("x").get, p.get("y").get)),//
      GET("/bar", p => FAIL)))
    val request = Request(8080, "localhost", "127.0.0.1", "/foo?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

}


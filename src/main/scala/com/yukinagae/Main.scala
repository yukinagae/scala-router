package com.yukinagae

import com.yukinagae.Method.GET

object Main extends App {

  def index(ps: Map[String, String]): String = {
    println(ps)
    "index"
  }

  def foo(ps: Map[String, String]): String = {
    println(ps)
    "foo"
  }

  def bar(ps: Map[String, String]): String = {
    println(ps)
    "bar"
  }

  val routes = ScalaRouter.routes(Seq(//
    (GET, "/", index),//
    (GET, "/:x", foo),//
    (GET, "/bar", bar)))
    
    JettyAdapter.run(routes)

}


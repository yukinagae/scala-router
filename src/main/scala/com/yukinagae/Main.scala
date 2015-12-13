package com.yukinagae

import scalatags.Text.all._

object Main extends App {

  // parameter alias type
  type Param = Map[String, String]

  // routing config
  val routes = ScalaRouter.routes(Seq(//
    GET("/", index),//
    GET("/:id", show),//
    POST("/", create)))
    
  // run jetty
  JettyAdapter.run(routes)

  // below are handlers
  def index = { param: Param =>
    html(
      body(
        h1("index page"),
        form(method:="POST", action:="/")(
          button("type".attr:="submit")("new")))).toString
  }

  def show = { param: Param => param.get(":id").map { id: String =>
    html(
      body(
        h1("show page"),
        div(
          p(id)))).toString
  }.get } // "get" must be successful after checking routing path contains ":id"

  def create = { param: Param =>
    println(param)
    html(
      body(
        h1("create page"))).toString
  }

}


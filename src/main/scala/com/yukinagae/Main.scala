package com.yukinagae

import scalatags.Text.all._

object Main extends App {

  // routing config
  val routes = Routes(//
    GET("/", index),//
    GET("/:id", show),//
    POST("/", create))
    
  // run server
  Server.run(routes)

  // below are handlers
  def index = { param: Map[String, String] =>
    html(
      body(
        h1("index page"),
        form(method:="POST", action:="/")(
          button("type".attr:="submit")("new")))).toString
  }

  def show = { param: Map[String, String] =>
    param.get(":id").map { id: String =>
      html(
        body(
          h1("show page"),
          div(
            p(id)))).toString
    }.get
  } // "get" must be successful after checking routing path contains ":id"

  def create = { param: Map[String, String] =>
    println(param)
    html(
      body(
        h1("create page"))).toString
  }

}


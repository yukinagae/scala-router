package com.yukinagae

import scalatags.Text.all._

object Main extends App {

  // routing config
  val routes = ScalaRouter.routes(Seq(//
    GET("/", p => index),//
    GET("/:id", p => show(p.get(":id").get)),// "get" must be successful after checking routing path contains ":id"
    POST("/", p => create(p))))
    
  // run jetty
  JettyAdapter.run(routes)

  // below are handlers
  def index = {
    html(
      body(
        h1("index page"),
        form(method:="POST", action:="/")(
          button("type".attr:="submit")("new")))).toString
  }

  def show = { id: String =>
    html(
      body(
        h1("show page"),
        div(
          p(id)))).toString
  }

  def create = { ps: Map[String, String] =>
    html(
      body(
        h1("create page"))).toString
  }

}


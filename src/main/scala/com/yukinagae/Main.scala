package com.yukinagae

import com.yukinagae.Method.{ GET, POST }
import scalatags.Text.all._

object Main extends App {

  // routing config
  val routes = ScalaRouter.routes(Seq(//
    (GET, "/", index),//
    (GET, "/:id", show),//
    (POST, "/", create)))
    
  // run jetty
  JettyAdapter.run(routes)

  // below are handlers
  def index(ps: Map[String, String]): String = {
    html(
      body(
        h1("index page"),
        form(method:="POST", action:="/")(
          button("type".attr:="submit")("new")))).toString
  }

  def show(ps: Map[String, String]): String = {
    html(
      body(
        h1("show page"),
        div(
          p(ps.get(":id").get)))).toString
  }

  def create(ps: Map[String, String]): String = {
    html(
      body(
        h1("create page"))).toString
  }

}


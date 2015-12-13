package com.yukinagae

import com.yukinagae.Method.{ GET, POST }

object Main extends App {

  def index(ps: Map[String, String]): String = {
    println(ps)
    s"""
    <h1>index page</h1>
    <form method="POST" action="/">
      <button type="submit">new</button>
    </form>
    """
  }

  def show(ps: Map[String, String]): String = {
    println(ps)
    s"""
    <h1>show page</h1>
    <p>${ps.get(":id").get}</p>
    """
  }

  def create(ps: Map[String, String]): String = {
    println(ps)
    s"""
    <h1>create page</h1>
    """
  }

  val routes = ScalaRouter.routes(Seq(//
    (GET, "/", index),//
    (GET, "/:id", show),//
    (POST, "/", create)))
    
    JettyAdapter.run(routes)

}


package com.yukinagae

object Validator {

  def validate(m: Map[String, String], vs: List[(String, String => Boolean, String)]): Map[String, List[String]] = {
    // merge two maps http://stackoverflow.com/questions/20047080/scala-merge-map
    val merged = vs.map { v =>
      val f = validation_on(v._1, v._2, v._3)
      f(m)
    } map { x =>
      x.toSeq
    } reduce { (first, second) =>
      first ++ second
    }
    val grouped = merged.groupBy(_._1)
    val cleaned = grouped.mapValues(_.map(_._2).toList.flatten)
    cleaned
  }

  def validation_on(key: String, pred: String => Boolean, message: String): Map[String, String] => Map[String, List[String]] = {
    (param: Map[String, String]) => {
      param.get(key) match {
        case Some(value) => if(!pred(value)) Map(key -> List(message)) else Map.empty[String, List[String]]
        case None => if(!pred(null)) Map(key -> List(message)) else Map.empty[String, List[String]]
      }
    }
  }

}

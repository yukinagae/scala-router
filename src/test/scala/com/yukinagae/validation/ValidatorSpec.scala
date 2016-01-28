package com.yukinagae

import org.scalatest.FlatSpec

class ValidatorSpec extends FlatSpec {

  import com.yukinagae.Validator._

  val present_? = (s: String) => s != null

  "Validator" should "validation on" in {
    val v = validation_on("x", (x: String) => x.toInt <= 0, "error")
    assert(v(Map("x" -> "1")) == Map("x" -> List("error")))
    assert(v(Map("x" -> "0")) == Map.empty)
  }

  "Validator" should "validate" in {
    val vs = List(//
      ("x", present_?, "must be present"),
      ("y", present_?, "must be present"),
      ("x", (s: String) => s.toInt > 18, "must be greater than 18"))
    
    val results = validate(Map("x" -> "17"), vs)
    assert(results == Map("x" -> List("must be greater than 18"), "y" -> List("must be present")))
  }

}

package com.yukinagae

import org.scalatest.FlatSpec

class ResponseUtilSpec extends FlatSpec {

  "ResponseUtil" should "file response" in {
    val response = ResponseUtil.fileResponse("src/test/scala/com/yukinagae/routing/test.txt").get
    assert(response.status == 200)
    assert(response.headers.keys == Set("Content-Length", "Last-Modified", "Content-Type"))
    assert(response.body == "foobar")
  }

}

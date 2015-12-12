import org.scalatest.FlatSpec
import com.yukinagae.Response

class ResponseSpec extends FlatSpec {

  val expectedResponse = Response(200, Map("Content-Type" -> "text/html; charset=utf-8"), "<h1>Foo</h1>")

  "Response" should "with null" in {
    assert(Response.render(null) == None)
  }

  "Response" should "with empty" in {
    assert(Response.render("") == None)
  }

  "Response" should "with string" in {
    assert(Response.render("<h1>Foo</h1>") == Some(expectedResponse))
  }

}

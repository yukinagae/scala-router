import org.scalatest.FlatSpec
import com.yukinagae.ScalaRouter
import com.yukinagae.Method
import com.yukinagae.Method._
import com.yukinagae.Request
import com.yukinagae.Response
import org.eclipse.jetty.server.Server
import scalaj.http._

class ScalaRouterSpec extends FlatSpec {

	def FAIL(ps: Map[String, String]) = fail

  "ScalaRouter" should "vector arguments" in {

 		def foo(ps: Map[String, String]) = {
      assert(ps == Map("x" -> "bar", "y" -> "baz"))
  		"foo page"
  	}

		val routes = ScalaRouter.routes(Seq(//
			(GET, "/foo", foo),//
			(GET, "/bar", FAIL)//
			))(_)
		val request = Request(8080, "localhost", "127.0.0.1", "/foo?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
		val response = routes(request)
		assert(response.body == "foo page")
  }

  "ScalaRouter" should "one symbol matching" in {

 		def foo(ps: Map[String, String]) = {
      assert(ps == Map(":x" -> "foo", "x" -> "bar", "y" -> "baz"))
  		"foo page"
  	}

		val routes = ScalaRouter.routes(Seq(//
			(GET, "/:x", foo),//
			(GET, "/foo", FAIL)//
			))(_)
		val request = Request(8080, "localhost", "127.0.0.1", "/foo?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
		val response = routes(request)
		assert(response.body == "foo page")
  }

  "ScalaRouter" should "two symbol matching" in {

    def foo(ps: Map[String, String]) = {
      assert(ps == Map(":a" -> "foo", ":b" -> "2","x" -> "bar", "y" -> "baz"))
      "foo page"
    }

    val routes = ScalaRouter.routes(Seq(//
      (GET, "/:a/:b", foo),//
      (GET, "/foo", FAIL)//
      ))(_)
    val request = Request(8080, "localhost", "127.0.0.1", "/foo/2?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

}

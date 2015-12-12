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

  "ScalaRouter" should "method matching" in {

    def foo(ps: Map[String, String]) = {
      assert(ps == Map.empty)
      "foo page"
    }

    val routes = ScalaRouter.routes(Seq(//
      (POST, "/foo", FAIL),//
      (GET, "/foo", foo),//
      (ANY, "/foo", FAIL)))
    val request = Request(8080, "localhost", "127.0.0.1", "/foo", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

  "ScalaRouter" should "vector arguments" in {

 		def foo(ps: Map[String, String]) = {
      assert(ps == Map("x" -> "1"))
  		"foo page"
  	}

		val routes = ScalaRouter.routes(Seq(//
			(GET, "/foo", foo),//
			(GET, "/bar", FAIL)))
		val request = Request(8080, "localhost", "127.0.0.1", "/foo?x=1", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
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
			(GET, "/foo", FAIL)))
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
      (GET, "/foo", FAIL)))
    val request = Request(8080, "localhost", "127.0.0.1", "/foo/2?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

  "ScalaRouter" should "routes" in {
    def foo(ps: Map[String, String]) = {
      assert(ps == Map("x" -> "bar", "y" -> "baz"))
      "foo page"
    }

    val routes = ScalaRouter.routes(Seq(//
      (GET, "/foo", foo),//
      (GET, "/bar", FAIL)))
    val request = Request(8080, "localhost", "127.0.0.1", "/foo?x=bar&y=baz", "nashi", "http", "GET", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
    val response = routes(request)
    assert(response.body == "foo page")
  }

}

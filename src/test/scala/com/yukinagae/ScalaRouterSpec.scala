import org.scalatest.FlatSpec
import com.yukinagae.ScalaRouter
import com.yukinagae.Method
import com.yukinagae.Method._
import com.yukinagae.Request
import com.yukinagae.Response
import org.eclipse.jetty.server.Server
import scalaj.http._

class ScalaRouterSpec extends FlatSpec {

	val testRoutes: Seq[(Method, String, () => String)] = Seq(//
		(GET, "/", index),//
		(GET, "/", index2),//
		(POST, "/post", postIndex)//
	)

	def index(): String = "index page"
	def index2(): String = "index2 page"
	def postIndex(): String = "post index page"

	val routes = ScalaRouter.routes(testRoutes)(_)
	println(routes)
	val request = Request(8080, "localhost", "127.0.0.1", "/", "nashi", "http", "POST", "proto", Map("version" -> "333"), "text/plain", 5, null, null)
	val response = routes(request)
	println(response)

}

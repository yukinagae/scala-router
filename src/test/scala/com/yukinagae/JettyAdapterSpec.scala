import org.scalatest.FlatSpec
import com.yukinagae.JettyAdapter
import com.yukinagae.Request
import com.yukinagae.Response
import org.eclipse.jetty.server.Server
import scalaj.http._

class JettyAdapterSpec extends FlatSpec {

  var server: Option[Server] = None
  val testPort = 8080
  val testUrl = "http://localhost:" + testPort

  override def withFixture(test: NoArgTest) = {
    try super.withFixture(test) // Invoke the test function
      finally {
        server.map(_.stop)
    }
  }

  def HelloWorld(request: Request): Response = {
  	Response(200, Map("Content-Type" -> "text/plain"), "Hello World")
  }

  "JettyAdapter" should "run" in {
    server = Some(JettyAdapter.run(HelloWorld))
    val response: HttpResponse[String] = Http(testUrl).asString
  	println(response) // TODO
  	assert(response.code == 200)
  	response.headers.get("Content-Type") match {
  		case Some(x) => assert(x.exists(v => v.startsWith("text/plain")))
  		case None => fail
  	}
  	assert(response.body == "Hello World")
  }
}

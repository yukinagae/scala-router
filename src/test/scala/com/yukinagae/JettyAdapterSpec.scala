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

  def getCCParams(cc: AnyRef) = (Map[String, String]() /: cc.getClass.getDeclaredFields) {(a, f) =>
    f.setAccessible(true)
  	f.get(cc) match {
  		case null => a + (f.getName -> "null")
  		case _ => a + (f.getName -> f.get(cc).toString)
  	}
  }

  def HelloWorld(request: Request): Response = {
  	Response(200, Map("Content-Type" -> "text/plain"), "Hello World")
  }

  def echoHandler(request: Request): Response = {
  	val reqJson = scala.util.parsing.json.JSONObject(getCCParams(request))
   	Response(200, Map("request-map" -> reqJson.toString), request.body)
  }

  "JettyAdapter" should "Hello World" in {
    server = Some(JettyAdapter.run(HelloWorld))
    val response: HttpResponse[String] = Http(testUrl).asString
  	// println(response) // TODO log
  	assert(response.code == 200)
  	response.headers.get("Content-Type") match {
  		case Some(x) => assert(x.exists(v => v.startsWith("text/plain")))
  		case None => fail
  	}
  	assert(response.body == "Hello World")
  }

  "JettyAdapter" should "echo request" in {
  	server = Some(JettyAdapter.run(echoHandler))
    val response: HttpResponse[String] = Http(testUrl + "/foo/bar/baz?surname=jones&age=123").postData("hello").asString
  	// println(response) // TODO log
  	assert(response.code == 200)
  	assert(response.body == "hello")
  	response.headers.get("request-map") match {
  		case Some(x) => {
  			val r = scala.util.parsing.json.JSON.parseFull(x.head)
  			val m = r.get.asInstanceOf[Map[String, String]];
  			for { // TODO should consider when some parameters are None
  				queryString <- m.get("queryString")
  				URI <- m.get("URI")
  				contentLength <- m.get("contentLength")
  				characterEncoding <- m.get("characterEncoding")
  				requestMethod <- m.get("requestMethod")
  				contentType <- m.get("contentType")
  				remoteAddr <- m.get("remoteAddr")
  				scheme <- m.get("scheme")
  				serverName <- m.get("serverName")
  				serverPort <- m.get("serverPort")
  				body <- m.get("body")
  			} yield {
  				assert(queryString == "surname=jones&age=123")
  				assert(URI == "/foo/bar/baz")
  				assert(contentLength.toInt == 5)
  				// assert(characterEncoding == "UTF-8") // TODO should fail because of charset is null
  				assert(requestMethod == "POST")
  				assert(contentType == "application/x-www-form-urlencoded")
  				assert(remoteAddr == "127.0.0.1")
  				assert(scheme == "http")
  				assert(serverName == "localhost")
  				assert(serverPort.toInt == testPort)
  				assert(body == "hello")
  			}
  		}
  		case None => fail
  	}
  }
}

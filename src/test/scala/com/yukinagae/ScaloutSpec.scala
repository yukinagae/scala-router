import org.scalatest.FlatSpec
import com.yukinagae.Scalout._

class ScaloutSpec extends FlatSpec {

  "Scalout" should "fixed path" in {
  	assert(routeMatches("/", Map("URI" -> "/"))._1)
  	assert(routeMatches("/foo", Map("URI" -> "/foo"))._1)
  	assert(routeMatches("/foo/bar", Map("URI" -> "/foo/bar"))._1)
  	assert(routeMatches("/foo/bar.html", Map("URI" -> "/foo/bar.html"))._1)
  }

  "Scalout" should "keyword path" in {
  	assert(routeMatches("/:x", Map("URI" -> "/foo")) == (true, Some(Map(":x" -> "foo"))))
  	assert(routeMatches("/:x/:y", Map("URI" -> "/foo/bar")) == (true, Some(Map(":x" -> "foo", ":y" -> "bar"))))
  }

}

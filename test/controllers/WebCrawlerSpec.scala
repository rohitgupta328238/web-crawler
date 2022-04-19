package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue}
import play.api.mvc.Headers
import play.api.test.Helpers._
import play.api.test._

import scala.collection.IndexedSeq

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class WebCrawlerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  val requestBody = JsObject(collection.Seq(("urls", JsArray(IndexedSeq(JsString("https://www.google.com"), JsString("https://github.com"))))))

  "WebCrawler POST" should {

    "return response from a new instance of controller" in {
      val controller = new WebCrawler(stubControllerComponents())
      val home = controller.crawl()
        .apply(FakeRequest[JsValue](POST, "/crawl", Headers(("Content-Type", "application/json")), requestBody))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
      contentAsString(home) must include ("Welcome to Crawl")
    }

    "return response from the application" in {
      val controller = inject[WebCrawler]
      val home = controller.crawl()
        .apply(FakeRequest[JsValue](POST, "/crawl", Headers(("Content-Type", "application/json")), requestBody))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
      contentAsString(home) must include ("Welcome to Crawl")
    }
  }
}

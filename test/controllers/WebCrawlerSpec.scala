package controllers

import common.{CrawlRequest, CrawlResponse, CrawlResponseElement}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Headers
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class WebCrawlerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit val crawlRequestWrites = Json.writes[CrawlRequest]
  implicit val crawlResponseElementReads = Json.reads[CrawlResponseElement]
  implicit val crawlResponseReads = Json.reads[CrawlResponse]

  val requestBody = CrawlRequest(Set("https://www.google.com", "https://github.com"))
  val requestBodyJson = Json.toJson(requestBody)

  "WebCrawlerController POST" should {

    "return response from the application" in {
      val controller = inject[WebCrawlerController]
      val crawlResponse = controller.crawl()
        .apply(FakeRequest[JsValue](POST, "/crawl", Headers(("Content-Type", "application/json")), requestBodyJson))

      status(crawlResponse) mustBe OK
      contentType(crawlResponse) mustBe Some("application/json")

      val responseJson: JsValue = contentAsJson(crawlResponse)
      val responseObject = responseJson.validate[CrawlResponse]
      responseObject.isSuccess mustBe  true
      responseObject.get.result.size mustEqual 2
      responseObject.get.error mustBe None
    }
  }
}

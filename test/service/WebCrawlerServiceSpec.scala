package service

import common.CrawlRequest
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import utils.RestService

class WebCrawlerServiceSpec extends AnyWordSpec with MockitoSugar with ScalaFutures with Matchers {

  "WebCrawlerService processRequest" should {

    "return response from the method" in {
        val request = CrawlRequest(Set("https://www.google.com", "https://github.com"))
      val service = new WebCrawlerService

      val restService = mock[RestService]

      //stub mock
      request.urls.map(url => when(restService.restGetCall(url)).thenReturn(   s"$url: response received"))

      val response = service.processRequest(request, restService)
      whenReady(response){ asyncResult =>
        asyncResult.result.size mustBe 2
        asyncResult.error mustBe None
      }
    }
  }
}

package service

import common.{CrawlRequest, CrawlResponseElement}
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class WebCrawlerService {
  val logger: Logger = Logger(this.getClass())

  def processRequest(crawlRequest: CrawlRequest): Set[Future[CrawlResponseElement]] = {
    val setOfFutures: Set[Future[CrawlResponseElement]] = crawlRequest.urls.map { url =>
      Future(CrawlResponseElement(url, s"Crawled $url"))
    }
    setOfFutures
  }
}

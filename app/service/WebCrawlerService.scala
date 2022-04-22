package service

import common.{CrawlRequest, CrawlResponse, CrawlResponseElement}
import play.api.Logger
import utils.Utils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class WebCrawlerService {
  val logger: Logger = Logger(this.getClass())

  def processRequest(crawlRequest: CrawlRequest): Future[CrawlResponse] = {
    //Process in parallel with the help of Futures.
    val setOfFutureResponse: Set[Future[Either[Throwable, CrawlResponseElement]]] = crawlRequest.urls.map { url =>
      Future{
        Right(CrawlResponseElement(url, Utils.restGetCall(url)))
      }.recover {
        case throwable: Throwable =>
          logger.error(s"Error occurred when fetching the GET for url: $url: ${throwable.getMessage}")
          Left(new Exception(s"Error occurred when fetching the GET for url: $url: ${throwable.getMessage}"))
      }
    }

    val response: Future[CrawlResponse] = Future.sequence(setOfFutureResponse).map { setOfResults: Set[Either[Throwable, CrawlResponseElement]] =>
      val (errors, values) = setOfResults.partition(_.isLeft) // partitions an either in set of left and set of right.
      val results: Set[CrawlResponseElement] = values.foldLeft(Set[CrawlResponseElement]())((acc, e) => acc + e.right.get)

      errors.isEmpty match {
        case true => // all good.
          CrawlResponse(results, None)
        case false => // At least one error is there. Concatenate all the errors and send the response.
          CrawlResponse(results, Some(errors.foldLeft("")((acc, s) => s"$acc|${s.left.get.getMessage}")))
      }
    }
    response
  }
}

package controllers

import common.{CrawlRequest, CrawlResponse, CrawlResponseElement}
import play.api.Logger

import javax.inject._
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * web crawler endpoint.
 */
@Singleton
class WebCrawlerController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val logger: Logger = Logger(this.getClass())
  /**
   * Create an Action to returned the crawled response for urls present in request.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `POST` request with
   * a path of `/crawl`.
   */

  implicit val crawlRequestReads = Json.reads[CrawlRequest]
  implicit val crawlResponseElementWrites = Json.writes[CrawlResponseElement]
  implicit val crawlResponseWrites = Json.writes[CrawlResponse]

  def crawl() = Action.async(parse.json) { implicit request: Request[JsValue] =>

    val body: JsValue = request.body
    val jsonBody: String = body.toString()

    logger.info("body: " + jsonBody)

    val crawlRequest: JsResult[CrawlRequest] =  body.validate[CrawlRequest]

    crawlRequest match {
      case JsSuccess(crawlRequestObj, _) =>
        val setOfFutures = crawlRequestObj.urls.map { url =>
          Future(CrawlResponseElement(url, s"Crawled $url"))
        }

        val response: Future[CrawlResponse] = Future.sequence(setOfFutures).map { setOfResults: Set[CrawlResponseElement] =>
          CrawlResponse(setOfResults, None)
        }

        response.map(crawlResponse => Ok(Json.toJson(crawlResponse)))
      case JsError(errors) =>
        logger.error(s"Error occurred during processing the crawl request: ${errors.toString()}")
        Future(BadRequest(s"Error occurred during processing the crawl request: ${errors.toString()}"))
    }
  }
}
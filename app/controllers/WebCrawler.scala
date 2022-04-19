package controllers

import javax.inject._
import play.api.libs.json.JsValue
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * web crawler endpoint.
 */
@Singleton
class WebCrawler @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def crawl() = Action.async(parse.json) { implicit request: Request[JsValue] =>
    Future(Ok("Welcome to Crawl"))
  }
}

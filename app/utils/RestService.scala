package utils

import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import play.api.Logger

trait RestService {
  def restGetCall(url: String): String
}

object RestServiceAdaptor extends RestService {
  val logger: Logger = Logger(this.getClass())
  override def restGetCall(url: String): String = {
    try {
      val request = Unirest.get(url)
        .header("Accept", "text/html")

      val stringHttpResponse = request.asString
      val status = stringHttpResponse.getStatus

      if (200 == status) {
        logger.info("Successfully got the GET Response: " + url)
        stringHttpResponse.getBody
      } else if (400 == status) {
        throw new Exception("Failed to get the GET response due to the error (400, bad request): " + stringHttpResponse.getBody)
      }
      else if (404 == status) {
        throw new Exception("Failed to get the GET response due to the error (404, Not found): " + stringHttpResponse.getBody)
      } else {
        val message = "Failed to get the GET response, error =\"" + stringHttpResponse.getBody + "\""
        logger.info(message)
        throw new Exception(message)
      }
    } catch {
      case ex: UnirestException => throw new Exception(s"Unirest exception during GET response for url $url", ex)
    }
  }
}
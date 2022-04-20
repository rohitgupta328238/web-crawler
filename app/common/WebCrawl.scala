package common

case class CrawlRequest(urls: Set[String])

case class CrawlResponseElement(url: String, data: String)
case class CrawlResponse(result: Set[CrawlResponseElement], error: Option[String])

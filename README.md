# web-crawler
web crawler for http urls

This project is play framework based scala project to crawl http urls.

#Commands to run the server
git clone https://github.com/rohitgupta328238/web-crawler.git

1. cd ${prodectRoorDir}         # web-crawler

2. sbt run                 #wait for server to start (may take time initially)

3. Curl for calling the endpoint


    curl --location --request POST 'http://localhost:9000/api/crawl' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "urls": ["https://www.google.com", "https://www.github.com"]
    }   '
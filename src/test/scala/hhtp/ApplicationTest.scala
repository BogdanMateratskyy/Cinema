package hhtp

import com.softwaremill.sttp._
import http.Application
import org.scalatest.{Matchers, WordSpec}

class ApplicationTest extends WordSpec with Matchers{
  "Service" should {
    "respons on target port" in {
      implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
      Application.start()
      sttp.get(uri"http://localhost:9000/").send().code shouldBe 200
    }
  }
}

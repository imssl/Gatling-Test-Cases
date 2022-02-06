package drm

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._


class WV_open extends Simulation {

  val rps = Integer.getInteger("rps",220).toDouble
  val duration = Integer.getInteger("duration",3600).toDouble
  val host = System.getProperty("host","https://URL")

  printf("RPS=%s", rps)
  println()
  printf("Duration=%s", duration)
  println()
  printf("Host=%s", host)
  println()


  val httpProtocol = http
    .baseUrl(host)
    .header("Token", "TOKEN")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatlling-v3.4.1-test")

  val scn = scenario("AcquireLicense")
    .exec(
      http("request_1")
        .post("/AcquireLicense")
        .body(RawFileBody("LicenseRequest.bin"))
    )

  setUp(
        scn.inject(
                rampUsersPerSec(0) to (rps) during (60 seconds),
                constantUsersPerSec(rps) during ((duration) seconds),
                rampUsersPerSec(rps) to 0 during (60 seconds)
                ).protocols(httpProtocol))
}

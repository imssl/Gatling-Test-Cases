import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FP_closed extends Simulation {

  val users = Integer.getInteger("users",4).toInt
  val duration = Integer.getInteger("duration",3600).toDouble
  val host = System.getProperty("host","https://URL")

  printf("Users=%s", users)
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
        .body(RawFileBody("DIRECTORY"))
    )

  setUp(
scn.inject(
                rampConcurrentUsers(0).to(users).during(10.seconds),
                constantConcurrentUsers(users).during(duration.seconds),
                rampConcurrentUsers(users).to(0).during(10.seconds)
                ).protocols(httpProtocol))
}

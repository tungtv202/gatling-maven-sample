package me.tungexplorer

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class SampleSimulation extends Simulation {

  def httpCall(): HttpRequestBuilder = http("Request to endpoint /test")
    .get("/test")
    .queryParam("greetingEmail", "${email}")
    .queryParam("secondParam", StringBody(session => s"""{ "orderReference": "${generateSecondParam()}" }"""))

  def generateSecondParam(): String = Random.alphanumeric.take(20).mkString

  val feeder: Iterator[Map[String, String]] = Iterator.continually {
    Map("email" -> s"${Random.alphanumeric.take(20).mkString}@foo.com")
  }

  def generate(): ScenarioBuilder =
    scenario("Call http test")
      .exec(feed(feeder)
        .exec(httpCall()
          .check(status.is(200))))

  def httpProtocol: HttpProtocolBuilder = http
    .baseUrl("https://webhook.site/456eed1d-a188-407d-a0d7-38d820366234")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json; charset=UTF-8")

  setUp(generate()
    .inject((rampUsersPerSec(1).to(2) during (2 minutes)).randomized)
    .protocols(httpProtocol)
  )
}

package me.tungexplorer

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object GatlingRunnerSample extends App {
  val simulationClass: String = "me.tungexplorer.SampleSimulation"

  val props: GatlingPropertiesBuilder = new GatlingPropertiesBuilder
  props.resourcesDirectory("src/main/scala")
  props.binariesDirectory("target/scala/classes")
  props.resultsDirectory("/tmp/gatling-result")
  props.simulationClass(simulationClass)

  Gatling.fromMap(props.build)
}

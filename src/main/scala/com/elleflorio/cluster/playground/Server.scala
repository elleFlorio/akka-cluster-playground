package com.elleflorio.cluster.playground

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.elleflorio.cluster.playground.api.NodeRoutes
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Server extends App with NodeRoutes {

  implicit val system: ActorSystem = ActorSystem("cluster-playground")

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val nodeId = UUID.randomUUID().toString
  val node: ActorRef = system.actorOf(ClusterManager.props(nodeId))

  val config: Config = ConfigFactory.load()
  val address = config.getString("http.ip")
  val port = config.getInt("http.port")

  lazy val routes: Route = healthRoute ~ statusRoutes

  Http().bindAndHandle(routes, address, port)
  println(s"Node $nodeId is listening at http://$address:$port")

  Await.result(system.whenTerminated, Duration.Inf)

}

package com.elleflorio.cluster.playground.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.elleflorio.cluster.playground.ClusterManager.GetMembers
import scala.concurrent.ExecutionContext.Implicits.global
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.concurrent.duration._

trait NodeRoutes extends SprayJsonSupport {

  implicit def system: ActorSystem

  def node: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val healthRoute: Route = pathPrefix("health") {
    concat(
      pathEnd {
        concat(
          get {
            complete(StatusCodes.OK)
          }
        )
      }
    )
  }

  lazy val statusRoutes: Route = pathPrefix("status") {
    concat(
      pathPrefix("members") {
        concat(
          pathEnd {
            concat(
              get {
                val membersFuture: Future[List[String]] = (node ? GetMembers).mapTo[List[String]]
                onSuccess(membersFuture) { members =>
                  complete(StatusCodes.OK, members)
                }
              }
            )
          }
        )
      }
    )
  }
}

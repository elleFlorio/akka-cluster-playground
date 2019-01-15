package com.elleflorio.cluster.playground

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.{Cluster, MemberStatus}
import com.elleflorio.cluster.playground.ClusterManager.GetMembers
import com.elleflorio.cluster.playground.Server.system

object ClusterManager {

  sealed trait ClusterMessage
  case object GetMembers extends ClusterMessage

  def props(nodeId: String) = Props(new ClusterManager(nodeId))
}

class ClusterManager(nodeId: String) extends Actor with ActorLogging {

  val cluster = Cluster(context.system)
  val listener = system.actorOf(ClusterListener.props(cluster))

  override def receive: Receive = {
    case GetMembers => {
      sender() ! cluster.state.members.filter(_.status == MemberStatus.up)
        .map(_.address.toString)
        .toList
    }
  }
}

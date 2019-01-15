package com.elleflorio.cluster.playground

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

object ClusterListener {
  def props(cluster: Cluster) = Props(new ClusterListener(cluster))
}

class ClusterListener(cluster: Cluster) extends Actor with ActorLogging{

  // subscribe to cluster changes, re-subscribe when restart
  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) ⇒
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) ⇒
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) ⇒
      log.info(
        "Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent ⇒ // ignore
  }
}

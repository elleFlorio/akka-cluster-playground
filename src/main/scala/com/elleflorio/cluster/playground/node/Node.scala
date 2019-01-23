package com.elleflorio.cluster.playground.node

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.FromConfig
import com.elleflorio.cluster.playground.node.Node.{GetClusterMembers, GetFibonacci}
import com.elleflorio.cluster.playground.node.cluster.ClusterManager
import com.elleflorio.cluster.playground.node.cluster.ClusterManager.GetMembers
import com.elleflorio.cluster.playground.node.processor.Processor
import com.elleflorio.cluster.playground.node.processor.Processor.ComputeFibonacci

object Node {

  sealed trait NodeMessage

  case class GetFibonacci(n: Int)

  case object GetClusterMembers

  def props(nodeId: String) = Props(new Node(nodeId))
}

class Node(nodeId: String) extends Actor {

  val processor: ActorRef = context.actorOf(Processor.props(nodeId), "processor")
  val processorRouter: ActorRef = context.actorOf(FromConfig.props(Props.empty), "processorRouter")
  val clusterManager: ActorRef = context.actorOf(ClusterManager.props(nodeId), "clusterManager")

  override def receive: Receive = {
    case GetClusterMembers => clusterManager forward GetMembers
    case GetFibonacci(value) => processorRouter forward ComputeFibonacci(value)
  }
}

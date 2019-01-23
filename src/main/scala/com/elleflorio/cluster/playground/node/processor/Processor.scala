package com.elleflorio.cluster.playground.node.processor

import akka.actor.{Actor, ActorRef, Props}
import com.elleflorio.cluster.playground.node.processor.ProcessorFibonacci.Compute

object Processor {

  sealed trait ProcessorMessage

  case class ComputeFibonacci(n: Int) extends ProcessorMessage

  def props(nodeId: String) = Props(new Processor(nodeId))
}

class Processor(nodeId: String) extends Actor {
  import Processor._

  val fibonacciProcessor: ActorRef = context.actorOf(ProcessorFibonacci.props(nodeId), "fibonacci")

  override def receive: Receive = {
    case ComputeFibonacci(value) => {
      val replyTo = sender()
      fibonacciProcessor ! Compute(value, replyTo)
    }
  }
}

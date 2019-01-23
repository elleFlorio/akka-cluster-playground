package com.elleflorio.cluster.playground.node.processor

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol


case class ProcessorResponse(nodeId: String, result: BigInt)

object ProcessorResponseJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val processorResponse = jsonFormat2(ProcessorResponse)
}

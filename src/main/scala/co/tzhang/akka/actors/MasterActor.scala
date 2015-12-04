package co.tzhang.akka.actors

import akka.routing.RoundRobinPool
import akka.actor.{Props, Actor}
import co.tzhang.akka.{ReduceData, MapData, Result}

/**
 * Created by TZhang on 5/12/15.
 */
class MasterActor extends Actor {
  val mapActor = context.actorOf(Props[MapActor].withRouter(RoundRobinPool(nrOfInstances = 5)), name = "map")
  val reduceActor = context.actorOf(Props[ReduceActor].withRouter(RoundRobinPool(nrOfInstances = 5)), name = "reduce")
  val aggregateActor = context.actorOf(Props[AggregateActor], name = "aggregate")

  def receive: Receive = {
    case line: String => mapActor ! line
    case mapData: MapData => reduceActor ! mapData
    case reduceData: ReduceData => aggregateActor ! reduceData
    case Result => aggregateActor forward Result
    case _ => println("that was unexpected")
  }

}

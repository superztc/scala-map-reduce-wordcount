package co.tzhang.akka.actors

import co.tzhang.akka.ReduceData

import scala.collection.immutable.Map
import scala.collection.mutable.HashMap
import akka.actor.Actor
import co.tzhang.akka.Result

/**
 * Created by TZhang on 5/12/15.
 */
class AggregateActor extends Actor{
  val finalReduceMap = new HashMap[String, Int]
  def receive: Receive = {
    case ReduceData(reduceDataMap) => aggregateInMemoryReduce(reduceDataMap);
    case Result => sender ! finalReduceMap.toString()
  }

  def aggregateInMemoryReduce(reduceList: Map[String, Int]): Unit = {
    for ((key, value) <- reduceList) {
      if (finalReduceMap contains key)
        finalReduceMap(key) = (value + finalReduceMap.get(key).get)
      else
        finalReduceMap += (key -> value)
    }
  }
}

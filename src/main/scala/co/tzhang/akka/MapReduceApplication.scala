package co.tzhang.akka

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import co.tzhang.akka.actors.MasterActor
import akka.pattern.ask

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration

/**
* Created by TZhang on 5/12/15.
*/

sealed trait MapReduceMessage
case class WordCount(word: String, count: Int) extends MapReduceMessage
case class MapData(datalist: ArrayBuffer[WordCount]) extends MapReduceMessage
case class ReduceData(reduceDataMap: Map[String, Int]) extends MapReduceMessage
case object Result extends MapReduceMessage

object MapReduceApplication extends App {
  val _system = ActorSystem("MapReduceApp")
  val master = _system.actorOf(Props[MasterActor], name = "master")
  implicit val timeout = Timeout(FiniteDuration(5, TimeUnit.SECONDS))

  master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog", master)
  master.tell("Dog is man's best friend", master)
  master.tell("Dog and Fox belong to the same family", master)

  Thread.sleep(5000)

  val future = ask(master, Result).mapTo[String]

  val result = Await.result(future, timeout.duration)
  println(result)
  _system.shutdown()

}

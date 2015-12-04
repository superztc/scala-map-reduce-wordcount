package co.tzhang.akka.actors

import akka.actor.Actor
import co.tzhang.akka.{MapData, ReduceData, WordCount}

/**
 * Created by TZhang on 5/12/15.
 */
class ReduceActor extends Actor {
  def receive: Receive = {
    case MapData(dataList) => sender ! reduce(dataList);
  }

  def reduce (words: IndexedSeq[WordCount]): ReduceData = ReduceData {
    words.foldLeft(Map.empty[String, Int]) { (index, words) =>
      if (index contains words.word)
        index + (words.word -> (index.get(words.word).get + 1))
      else
        index + (words.word -> 1)
    }
  }

}

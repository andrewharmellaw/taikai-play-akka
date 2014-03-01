package actors

import org.scalatest.{BeforeAndAfterAll, Matchers, FunSuiteLike}
import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}
import scala.concurrent.duration._
import akka.util.Timeout

class MatchActorTest extends TestKit(ActorSystem("test")) with FunSuiteLike with Matchers
with BeforeAndAfterAll with ImplicitSender {

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  implicit val askTimeout = Timeout(15.seconds)


  test("red point should increment red points") {
    val actorRef = system.actorOf(Props[MatchActor])

    actorRef ! PointScored("red")
    expectMsg(MatchOngoing(1,0))
  }

  test("white point should increment white points") {
    val actorRef = system.actorOf(Props[MatchActor])

    actorRef ! PointScored("white")
    expectMsg(MatchOngoing(0,1))
  }

  test("red wins when he gets two points") {
    val actorRef = system.actorOf(Props[MatchActor])

    actorRef ! PointScored("red")
    expectMsg(MatchOngoing(1,0))

    actorRef ! PointScored("white")
    expectMsg(MatchOngoing(1,1))

    actorRef ! PointScored("red")
    expectMsg(MatchWon("red"))
  }

  test("white wins when he gets two points") {
    val actorRef = system.actorOf(Props[MatchActor])

    actorRef ! PointScored("white")
    expectMsg(MatchOngoing(0,1))

    actorRef ! PointScored("red")
    expectMsg(MatchOngoing(1,1))

    actorRef ! PointScored("white")
    expectMsg(MatchWon("white"))
  }
}



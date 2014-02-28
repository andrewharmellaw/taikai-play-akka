package actors

import akka.actor.Actor
import play.api.libs.json.{Json, Format}

class MatchActor extends Actor {

  var redpoints = 0
  var whitepoints = 0

  def receive = {
    case PointScored("red") =>
      redpoints += 1
      if (redpoints == 2)
        sender ! MatchWon("red")
      else
        sender ! MatchOngoing(redpoints, whitepoints)
    case PointScored("white") =>
      whitepoints += 1
      if (whitepoints == 2)
        sender ! MatchWon("white")
      else
        sender ! MatchOngoing(redpoints, whitepoints)
    case _ =>
      context.system.log.info("Unknown message")
  }
  
}

case class PointScored(player: String)

case class MatchOngoing(redpoints: Int, whitepoints: Int)

object MatchOngoing {
  implicit val format: Format[MatchOngoing] = Json.format[MatchOngoing]
}

case class MatchWon(player: String)

object MatchWon {
  implicit val format: Format[MatchWon] = Json.format[MatchWon]
}


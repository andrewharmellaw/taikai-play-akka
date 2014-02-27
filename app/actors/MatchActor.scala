package actors

import akka.actor.Actor
import play.api.libs.json.{Json, Format}

class MatchActor extends Actor {

  var redpoints = 0
  var whitepoints = 0

  def receive = {
    case PointScored(player) =>
      context.system.log.info("Point scored for " + player)
      if (player == "red")
        redpoints += 1
      else if (player == "white")
        whitepoints += 1
      sender ! MatchOngoing(redpoints,whitepoints)
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


package controllers

import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.mvc.{Action, Controller}
import akka.actor.{ActorRef, Props}
import scala.concurrent.Future
import akka.pattern.ask
import akka.util.Timeout
import play.api._
import play.api.mvc._
import akka.pattern.ask
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import models._
import actors._

object Application extends Controller {
  implicit val timeout = Timeout(15.seconds)

  def index = Action {
    Ok(views.html.index("Hello Play Framework"))
  }

  def redpoint() = Action.async {
    val matchActorRef: ActorRef = Akka.system.actorOf(Props[MatchActor])

    val matchResults: Future[Any] = matchActorRef ? PointScored("red")
    val resultFuture: Future[SimpleResult] = matchResults.map {
      case ongoing: MatchOngoing =>
        Ok(Json.toJson(ongoing))
      case won: MatchWon =>
        Ok(Json.toJson(won))
      case _ =>
        InternalServerError("Unknown Error")
    } recover {
      case e: Exception =>
        InternalServerError(e.toString)
    }
    resultFuture
  }

  def whitepoint() = Action.async {
    val matchActorRef: ActorRef = Akka.system.actorOf(Props[MatchActor])

    val matchResults: Future[Any] = matchActorRef ? PointScored("white")
    val resultFuture: Future[SimpleResult] = matchResults.map {
      case ongoing: MatchOngoing =>
        Ok(Json.toJson(ongoing))
      case won: MatchWon =>
        Ok(Json.toJson(won))
      case _ =>
        InternalServerError("Unknown Error")
    } recover {
      case e: Exception =>
        InternalServerError(e.toString)
    }
    resultFuture
  }

//  def doSearch(query: String, location: String) = Action.async {
//    val beerSearchActorRef: ActorRef = Akka.system.actorOf(Props[BeerSearchActor])
//
//    val searchResults: Future[Any] = beerSearchActorRef ? DoSearch(query, location)
//    val resultFuture: Future[SimpleResult] = searchResults.map {
//      case searchResults: PubSeq =>
//        Ok(Json.toJson(searchResults))
//      case _ =>
//        InternalServerError("Unknown Error")
//    } recover {
//      case e: Exception =>
//        InternalServerError(e.toString)
//    }
//    resultFuture
//  }
}
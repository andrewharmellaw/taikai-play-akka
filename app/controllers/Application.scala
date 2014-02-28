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

  val matchActorRef: ActorRef = Akka.system.actorOf(Props[MatchActor])

  def index = Action {
    Ok(views.html.index("Hello Play Framework"))
  }

  def formatResponse(results: Future[Any]) = results.map {
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

  def redpoint() = Action.async {
    val matchResults: Future[Any] = matchActorRef ? PointScored("red")
    formatResponse(matchResults)
  }

  def whitepoint() = Action.async {
    val matchResults: Future[Any] = matchActorRef ? PointScored("white")
    formatResponse(matchResults)
  }

}
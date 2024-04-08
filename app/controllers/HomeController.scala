package controllers

import scala.concurrent.duration.DurationInt
import javax.inject._
import play.api._
import play.api.mvc._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.pattern.ask
import org.slf4j.MDC

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()
  (val controllerComponents: ControllerComponents,
       system:               ActorSystem) extends BaseController {

  val logger: Logger = Logger(this.getClass.getCanonicalName)

  implicit def ec: ExecutionContext = controllerComponents.executionContext

  val testActor: akka.actor.ActorRef = system.actorOf(TestActor.props())


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }


  def test(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    logger.info(s"ActorSystem: $system with Dispatchers ${system.dispatcher}")

    MDC.put("requestId", request.id.toString)
    MDC.put("path", request.path)
    MDC.put("method", request.method)
    MDC.put("clientAddress", request.remoteAddress)
    logger.info(s"MDC now: ${MDC.getCopyOfContextMap}")

    def future(input: String): Future[String] = {
      implicit val timeout: akka.util.Timeout = 2.seconds
      (testActor ? input).mapTo[String]
    }

    val message = "Hello whoever you are"

    future(message).map { resp =>
      logger.info(s"and ... MDC is: ${MDC.getCopyOfContextMap}")
      val response = resp + " --> " + request.connection.toString
      Ok(response)
    }
  }


  private class TestActor() extends Actor {
    logger.info("init TestActor")

    override def receive: Receive = {
      case msg: String =>
        MDC.put("msg", msg)
        logger.info(s"got msg '$msg' mdc.get is: ${MDC.get("requestId")}")
        logger.info(s"got msg '$msg' mdc.getCopyOfContextMap is: ${MDC.getCopyOfContextMap}")
        sender() ! msg
    }
  }

  object TestActor {
    def props(): akka.actor.Props = akka.actor.Props {
      new TestActor()
    }
  }

}

package irm

import scala.io.StdIn
import scala.concurrent.*

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport

import no.vedaadata.html.*

object IrmProto extends ScalaXmlSupport:

  given system: ActorSystem = ActorSystem()
  given ExecutionContext = system.dispatcher

  val config = ConfigFactory.load()
  val logger = Logging(system, "AkkaHttpMicroservice")

  val systemForm = productFormElement[System]
  val informationGroupForm = productFormElement[InformationGroup]

  val selectPrompt = Some("Please select...")

  val connectionForm = 
    given FormElement[ConnectionType] = new SelectFormElement(ConnectionType.values, selectPrompt)(_.toString)(_.toString)
    given FormElement[System] = new SelectFormElement(Store.systems, selectPrompt)(_.label)(_.label)
    given FormElement[InformationGroup] = new SelectFormElement(Store.informationGroups, selectPrompt)(_.label)(_.label)
    productFormElement[Connection]

  val routes: Route =
    path("") {
      get {
        complete {
          html {
            <div>
              <div class="form-group mb-3">
                <a class="btn btn-primary" href="/addSystem">Add system</a>
                <a class="btn btn-primary" href="/addInformationGroup">Add information group</a>
                <a class="btn btn-primary" href="/addConnection">Add connection</a>
              </div>
              <div class="mb-3">
                { matrix(Store.systems.toList, Store.informationGroups.toList, Store.connections.toList) }
              </div>
              <form method="post" class="form-group mb-3">
                <button type="submit" class="btn btn-secondary" name="clearSystems">Clear systems</button>
                <button type="submit" class="btn btn-secondary" name="clearInformationGroups">Clear information groups</button>
                <button type="submit" class="btn btn-secondary" name="clearConnections">Clear connections</button>
                <button type="submit" class="btn btn-secondary" name="clear">Clear all</button>
                <button type="submit" class="btn btn-secondary" name="reset">Reset to sample data</button>
              </form>
            </div>
          }
        }
      } ~
      post {
        formFieldMultiMap { params =>
          if params.contains("clearSystems") then Store.clearSystems()
          else if params.contains("clearInformationGroups") then Store.clearInformationGroups()
          else if params.contains("clearConnections") then Store.clearConnections()
          else if params.contains("clear") then Store.clear()
          else if params.contains("reset") then Store.reset()
          redirect("/", StatusCodes.SeeOther)
        }
      }
    } ~
    path("addSystem") {
      get {
        complete {
          html {
            form("Add system", systemForm)
          }
        }
      } ~ 
      post {
        formFieldMultiMap { params =>
          systemForm.fromParameters("System", params) foreach { system =>
            Store.systems += system
          }
          if params.contains("another") then redirect("/addSystem", StatusCodes.SeeOther)
          else redirect("/", StatusCodes.SeeOther)
        }
      }
    } ~
    path("addInformationGroup") {
      get {
        complete {
          html {
            form("Add information group", informationGroupForm)
          }
        }
      } ~ 
      post {
        formFieldMultiMap { params =>
          informationGroupForm.fromParameters("", params) foreach { informationGroup =>
            Store.informationGroups += informationGroup
          }
          if params.contains("another") then redirect("/addInformationGroup", StatusCodes.SeeOther)
          else redirect("/", StatusCodes.SeeOther)
        }
      }
    } ~
    path("addConnection") {
      get {
        complete {
          html {
            form("Add connection", connectionForm)
          }
        }
      } ~ 
      post {
        formFieldMultiMap { params =>
          connectionForm.fromParameters("Connection", params) foreach { connection =>
            Store.connections += connection
          }
          if params.contains("another") then redirect("/addConnection", StatusCodes.SeeOther)
          else redirect("/", StatusCodes.SeeOther)
        }
      }
    } ~
    path("matrix.css") {
      getFromResource("matrix.css")
    }

  def form(title: String, formElement: FormElement[?]) = 
    <form method="post">
      <legend>{ title }</legend>
      { formElement.render("") }
      <button type="submit" class="btn btn-success" >Save</button>
      <button type="submit" class="btn btn-success" name="another">Save and add another</button>
    </form>


  def html(content: xml.Elem) =
    <html>
      <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous"/>
        <link rel="stylesheet" href="matrix.css" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
      </head>
      <body>
        <div class="container">
          <h1>IRM proto tool</h1>
          { content }
        </div>
      </body>
    </html>

  def main(args: Array[String]): Unit =
    val serverBuilder = Http().newServerAt(config.getString("http.interface"), config.getInt("http.port")).bindFlow(routes)
    StdIn.readLine()
    // Unbind from the port and shut down when done
    serverBuilder
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())


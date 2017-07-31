import akka.actor.{ActorSystem, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}

/**
  * Created by archana on 7/31/2017.
  */
case class PersistenceEvent(data: String)

case class StateHolder(events: List[String] = Nil) {
  def updatedState(evt: PersistenceEvent): StateHolder = copy(evt.data :: events)

  def eventSize: Int = events.length

  override def toString: String = events.reverse.toString
}

class AkkaPersistenceTrial extends PersistentActor {
  var state = StateHolder()

  /**
    * should not perform actions which may fail here
    */
  override def receiveRecover: Receive = {
    case cmd: PersistenceEvent                   => {
      state = state.updatedState(cmd)
    }
    case SnapshotOffer(_, snapshot: StateHolder) => state = snapshot
  }

  override def receiveCommand: Receive = {
    case PersistenceEvent(data) => persist(PersistenceEvent(s"$data-${state.eventSize}")) {
      e => state = state.updatedState(e)
    }
    case "snap" =>saveSnapshot(state)
    case "print"                => println(state)
  }

  override def persistenceId: String = "sample-persistence-id-1"
}

object PersistenceExample extends App{
  val system = ActorSystem("persistence-example-akka")
  val persistentActor = system.actorOf(Props[AkkaPersistenceTrial], "persistentActor-scala")
  persistentActor ! "print"
  persistentActor ! PersistenceEvent("first")
  persistentActor ! PersistenceEvent("second")
  persistentActor ! PersistenceEvent("third")
  persistentActor ! "snap"
  persistentActor ! "print"
  persistentActor ! PersistenceEvent("fourth")
  persistentActor ! "snap"
  persistentActor ! "print"

  Thread.sleep(10000)
  system.terminate()
}

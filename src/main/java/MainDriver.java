import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

public class MainDriver {
    public static void main(String[] args) {
        //Send to BoundedQueue
//        ActorSystem<BoundedActor.COMMAND> system = ActorSystem.create(BoundedActor.create(10, 0), "BoundedQueue");
//
//        system.tell(new BoundedActor.Insert(new Item()));
//        system.tell(new BoundedActor.Insert(new Item()));

       // MAIN PC SYSTEM
//         ActorSystem<MainActor.START> mySystem = ActorSystem.create(MainActor.create(5, 2, 10), "Main-Producer");
//         mySystem.tell(MainActor.START.INSTANCE);

        //MAIN SIEVE
        SieveActor.runSieveActor(1_000);



//        ActorSystem<BoundedActor.Request> producerActor1 = ActorSystem.create(ProducerActor.create(10), "ProducerActor1");
//
//        ActorRef<BoundedActor.Request> producerActor2 = ActorSystem.create(ProducerActor.create(10), "ProducerActor2");
//        ActorRef<BoundedActor.Request> producerActor3 = ActorSystem.create(ProducerActor.create(10), "ProducerActor3");
//
//        ActorRef<BoundedActor.COMMAND> boundedActorRef = ActorSystem.create(BoundedActor.create(10, 30), "BoundedQueue1");
//
//        producerActor1.tell(new BoundedActor.Request(boundedActorRef));
//        producerActor2.tell(new BoundedActor.Request(boundedActorRef));
//        producerActor3.tell(new BoundedActor.Request(boundedActorRef));
//
//        ActorRef<BoundedActor.Remove> consumeActorRef = ActorSystem.create(ConsumerActor.create(), "ConsumerActor1");
//        ActorRef<BoundedActor.Remove> consumeActorRef2 = ActorSystem.create(ConsumerActor.create(), "ConsumerActor2");

//        for(int i =0; i < 15; i++) {
//            boundedActorRef.tell(new BoundedActor.Ready(consumeActorRef));
//            boundedActorRef.tell(new BoundedActor.Ready(consumeActorRef2));
//        }

//        insertActorRef.tell(new BoundedActor.Ready(insertActorRef));
//        ActorSystem<BoundedActor.Remove> actorRef = ActorSystem.create(ConsumerActor.create(), "ConsumeActor");
//        system2.tell(new BoundedActor.Ready(actorRef));

        //terminate

    }
}

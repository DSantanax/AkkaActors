import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Receive;

public class MainActor extends AbstractBehavior<MainActor.START> {

        public enum START{
            INSTANCE
        }
        //add more parameters here
        //spawn Producers, Consumers & Bounded Queue
        private MainActor(ActorContext<START> context, int totalProducers, int totalConsumers, int produce){
            super(context);
            this.totalProducers = totalProducers;
            this.totalConsumers = totalConsumers;
            this.produce = produce;
            this.boundedActorRef = context.spawn(BoundedActor.create(10, totalProducers*10), "BoundedQueue");
        }

        public static Behavior<START> create(int totalProducers, int totalConsumers, int produce){
            return Behaviors.setup(context -> new MainActor(context, totalProducers, totalConsumers, produce));
        }
        private final ActorRef<BoundedActor.COMMAND> boundedActorRef;
        private final int totalProducers;
        private final int totalConsumers;
        private final int produce;

        @Override
        public Receive<START> createReceive() {
            return newReceiveBuilder().onMessageEquals(START.INSTANCE, this::createPCSystem).build();
        }

        private Behavior<START> createPCSystem() {

            for(int i = 0; i < totalProducers; i++) {
                ActorRef<BoundedActor.Request> replyTo
                        = getContext().spawn(ProducerActor.create(produce), "Producer" + i);
                replyTo.tell(new BoundedActor.Request(boundedActorRef));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < totalConsumers; i++){
                ActorRef<BoundedActor.Remove> replyTo =
                        getContext().spawn(ConsumerActor.create(), "Consumer" + i);
                boundedActorRef.tell(new BoundedActor.Ready(replyTo));
            }
            return this;
        }
}
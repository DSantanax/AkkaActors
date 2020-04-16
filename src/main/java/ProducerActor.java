import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ActorContext;


public class ProducerActor extends AbstractBehavior<BoundedActor.Request> {

    private int totalProduction;

    private ProducerActor(ActorContext<BoundedActor.Request> context, int totalProduction) {
        super(context);
        this.totalProduction = totalProduction;
    }

    public static Behavior<BoundedActor.Request> create(int totalProduction){
        return Behaviors.setup(context -> new ProducerActor(context, totalProduction));
    }

    @Override
    public Receive<BoundedActor.Request> createReceive() {
        return newReceiveBuilder().onMessage(BoundedActor.Request.class, this::insertItem).build();
    }

    private Behavior<BoundedActor.Request> insertItem(BoundedActor.Request command) {
        for (int i = 0; i < totalProduction; i++) {
            Item item = new Item();
            getContext().getLog().info("Sending item: {} ", item);
            command.replyTo.tell(new BoundedActor.Insert(item));
        }
        return this;
    }

}

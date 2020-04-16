import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ActorContext;

public class ConsumerActor extends AbstractBehavior<BoundedActor.Remove> {


    //private constructor for factory method
    private ConsumerActor(ActorContext<BoundedActor.Remove> context) {
        super(context);
    }
    //static factory to initialize the private constructor
    public static Behavior<BoundedActor.Remove> create(){
        return Behaviors.setup(ConsumerActor::new);
    }

    @Override
    public Receive<BoundedActor.Remove> createReceive() {
        return newReceiveBuilder().onMessage(BoundedActor.Remove.class, this::takeItem).build();
    }

    //remove
    private Behavior<BoundedActor.Remove> takeItem(BoundedActor.Remove command) {
            getContext().getLog().info("Removed Item: {}", command.getItem());
        return this;
    }
}

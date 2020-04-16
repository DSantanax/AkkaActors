import akka.actor.typed.javadsl.*;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;

import java.util.LinkedList;
import java.util.Queue;


public class BoundedActor extends AbstractBehavior<BoundedActor.COMMAND> {
    private Queue<Item> itemQueue;
    private final int maxQueue;
    private final int totalProduced;

    //private constructor
    private BoundedActor(ActorContext<COMMAND> context, int maxQueue, int totalProduced) {
        super(context);
        this.maxQueue = maxQueue;
        this.totalProduced = totalProduced;
        itemQueue = new LinkedList<>();
    }

    //factory method for private constructor
    public static Behavior<COMMAND> create(int maxQueue, int totalProduced) {
        return Behaviors.setup(context -> new BoundedActor(context, maxQueue, totalProduced));
    }

    @Override
    public Receive<COMMAND> createReceive() {
        ReceiveBuilder<BoundedActor.COMMAND> builder = newReceiveBuilder();
        return builder
               .onMessage(Insert.class, this::itemReceive)
                .onMessage(Ready.class, this::removeItem)
                .build();
    }

    private Behavior<COMMAND> itemReceive(Insert command) {
        itemQueue.add(command.getItem());
        //print info
        getContext().getLog().info("Added item to queue:  {}", itemQueue.size());
        getContext().getLog().info("Max Size: {}", maxQueue);
        return this;
    }

    private Behavior<COMMAND> removeItem(Ready command) {
        for (int i = 0; i < totalProduced/2; i++) {
            command.replyTo.tell(new BoundedActor.Remove(itemQueue.remove()));
            getContext().getLog().info("Sending To Consumer! Queue Size: {}", itemQueue.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    //type of messages the actor(s) can receive

    interface COMMAND {}

    public static final class Request implements COMMAND {
        public final ActorRef<COMMAND> replyTo;

        public Request(ActorRef<COMMAND> replyTo) {
            this.replyTo = replyTo;
        }
    }

    public static class Insert implements COMMAND{
        private final Item item;
        public Insert(Item item) {
            this.item = item;
        }
        public Item getItem(){
            return item;
        }
    }

    public static final class Ready implements COMMAND {

        public final ActorRef<Remove> replyTo;

        public Ready(ActorRef<Remove> replyTo) {
            this.replyTo = replyTo;
        }
    }

    public static final class Remove implements COMMAND {
        public final Item item;

        public Remove(Item item) {
            this.item = item;
        }
        public Item getItem(){
            return item;
        }
    }
}
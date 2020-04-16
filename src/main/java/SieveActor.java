import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.IOException;

public class SieveActor extends AbstractBehavior<SieveActor.Commands> {
    private ActorRef<SieveActor.Commands> nextActorPrime;
    private final int primeNumber;

    private SieveActor(ActorContext<Commands> context, int primeNumber) {
        super(context);
        this.primeNumber = primeNumber;
        getContext().getLog().info("Prime Number: {}", primeNumber);
    }

    public static Behavior<Commands> create(int primeNumber) {
        return Behaviors.setup(context -> new SieveActor(context, primeNumber));
    }

    @Override
    public Receive<Commands> createReceive() {
        return newReceiveBuilder().onMessage(Process.class, this::process).build();
    }

    private Behavior<Commands> process(Process command) {
        if ((command.number % primeNumber) != 0) {
            if (nextActorPrime == null) {
                nextActorPrime = getContext().spawn(SieveActor.create(command.number), "prime-actor-" + command.number);
            } else {
                nextActorPrime.tell(new Process(command.number));
            }
        }
        return this;
    }

    public static void runSieveActor(int primeNumber) {
        final ActorSystem<SieveActor.Commands> actorSystem = ActorSystem.create(SieveActor.create(2), "prime-actor");
        for (int i = 3; i < primeNumber; i++) {
            actorSystem.tell(new SieveActor.Process(i));
        }
        try {
            System.out.println("Enter to exit");
            System.in.read();
        } catch(IOException ignored) {
        }finally {
            actorSystem.terminate();
        }
    }
    //Interface for the commands we can receive
    interface Commands {}

    //Messages to receive
    public static class Process implements Commands {
        public final int number;

        public Process(int prime) {
            this.number = prime;
        }
    }
}

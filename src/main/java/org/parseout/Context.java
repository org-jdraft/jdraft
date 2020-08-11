package org.parseout;

import java.util.ArrayDeque;

public interface Context {

    /** The initial context that marks the beginning context of parsing (acts like a tombstone i.e. cant be popped from the queue)*/
    public static final NamedContext INITIAL = new NamedContext("INITIAL");

    /** A queue for pushing "contexts" (i.e. MethodBody) on and later evaluating tokens to terms in the outline */
    class ContextQueue {
        ArrayDeque<Context> ctxQueue = new ArrayDeque<>();

        public ContextQueue() {
            ctxQueue.push(Context.INITIAL);
        }

        public void push(Context ctx) {
            ctxQueue.push(ctx);
        }

        public Context[] array() {
            return ctxQueue.toArray(new Context[0]);
        }

        public Context get() {
            return ctxQueue.peek();
        }

        public Context pop() {
            return ctxQueue.pop();
        }

        public boolean isEmpty(){
            return ctxQueue.peek() == INITIAL;
        }
    }

    class NamedContext implements Context {
        final String name;

        public NamedContext(String name) {
            this.name = name;
        }

        public String toString() {
            return "Context("+name+")";
        }
    }
}

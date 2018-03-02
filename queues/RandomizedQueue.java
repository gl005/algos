import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] elements;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        elements = (Item[]) new Object[2];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (n == 0);
    }
    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new UnsupportedOperationException();
        }
        if (n == elements.length) {
            resize(elements.length*2);
        }
        // insert the new item at a random position
        int idx = n == 0 ? 0 : StdRandom.uniform(n);
        Item oldElement = elements[idx];

        elements[n] = oldElement;
        elements[idx] = item;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) {
            throw new NoSuchElementException();
        }

        Item item = elements[n-1];
        elements[n-1] = null;
        n--;

        if (n <= (elements.length/4)) {
            resize(elements.length/2);
        }
        return item;
    }

    public Item sample() {
        if (n == 0) {
            throw new NoSuchElementException();
        }
        return elements[StdRandom.uniform(n)];
    }


    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            if (elements[i] != null) {
                temp[i] = elements[i];
            }
        }
        elements = temp;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("the");
        rq.enqueue("first");
        rq.enqueue("word");
        rq.enqueue("but");
        rq.enqueue("not");
        rq.enqueue("the");
        rq.enqueue("last");
        rq.enqueue("OK");
        rq.enqueue("YEAH");
        System.out.println(rq.size());

        System.out.println("sample: "+ rq.sample());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());
        System.out.println("dequeue: "+rq.dequeue());

        rq.enqueue("HA");
        rq.enqueue("JA");
        rq.enqueue("DA");

        System.out.println(rq.size());
        for (String s : rq) {
            System.out.println(s);
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final Item[] iterElements;

        private int index;

        private RandomizedQueueIterator() {
            iterElements = elements.clone();
            StdRandom.shuffle(iterElements, 0, n);
            index = 0;
        }


        @Override
        public boolean hasNext() {
            return index < n;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iterElements[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
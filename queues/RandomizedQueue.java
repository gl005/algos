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
        n += 1;
        elements[n-1] = item;

        if (n > 1) {
            // switch the new item with a random position
            int idx = StdRandom.uniform(n);
            Item oldElement = elements[idx];
            Item oldLastElement = elements[n-1];

            elements[idx] = oldLastElement;
            elements[n-1] = oldElement;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) {
            throw new NoSuchElementException();
        }

        Item item = elements[n-1];
        elements[n-1] = null;
        n--;

        if (n > 0 && n == (elements.length/4)) {
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
        rq.enqueue("hello");
        rq.enqueue("foo");
        rq.enqueue("world");
        rq.enqueue("bar");
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
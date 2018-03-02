import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;

    private Node last;

    private int length = 0;

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size()  {
        return length;
    }

    public void addFirst(Item item)  {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node itemNode = new Node(item, first);
        first = itemNode;
        if (last == null) {
            last = first;
        }
        length++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node itemNode = new Node(item, null);
        if (last != null) {
            last.setNext(itemNode);
        }

        last = itemNode;
        if (first == null) {
            first = last;
        }
        length++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item firstItem = first.getValue();
        first = first.getNext();
        if (first == null) {
            last = null;
        }
        length--;
        return firstItem;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item lastItem = last.getValue();

        last = getBeforeLast();
        if (last == null) {
            first = null;
        }
        else {
            last.setNext(null);
        }
        length--;

        return lastItem;
    }

    private Node getBeforeLast() {
        Node current = first;
        Node lastOne = null;
        while (current.getNext() != null) {
            lastOne = current;
            current = current.getNext();
        }
        return lastOne;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator()  {
        return new DequeueIterator();
    }



    public static void main(String[] args)  {
        Deque<String> stringDeque = new Deque<>();
        stringDeque.addFirst("first");
        stringDeque.addFirst("the");
        stringDeque.addFirst("am");
        stringDeque.addFirst("I");
        stringDeque.addFirst("Yes");
        stringDeque.addLast("word");
        stringDeque.addLast("SIR");
        stringDeque.addLast("!");
        stringDeque.removeLast();
        stringDeque.removeFirst();

        StringBuilder sb = new StringBuilder();
        for (String s : stringDeque) {
            sb.append(" ").append(s);
        }
        System.out.println(sb.toString());
    }

    private class DequeueIterator implements Iterator<Item> {

        private Node current;

        public DequeueIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node item = current;
            current = item.getNext();
            return item.getValue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private final Item value;

        private Node next;

        public Node(Item value, Node next) {
            this.value = value;
            this.next = next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Item getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }
    }
}
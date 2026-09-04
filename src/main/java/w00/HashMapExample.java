package w00;

import java.util.Objects;

public class HashMapExample<Key, Value> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    private final float loadFactor;

    private Element<Key, Value>[] elements;
    private int size;
    private int threshold;

    public HashMapExample() {

        loadFactor = DEFAULT_LOAD_FACTOR;
        elements = new Element[DEFAULT_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_CAPACITY * loadFactor);
    }

    public int size() {

        return size;
    }

    public Value put(Key key, Value value) {

        int hash = getHash(key);
        int index = getIndex(hash, elements.length);

        var elementInIndex = elements[index];

        while (elementInIndex != null) {

            if (isKeyMatch(elementInIndex, key, hash)) {
                Value oldValue = elementInIndex.value;
                elementInIndex.value = value;
                return oldValue;
            }

            elementInIndex = elementInIndex.next;
        }

        elements[index] = new Element<Key, Value>(hash, key, value, elements[index]);
        size++;

        if (size >= threshold)
            resize();

        return null;
    }

    public Value get(Key key) {

        int hash = getHash(key);
        int index = getIndex(hash, elements.length);

        var elementInIndex = elements[index];

        while (elementInIndex != null) {

            if (isKeyMatch(elementInIndex, key, hash)) {

                return elementInIndex.value;
            }

            elementInIndex = elementInIndex.next;
        }

        return null;
    }

    public Value remove(Key key) {

        int hash = getHash(key);
        int index = getIndex(hash, elements.length);

        var elementInIndex = elements[index];
        Element<Key, Value> previousElement = null;

        while (elementInIndex != null) {

            if (isKeyMatch(elementInIndex, key, hash)) {

                if (previousElement == null) {

                    elements[index] = elementInIndex.next;
                } else {

                    previousElement.next = elementInIndex.next;
                }

                size--;
                return elementInIndex.value;
            }

            previousElement = elementInIndex;
            elementInIndex = elementInIndex.next;
        }

        return null;
    }

    private int getHash(Object key) {

        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndex(int hash, int length) {

        return Math.floorMod(hash, length);
    }

    private boolean isKeyMatch(Element<Key, Value> element, Key key, int hash) {

        return element.hash == hash && (Objects.equals(key, element.key));
    }

    private void resize() {

        var oldElements = elements;
        var newCapacity = oldElements.length * 2;
        var newElements = new Element[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        for (int i = 0; i < oldElements.length; i++) {

            var element = oldElements[i];

            if (element != null) {

                while (element != null) {

                    var next = element.next;
                    var newIndex = getIndex(element.hash, newCapacity);
                    element.next = newElements[newIndex];
                    newElements[newIndex] = element;

                    element = next;
                }
            }
        }

        elements = newElements;
    }

    private static class Element<Key, Value> {

        final int hash;
        final Key key;
        Value value;
        Element<Key, Value> next;

        public Element(int hash, Key key, Value value, Element<Key, Value> next) {

            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

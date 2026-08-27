package w00;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class HashMapExampleTest {

    private HashMapExample<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new HashMapExample<>();
    }

    @Test
    void testPutAndGet() {

        String key = "one";
        Integer value = 1;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }

    @Test
    void testPutNullKey() {

        Integer value = 42;
        map.put(null, value);
        assertEquals(value, map.get(null));
    }

    @Test
    void testPutNullValue() {

        String key = "nullValue";
        map.put(key, null);
        assertNull(map.get(key));
    }

    @Test
    void testUpdateExistingKey() {

        String key = "test";
        map.put(key, 10);
        map.put(key, 20);
        assertEquals(20, map.get(key));
    }

    @Test
    void testGetNonExistingKey() {

        String key = "notExists";
        Integer result = map.get(key);
        assertNull(result);
    }

    @Test
    void testRemoveExistingKey() {

        String key = "removeMe";
        Integer value = 100;
        map.put(key, value);
        Integer removedValue = map.remove(key);
        assertEquals(value, removedValue);
        assertNull(map.get(key));
    }

    @Test
    void testRemoveNonExistingKey() {

        Integer result = map.remove("nonExists");
        assertNull(result);
    }

    @Test
    void testRemoveNullKey() {

        Integer value = 99;
        map.put(null, value);
        Integer removedValue = map.remove(null);
        assertEquals(value, removedValue);
        assertNull(map.get(null));
    }

    @Test
    void testMultiplePutsAndGets() {

        String[] keys = {"a", "b", "c", "d", "e"};
        Integer[] values = {1, 2, 3, 4, 5};

        for (int i = 0; i < keys.length; i++) {

            map.put(keys[i], values[i]);
        }

        for (int i = 0; i < keys.length; i++) {

            assertEquals(values[i], map.get(keys[i]));
        }
    }

    @Test
    void testCollisionHandling() {

        var customMap = new HashMapExample<CustomKey, String>();
        var key1 = new CustomKey(1, "first");
        var key2 = new CustomKey(1, "second");

        customMap.put(key1, "value1");
        customMap.put(key2, "value2");

        assertEquals("value1", customMap.get(key1));
        assertEquals("value2", customMap.get(key2));
    }

    @Test
    void testResize() {

        for (int i = 0; i < 100; i++)
            map.put("key" + i, i);

        for (int i = 0; i < 100; i++)
            assertEquals(Integer.valueOf(i), map.get("key" + i));
    }

    @Test
    void testSizeAfterOperations() {

        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        assertEquals(3, map.size());

        map.remove("b");
        assertEquals(2, map.size());

        map.put("b", 2);
        assertEquals(3, map.size());
    }

    static class CustomKey {

        private final int id;
        private final String name;

        CustomKey(int id, String name) {

            this.id = id;
            this.name = name;
        }

        @Override
        public int hashCode() {

            return id;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;

            CustomKey that = (CustomKey) obj;

            return id == that.id && Objects.equals(name, that.name);
        }
    }
}

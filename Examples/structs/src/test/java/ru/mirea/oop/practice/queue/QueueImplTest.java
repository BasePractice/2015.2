package ru.mirea.oop.practice.queue;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class QueueImplTest {

    private IQueue<Integer> queue;

    @Before
    public void setUp() throws Exception {
        queue = new QueueImpl<>();
        queue.push(0);
        queue.push(1);
        queue.push(2);
        queue.push(3);
        queue.push(4);
        queue.push(5);
        queue.push(6);
        queue.push(7);
        queue.push(8);
        queue.push(9);
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(queue.size(), 10);
    }

    @Test
    public void testPush() throws Exception {
        queue.push(10);
        assertEquals(queue.size(), 11);
    }

    @Test
    public void testPop() throws Exception {
        Integer result = queue.pop();
        assertEquals(queue.size(), 9);
        assertEquals(result.intValue(), 0);
    }

    @Test
    public void testPeek() throws Exception {
        Integer result = queue.peek();
        assertEquals(queue.size(), 10);
        assertEquals(result.intValue(), 0);
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testClear() throws Exception {
        queue.clear();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testIterator() throws Exception {
        /** Так делать нельзя. Итератор должен быть блокируемым.
         * Это все работает только из-за того, что бы не очищаем
         * элементы в методе pop*/
        Iterator<Integer> it = queue.iterator();
        assertNotNull(it);
        for (int  i = 0; it.hasNext(); ++i) {
            assertEquals(queue.pop().intValue(), i);
        }
        assertTrue(queue.isEmpty());
    }
}
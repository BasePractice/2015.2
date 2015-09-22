package ru.mirea.oop.practice.lists;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class LinkedListImplTest extends DefaultListTest {

    @Test
    public void testSize() throws Exception {
        assertEquals(list.size(), 10);
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertFalse(list.isEmpty());
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(list.contains(0));
    }

    @Test
    public void testAdd() throws Exception {
        list.add(10);
        assertEquals(list.size(), 11);
        assertTrue(list.contains(10));
    }

    @Test
    public void testRemove() throws Exception {
        list.remove(0);
        assertEquals(list.size(), 9);
        assertFalse(list.contains(0));
    }

    @Test
    public void testClear() throws Exception {
        list.clear();
        assertEquals(list.size(), 0);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<Integer> it = list.iterator();
        int index = 0;
        assertNotNull(it);
        while (it.hasNext()) {
            it.next();
            ++index;
        }
        assertEquals(index, list.size());
    }
}
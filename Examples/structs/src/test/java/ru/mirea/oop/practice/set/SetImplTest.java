package ru.mirea.oop.practice.set;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SetImplTest {
    private static final int SIZE = 10;
    private ISet<Integer> set;

    @Before
    public void setUp() throws Exception {
        set = new SetImpl<>();
        for (int i = 0; i < SIZE; ++i)
            set.put(i);
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(set.size(), SIZE);
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertFalse(set.isEmpty());
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(set.contains(0));
    }

    @Test
    public void testPut() throws Exception {
        set.put(SIZE + 10);
        assertEquals(set.size(), SIZE + 1);
    }

    @Test
    public void testRemove() throws Exception {
        set.remove(0);
        assertEquals(set.size(), SIZE - 1);
    }

    @Test
    public void testClear() throws Exception {
        set.clear();
        assertTrue(set.isEmpty());
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<Integer> it = set.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            System.out.println(next);
        }
    }
}
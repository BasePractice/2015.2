package ru.mirea.oop.practice.lists;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlgorithmsTest extends DefaultListTest {

    @Test
    public void testSort() throws Exception {

    }

    @Test
    public void testFoldl() throws Exception {
        int result = 0;
        result = Algorithms.foldl(list, result, new Algorithms.LinkedListFold<Integer, Integer>() {
            @Override
            public Integer execute(Integer accum, Integer element) {
                return accum + element;
            }
        });
        assertEquals(result, 45);
    }
}
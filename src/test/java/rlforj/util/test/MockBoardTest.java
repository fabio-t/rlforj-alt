/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.util.test;

import junit.framework.TestCase;

/**
 * Test the MockBoard class
 *
 * @author vic
 */
public class MockBoardTest extends TestCase
{

    public void testConstructor_empty()
    {
        final MockBoard board = new MockBoard("   ");

        assertEquals(3, board.getWidth());
        assertEquals(1, board.getHeight());
        assertFalse(board.isObstacle(0, 0));
        assertFalse(board.isObstacle(1, 0));
        assertFalse(board.isObstacle(2, 0));
    }

    public void testConstructor_N()
    {
        final MockBoard board = new MockBoard("#########\n" + "#       #\n" + "####### #\n" + "#       #\n" +
                                              "#########");

        assertEquals(9, board.getWidth());
        assertEquals(5, board.getHeight());
        assertTrue(board.isObstacle(0, 0));
        assertTrue(board.isObstacle(0, 1));
        assertTrue(board.isObstacle(1, 0));
        assertFalse(board.isObstacle(1, 1));
    }

}

/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.util.test;

import rlforj.IBoard;

/**
 * A simple board for testing LOS, Pathfinding, etc
 *
 * @author vic
 */
class MockBoard implements IBoard
{

    private final boolean[][] obstacle;

    public MockBoard(final String map)
    {
        final String[] mapText = map.split("\n");
        obstacle = new boolean[mapText.length][];
        int lineNo = 0;
        for (final String line : mapText)
        {
            final boolean[] lineTiles = new boolean[line.length()];
            for (int i = 0; i < line.length(); i++)
            {
                lineTiles[i] = line.charAt(i) == '#';
            }
            obstacle[lineNo++] = lineTiles;
        }
    }

    public boolean contains(final int x, final int y)
    {
        return x >= 0 && x < obstacle[0].length && y >= 0 && y < obstacle.length;
    }

    public boolean isObstacle(final int x, final int y)
    {
        return obstacle[y][x];
    }

    @Override
    public boolean blocksLight(final int x, final int y)
    {
        return isObstacle(x, y);
    }

    @Override
    public boolean blocksStep(final int x, final int y)
    {
        return isObstacle(x, y);
    }

    public void visit(final int x, final int y)
    {
    }

    public int getWidth()
    {
        return obstacle[0].length;
    }

    public int getHeight()
    {
        return obstacle.length;
    }
}

package rlforj.los.raymulticast;

// This file contains both the public World interface and a 
// package-visible implementation (SimpleWorld).

/**
 * <p>A Map object is a simple implementation of a world where some points are
 * impenetrable. Points can be marked as obstructed, and checked for obstruction.</p>
 * <p>The implied contract is that obstructionAt(a,b) is true iff
 * addObstruction(a,b) was called on the object in the past.</p>
 */
public interface World
{

    int getSize();

    void addObstruction(int x, int y);

    boolean obstructionAt(int x, int y);

}

/**
 * <p>A basic implementation of the World interface.</p>
 */
class SimpleWorld implements World
{

    private final boolean[][] obstructions;

    public SimpleWorld(final int size)
    {
        obstructions = new boolean[size][size];
    }

    public int getSize()
    {
        return obstructions.length;
    }

    public void addObstruction(final int x, final int y)
    {
        obstructions[x][y] = true;
    }

    public boolean obstructionAt(final int x, final int y)
    {
        return obstructions[x][y];
    }

}


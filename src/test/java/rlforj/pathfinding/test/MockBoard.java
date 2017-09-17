package rlforj.pathfinding.test;

import rlforj.los.ILosBoard;

/**
* A simple board for testing LOS, Pathfinding, etc
* @author vic
*/
class MockBoard implements ILosBoard {

    private boolean[][] obstacle;

    public MockBoard(String map)
    {
        String[] mapText = map.split("\n");
        obstacle = new boolean[mapText.length][];
        int lineNo = 0;
        for (String line: mapText)
        {
            boolean[] lineTiles = new boolean[line.length()];
            for (int i=0; i<line.length(); i++)
            {
                lineTiles[i] = line.charAt(i) == '#';
            }
            obstacle[lineNo++] = lineTiles;
        }
    }

    public boolean contains(int x, int y)
    {
        return x >= 0 && x < obstacle[0].length && y >= 0 && y < obstacle.length;
    }

    public boolean isObstacle(int x, int y)
    {
        return obstacle[y][x];
    }

    public void visit(int x, int y) { }

    public int getWidth()
    {
        return obstacle[0].length;
    }

    public int getHeight()
    {
        return obstacle.length;
    }
}

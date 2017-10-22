package rlforj.pathfinding;

import rlforj.los.ILosBoard;
import rlforj.math.Point2I;
import rlforj.util.HeapNode;
import rlforj.util.SimpleHeap;

import java.util.ArrayList;

public class AStar
{
    private final ILosBoard    map;
    private final int          boardWidth;
    private final int          boardHeight;
    private final boolean      allowDiagonal;
    public        PathNode[][] dbg_savedMap;

    public AStar(final ILosBoard map, final int boardWidth, final int boardHeight)
    {
        this(map, boardWidth, boardHeight, true);
    }

    public AStar(final ILosBoard map, final int boardWidth, final int boardHeight, final boolean allowDiagonal)
    {
        this.map = map;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.allowDiagonal = allowDiagonal;
    }

    public Point2I[] findPath(final int x, final int y, final int x1, final int y1)
    {
        final PathNode[][] nodeHash;
        if (!this.map.contains(x, y))
        {
            return null;
        }
        if (!this.map.contains(x1, y1))
        {
            return null;
        }
        this.dbg_savedMap = nodeHash = new PathNode[this.boardWidth][this.boardHeight];
        final SimpleHeap<HeapNode> open      = new SimpleHeap<HeapNode>(1000);
        final PathNode             startNode = new PathNode(x, y, 0.0);
        startNode.h = this.computeHeuristics(startNode, x1, y1, x, y);
        startNode.calcCost();
        open.add((HeapNode) startNode);
        nodeHash[x][y] = startNode;
        // int dbg_expanded = 0;
        while (open.size() > 0)
        {
            final PathNode step = (PathNode) open.poll();
            // ++dbg_expanded;
            if (step.x == x1 && step.y == y1)
            {
                return this.createPath(step);
            }
            int dx = -1;
            while (dx <= 1)
            {
                int dy = -1;
                while (dy <= 1)
                {
                    if (!(dx == 0 && dy == 0 || dx != 0 && dy != 0 && !this.allowDiagonal))
                    {
                        final int cx = step.x + dx;
                        final int cy = step.y + dy;
                        if (cx >= 0 && cy >= 0 && cx < this.boardWidth && cy < this.boardHeight &&
                            this.map.contains(cx, cy) && !this.map.isObstacle(cx, cy))
                        {
                            final PathNode n1;
                            double         this_cost = 0.0;
                            this_cost = dx != 0 && dy != 0 ? 1.1 : 1.0;
                            if (nodeHash[cx][cy] == null)
                            {
                                n1 = new PathNode(cx, cy, step.g + this_cost);
                                n1.prev = step;
                                n1.h = this.computeHeuristics(n1, x1, y1, x, y);
                                n1.calcCost();
                                open.add((HeapNode) n1);
                                nodeHash[cx][cy] = n1;
                            }
                            else
                            {
                                n1 = nodeHash[cx][cy];
                                if (n1.g > step.g + this_cost)
                                {
                                    n1.g = step.g + this_cost;
                                    n1.calcCost();
                                    n1.prev = step;
                                    if (open.contains((HeapNode) n1))
                                    {
                                        open.adjust((HeapNode) n1);
                                    }
                                    else
                                    {
                                        open.add((HeapNode) n1);
                                    }
                                }
                            }
                        }
                    }
                    ++dy;
                }
                ++dx;
            }
        }
        return null;
    }

    private double computeHeuristics(final PathNode node, final int x1, final int y1, final int startx,
                                     final int starty)
    {
        final int dx        = Math.abs(node.x - x1);
        final int dy        = Math.abs(node.y - y1);
        final int diagsteps = Math.min(dx, dy);
        return (double) diagsteps * 1.0 + (double) ((Math.max(dx, dy) - diagsteps) * 1) +
               (double) Math.abs((node.x - x1) * (starty - y1) - (node.y - y1) * (startx - x1)) * 0.01;
    }

    private Point2I[] createPath(PathNode end)
    {
        final ArrayList<Point2I> v = new ArrayList<Point2I>();
        while (end != null)
        {
            v.add(new Point2I(end.x, end.y));
            end = end.prev;
        }
        final int       sz  = v.size();
        final Point2I[] ret = new Point2I[sz];
        int             i   = 0;
        while (i < sz)
        {
            ret[i] = (Point2I) v.get(sz - i - 1);
            ++i;
        }
        return ret;
    }

    public static class PathNode implements HeapNode
    {
        public double g;
        public double h;
        int      x;
        int      y;
        double   cost;
        PathNode prev;
        int      heapIndex;

        public PathNode(final int x, final int y, double g)
        {
            this.x = x;
            this.y = y;
            this.g = g;
        }

        public PathNode(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public void calcCost()
        {
            this.cost = this.h + this.g;
        }

        public int compareTo(Object o)
        {
            return (int) Math.signum(this.cost - ((PathNode) o).cost);
        }

        public int getHeapIndex()
        {
            return this.heapIndex;
        }

        public void setHeapIndex(int heapIndex)
        {
            this.heapIndex = heapIndex;
        }
    }
}


package rlforj.pathfinding;

import java.util.ArrayList;
import rlforj.los.ILosBoard;
import rlforj.math.Point2I;
import rlforj.pathfinding.AStar;
import rlforj.util.HeapNode;
import rlforj.util.SimpleHeap;

public class AStar
{
    public static class PathNode implements HeapNode
    {
        int           x;
        int           y;
        double        cost;
        public double g;
        public double h;
        PathNode      prev;
        int           heapIndex;

        public PathNode(int x, int y, double g)
        {
            this.x = x;
            this.y = y;
            this.g = g;
        }

        public void calcCost()
        {
            this.cost = this.h + this.g;
        }

        public PathNode(int x, int y)
        {
            this.x = x;
            this.y = y;
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

    private ILosBoard   map;
    private int         boardWidth;
    private int         boardHeight;
    private boolean     allowDiagonal;
    public PathNode[][] dbg_savedMap;

    public AStar(ILosBoard map, int boardWidth, int boardHeight)
    {
        this(map, boardWidth, boardHeight, true);
    }

    public AStar(ILosBoard map, int boardWidth, int boardHeight, boolean allowDiagonal)
    {
        this.map = map;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.allowDiagonal = allowDiagonal;
    }

    public Point2I[] findPath(int x, int y, int x1, int y1)
    {
        PathNode[][] nodeHash;
        if (!this.map.contains(x, y))
        {
            return null;
        }
        if (!this.map.contains(x1, y1))
        {
            return null;
        }
        this.dbg_savedMap = nodeHash = new PathNode[this.boardWidth][this.boardHeight];
        SimpleHeap<HeapNode> open = new SimpleHeap<HeapNode>(1000);
        PathNode startNode = new PathNode(x, y, 0.0);
        startNode.h = this.computeHeuristics(startNode, x1, y1, x, y);
        startNode.calcCost();
        open.add((HeapNode) startNode);
        nodeHash[x][y] = startNode;
        // int dbg_expanded = 0;
        while (open.size() > 0)
        {
            PathNode step = (PathNode) open.poll();
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
                        int cx = step.x + dx;
                        int cy = step.y + dy;
                        if (cx >= 0
                                && cy >= 0
                                && cx < this.boardWidth
                                && cy < this.boardHeight
                                && this.map.contains(cx, cy)
                                && !this.map.isObstacle(cx, cy))
                        {
                            PathNode n1;
                            double this_cost = 0.0;
                            this_cost = dx != 0 && dy != 0 ? 1.1 : 1.0;
                            if (nodeHash[cx][cy] == null)
                            {
                                n1 = new PathNode(cx, cy, step.g + this_cost);
                                n1.prev = step;
                                n1.h = this.computeHeuristics(n1, x1, y1, x, y);
                                n1.calcCost();
                                open.add((HeapNode) n1);
                                nodeHash[cx][cy] = n1;
                            } else
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
                                    } else
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

    private double computeHeuristics(PathNode node, int x1, int y1, int startx, int starty)
    {
        int dx = Math.abs(node.x - x1);
        int dy = Math.abs(node.y - y1);
        int diagsteps = Math.min(dx, dy);
        return (double) diagsteps * 1.0
                + (double) ((Math.max(dx, dy) - diagsteps) * 1)
                + (double) Math.abs((node.x - x1) * (starty - y1) - (node.y - y1) * (startx - x1)) * 0.01;
    }

    private Point2I[] createPath(PathNode end)
    {
        ArrayList<Point2I> v = new ArrayList<Point2I>();
        while (end != null)
        {
            v.add(new Point2I(end.x, end.y));
            end = end.prev;
        }
        int sz = v.size();
        Point2I[] ret = new Point2I[sz];
        int i = 0;
        while (i < sz)
        {
            ret[i] = (Point2I) v.get(sz - i - 1);
            ++i;
        }
        return ret;
    }
}

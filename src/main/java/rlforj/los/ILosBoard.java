package rlforj.los;

import rlforj.pathfinding.AStar;

/**
 * An interface board that allows visibility alogithms to
 * decide which points are in the board, which points are
 * obstacles to this form of visibility, and visit those points
 * on the board.
 *
 * @author sdatta
 */
public interface ILosBoard
{

    /**
     * Is the location (x, y) inside the board ?
     * Note: If a point is outside, any radially
     * outward points are not checked, so the area must
     * be concave.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(int x, int y);

    /**
     * Is the location (x, y) an obstacle? Generic obstacles may block
     * either light (for FOV/LoS) or movement. Pathfinding usually
     * looks for cells that are both visible and don't block movement so
     * it will probably use this method. LoS/FoV might just want to know
     * about light blocking.
     *
     * @see AStar
     *
     * @param x
     * @param y
     * @return
     */
    default boolean isObstacle(int x, int y)
    {
        return blocksLight(x, y) || blocksStep(x, y);
    }

    /**
     * Does the location (x, y) block light? This is mainly used
     * by the FOV/LoS algorithms, since they don't care if you can step
     * there or not: just if you see it.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean blocksLight(int x, int y);

    /**
     * Does the location (x, y) block movement? Some implementations might
     * not care about light, for example if we have an "X-ray view" creature we'll
     * want to use special pathfinding/los/fov algorithms that only call blocksStep.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean blocksStep(int x, int y);

    /**
     * Location (x,y) is visible
     * Visit the location (x,y)
     * <p>
     * This can involve saving the points in a collection,
     * setting flags on a 2D map etc.
     *
     * @param x
     * @param y
     */
    public void visit(int x, int y);

}

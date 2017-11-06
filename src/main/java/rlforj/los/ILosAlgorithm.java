package rlforj.los;

import rlforj.IBoard;
import rlforj.math.Point;

import java.util.List;

/**
 * An interface for for LOS and projection
 *
 * @author sdatta
 */
public interface ILosAlgorithm
{

    /**
     * Calculates if line of sight exists between point startX, startY and
     * endX, y1. Optionally calculate the path of projection.
     *
     * @param b                The board to be visited.
     * @param startX           Starting position:x
     * @param startY           Starting position:y
     * @param endX             Target location:x
     * @param endY             Target location:y
     * @param calculateProject Whether to also calculate the path from the
     *                         source to the target.
     * @return true if a line of sight could be established
     */
    boolean existsLineOfSight(IBoard b, int startX, int startY, int endX, int endY, boolean calculateProject);

    /**
     * Obtain the path of the projection calculated during the last call
     * to existsLineOfSight.
     *
     * @return null if no los was established so far, or a list of points if a los found
     */
    List<Point> getProjectPath();

}

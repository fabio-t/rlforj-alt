package rlforj.los;

/**
 * FOV along a cone. Give starting and finish angle.
 * Note: Positive Y axis is down.
 *
 * @author sdatta
 */
public interface IConeFovAlgorithm extends IFovAlgorithm
{
    /**
     * Compute cone FOV on board b, starting from (x,y), from startAngle to
     * finishAngle.
     * Positive Y axis is downwards.
     *
     * @param b board
     * @param x x position
     * @param y y position
     * @param distance maximum distance of cone of view
     * @param startAngle start angle
     * @param finishAngle end angle
     */
    void visitConeFieldOfView(ILosBoard b, int x, int y, int distance, int startAngle, int finishAngle);
}

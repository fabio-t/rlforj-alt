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
     * @param b
     * @param x
     * @param y
     * @param distance
     * @param startAngle
     * @param finishAngle
     */
    void visitConeFieldOfView(ILosBoard b, int x, int y, int distance, int startAngle, int finishAngle);
}

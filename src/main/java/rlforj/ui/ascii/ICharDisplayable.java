package rlforj.ui.ascii;

/**
 * Encapsulates a 2D area composed of CharVisual's
 *
 * @author sdatta
 */
public interface ICharDisplayable
{

    /**
     * The width of the area
     *
     * @return
     */
    public int getWidth();

    /**
     * The height of the area
     *
     * @return
     */
    public int getHeight();

    /**
     * The character at a given location.
     *
     * @param x
     * @param y
     * @return
     */
    public CharVisual getCharAt(int x, int y);

    /**
     * Whether the given location is inside this 2D area.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(int x, int y);

}

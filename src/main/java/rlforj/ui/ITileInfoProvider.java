package rlforj.ui;

/**
 * An interface encapsulating objects that return information
 * about tiles at a location
 *
 * @author sdatta
 */
public interface ITileInfoProvider
{

    /**
     * Return the String describing the tile at (x, y) in the
     * current level.
     *
     * @param x
     * @param y
     * @return
     */
    public String getTileInfo(int x, int y);
}

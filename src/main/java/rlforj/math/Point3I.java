package rlforj.math;

/**
 * 3D integer precision point.
 * Immutable!
 *
 * @author sdatta
 */
public final class Point3I
{
    public final int x, y, z;
    private final int hashCode;

    public Point3I(int x, int y, int z)
    {
        super();
        this.x = x;
        this.y = y;
        this.z = z;

        hashCode = _hashCode();
    }

    //    public Point3I(double[] d)
    //	{
    //		x=(int) Math.floor(d[0]);
    //		y=(int) Math.floor(d[1]);
    //		z=(int) Math.floor(d[2]);
    //	}

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    private int _hashCode()
    {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point3I other = (Point3I) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }

    public Point3I offset(Point3I where)
    {
        return new Point3I(this.x + where.x, this.y + where.y, this.z + where.z);
    }
}

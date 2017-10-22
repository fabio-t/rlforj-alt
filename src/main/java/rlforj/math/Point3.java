package rlforj.math;

/**
 * 3D double precision point
 *
 * @author sdatta
 */
public class Point3
{
    public double x, y, z;

    public Point3(double x, double y, double z)
    {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3(double[] d)
    {
        super();
        x = d[0];
        y = d[1];
        z = d[2];
    }

    @Override
    public int hashCode()
    {
        final int PRIME  = 31;
        int       result = 1;
        long      temp;
        temp = Double.doubleToLongBits(x);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
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
        final Point3 other = (Point3) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }

}

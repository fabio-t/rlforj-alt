package rlforj.util;

/**
 * A cache that can cache circles, ie can tell by table lookup
 * if (x, y) is in a circle of radius r.
 * <p>
 * However this is useless as x*x+y*y &lt; r*r usually is faster
 * or takes about the same time
 *
 * @author sdatta
 */
public class CircleCache
{
    public final static int UPTO_RADIUS = 100;

    public static final int dist[] = new int[UPTO_RADIUS * UPTO_RADIUS];

    static
    {
        for (int i = 0; i < UPTO_RADIUS; i++)
        {
            for (int j = 0; j < UPTO_RADIUS; j++)
            {
                dist[i * UPTO_RADIUS + j] = (int) Math.floor(Math.sqrt(i * i + j * j) + .5);
            }
        }
    }

    public static void main(String[] args)
    {
        final int NUM_TIMES = 1000000;

        int[] x = new int[NUM_TIMES], y = new int[NUM_TIMES];

        for (int i = 0; i < NUM_TIMES; i++)
        {
            x[i] = (int) (Math.random() * UPTO_RADIUS);
            y[i] = (int) (Math.random() * UPTO_RADIUS);
        }

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < NUM_TIMES; i++)
        {
            boolean r = getDist(x[i], y[i]) < 200;
            if (!r)
                System.out.println();
        }
        long t2 = System.currentTimeMillis();

        System.out.println("Time " + (t2 - t1));

        t1 = System.currentTimeMillis();
        for (int i = 0; i < NUM_TIMES; i++)
        {
            boolean r = x[i] * x[i] + y[i] * y[i] < 40000;
            if (!r)
                System.out.println();
        }
        t2 = System.currentTimeMillis();
        System.out.println("Time " + (t2 - t1));

    }

    public static final int getDist(int x, int y)
    {
        return dist[x * UPTO_RADIUS + y];
    }
}

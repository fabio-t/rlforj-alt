package rlforj.los.test;

import rlforj.los.ILosAlgorithm;
import rlforj.los.ShadowCasting;
import rlforj.math.Point;

import java.util.List;
import java.util.Random;

public class ProjectionTest
{

    public static void main(final String[] args)
    {
        final Random    rand = new Random();
        final TestBoard tb   = new TestBoard(false);

        for (int i = 0; i < 50; i++)
        {
            tb.exception.add(new Point(rand.nextInt(21), rand.nextInt(21)));
        }

        final int x1 = rand.nextInt(21);
        final int y1 = rand.nextInt(21);

        //		ILosAlgorithm alg = new PrecisePermissive();
        final ILosAlgorithm alg = new ShadowCasting();

        final boolean     losExists = alg.existsLineOfSight(tb, 10, 10, x1, y1, true);
        final List<Point> path      = alg.getProjectPath();

        for (final Point p : path)
        {
            final int xx = p.x;
            final int yy = p.y;
            tb.mark(xx, yy, '-');
        }

        tb.mark(10, 10, '@');
        tb.mark(x1, y1, '*');

        tb.print(-1, 46, -1, 22);
        System.out.println("LosExists " + losExists);
    }
}

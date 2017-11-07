/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.examples;

import rlforj.los.IFovAlgorithm;
import rlforj.los.PrecisePermissive;
import rlforj.los.ShadowCasting;

import java.util.Random;

public class FovExample
{
    final static int width  = 17;
    final static int height = 17;

    /*
     * Each time creates a 21x21 area with random obstacles and
     * runs ShadowCasting and Precise Permissive algorithms
     * on it, printing out the results in stdout.
     */
    public static void main(final String[] args)
    {
        final ExampleBoard b    = new ExampleBoard(width, height);
        final Random       rand = new Random();
        for (int i = 0; i < 30; i++)
        {
            b.setObstacle(rand.nextInt(width), rand.nextInt(height));
        }

        System.out.println("ShadowCasting");
        IFovAlgorithm a = new ShadowCasting();
        a.visitFoV(b, width / 2, height / 2, width / 3 + 1);
        b.print(width / 2, height / 2);

        b.resetVisitedAndMarks();
        System.out.println("Precise Permissive");
        a = new PrecisePermissive();
        a.visitFoV(b, width / 2, height / 2, width / 3 + 1);
        b.print(width / 2, height / 2);
    }
}

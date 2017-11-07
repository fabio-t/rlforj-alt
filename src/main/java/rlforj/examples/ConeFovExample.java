/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.examples;

import rlforj.los.ConePrecisePremisive;
import rlforj.los.IConeFovAlgorithm;
import rlforj.los.ShadowCasting;

import java.util.Random;

public class ConeFovExample
{
    final static int width  = 21;
    final static int height = 21;

    public static void main(final String[] args)
    {
        final ExampleBoard b    = new ExampleBoard(width, height);
        final Random       rand = new Random();
        for (int i = 0; i < 30; i++)
        {
            b.setObstacle(rand.nextInt(width), rand.nextInt(height));
        }
        //		int startAngle=rand.nextInt(360), finishAngle=rand.nextInt(360);
        final int startAngle  = 30;
        final int finishAngle = 70;
        System.out.println(startAngle + " degrees to " + finishAngle + " degrees");
        System.out.println("ShadowCasting");
        IConeFovAlgorithm a = new ShadowCasting();
        a.visitConeFieldOfView(b, width / 2, height / 2, width / 3 + 1, startAngle, finishAngle);
        b.print(width / 2, height / 2);

        b.resetVisitedAndMarks();
        System.out.println("Precise Permissive");
        a = new ConePrecisePremisive();
        a.visitConeFieldOfView(b, width / 2, height / 2, width / 3 + 1, startAngle, finishAngle);
        b.print(width / 2, height / 2);
    }
}

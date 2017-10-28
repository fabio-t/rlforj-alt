package rlforj.examples;

import rlforj.los.ConePrecisePremisive;
import rlforj.los.IConeFovAlgorithm;
import rlforj.los.ShadowCasting;

import java.util.Random;

public class ConeFovExample
{

    public static void main(final String[] args)
    {
        final ExampleBoard b    = new ExampleBoard(21, 21);
        final Random       rand = new Random();
        for (int i = 0; i < 30; i++)
        {
            b.setObstacle(rand.nextInt(21), rand.nextInt(21));
        }
        //		int startAngle=rand.nextInt(360), finishAngle=rand.nextInt(360);
        final int startAngle  = 30;
        final int finishAngle = 70;
        System.out.println(startAngle + " degrees to " + finishAngle + " degrees");
        System.out.println("ShadowCasting");
        IConeFovAlgorithm a = new ShadowCasting();
        a.visitConeFieldOfView(b, 10, 10, 9, startAngle, finishAngle);
        b.print(10, 10);

        b.resetVisitedAndMarks();
        System.out.println("Precise Permissive");
        a = new ConePrecisePremisive();
        a.visitConeFieldOfView(b, 10, 10, 10, startAngle, finishAngle);
        b.print(10, 10);
    }
}

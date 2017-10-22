package rlforj.examples;

import rlforj.los.IFovAlgorithm;
import rlforj.los.PrecisePermissive;
import rlforj.los.ShadowCasting;

import java.util.Random;

public class FovExample
{

    /**
     * Each time creates a 21x21 area with random obstacles and
     * runs ShadowCasting and Precise Permissive algoritms
     * on it, printing out the results in stdout.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        ExampleBoard b    = new ExampleBoard(21, 21);
        Random       rand = new Random();
        for (int i = 0; i < 30; i++)
        {
            b.setObstacle(rand.nextInt(21), rand.nextInt(21));
        }

        System.out.println("ShadowCasting");
        IFovAlgorithm a = new ShadowCasting();
        a.visitFieldOfView(b, 10, 10, 9);
        b.print(10, 10);

        b.resetVisitedAndMarks();
        System.out.println("Precise Permissive");
        a = new PrecisePermissive();
        a.visitFieldOfView(b, 10, 10, 9);
        b.print(10, 10);
    }
}

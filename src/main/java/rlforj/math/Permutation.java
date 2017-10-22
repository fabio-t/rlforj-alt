package rlforj.math;

import java.util.Random;

/**
 * Class to generate permutations.
 *
 * @author sdatta
 */
public class Permutation
{

    /**
     * returns a random permutation of 1 to n.
     * <p>
     * Function copied from the fortran implementation of
     * Combinatorial Algorithms ...
     *
     * @param n
     * @return
     */
    public static int[] randomPermutation(int n)
    {
        return randomPermutation(new Random(), n);
    }

    public static int[] randomPermutation(Random rand, int n)
    {
        int[] a = new int[n];

        for (int i = 0; i < n; i++)
        {
            a[i] = i;
        }

        for (int i = 0; i < n; i++)
        {
            int j = (int) (i + rand.nextInt(n - i));

            //swap
            int temp = a[j];
            a[j] = a[i];
            a[i] = temp;
        }

        return a;
    }
    //	  subroutine ranper(n,a)
    //      integer a(n)
    //      do 10  i=1,n
    //10    a(i)=i
    //20    do 40  m=1,n
    //30    l=float(m)+rand(iseed)*float(n+1-m)
    //      l1=a(l)
    //      a(l)=a(m)
    //40    a(m)=l1
    //      return
}

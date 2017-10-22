package rlforj.util;

public class MathUtils
{

    public static final int abs(int i)
    {
        return (i < 0) ? -i : i;
    }

    /**
     * @param i
     * @return +1, -1 or 0
     */
    public static final int sgn(int i)
    {
        return (i > 0 ? 1 : (i == 0 ? 0 : -1));
    }

    public static final int max(int a, int b)
    {
        return a > b ? a : b;
    }

    public static final int min(int a, int b)
    {
        return a < b ? a : b;
    }

    public static final int isqrt(int x)
    {
        int op, res, one;

        op = x;
        res = 0;

	    /* "one" starts at the highest power of four <= than the argument. */
        one = 1 << 30;  /* second-to-top bit set */
        while (one > op)
            one >>= 2;

        while (one != 0)
        {
            if (op >= res + one)
            {
                op = op - (res + one);
                res = res + 2 * one;
            }
            res >>= 1;
            one >>= 2;
        }
        return (res);
    }

    private static int next(int n, int i)
    {
        return (n + i / n) >> 1;
    }

    /**
     * 20% slower than the other one
     *
     * @param number
     * @return
     */
    public static int isqrt2(int number)
    {
        int n  = 1;
        int n1 = next(n, number);

        while (Math.abs(n1 - n) > 1)
        {
            n = n1;
            n1 = next(n, number);
        }
        while ((n1 * n1) > number)
        {
            n1 -= 1;
        }
        return n1;
    }
}

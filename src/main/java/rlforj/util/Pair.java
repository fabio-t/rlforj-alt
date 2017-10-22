package rlforj.util;

/**
 * A pair of arbitrary objects
 * Used when you want to return 2 objects
 * Shortcut for a tuple.
 *
 * @param <E1>
 * @param <E2>
 * @author sdatta
 */
public class Pair<E1, E2>
{
    public E1 e1;
    public E2 e2;

    public Pair(E1 e1, E2 e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }

}

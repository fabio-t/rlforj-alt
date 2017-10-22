package rlforj.util.test;

import org.junit.Test;
import rlforj.util.HeapNode;
import rlforj.util.SimpleHeap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * SimpleHeap Test
 *
 * @author vic
 */
public class SimpleHeapTest
{
    // NOTE: JUnit 4 guideline is no longer to use TestCase but use annotations.
    // assert* can be static imported from org.junit.Assert.

    @Test
    public void testIndex()
    {
        final int[] arr = { 4, 5, 3, 7, 8, 1, 2, 20, 14, 100, -1 };

        final SimpleHeap<A> h = new SimpleHeap<A>(20);

        for (final int i : arr)
        {
            h.add(new A(i));
            assertIndexes(h);
        }

        while (h.size() != 0)
        {
            System.out.println(h.poll().a);
            assertIndexes(h);
        }

        h.clear();
        assertIndexes(h);

        final A a1 = new A(12);
        final A a2 = new A(5);
        final A a3 = new A(10);
        final A a4 = new A(1);

        h.add(a1);
        h.add(a2);
        h.add(a3);
        h.add(a4);
        assertIndexes(h);

        a2.a = 1000;
        h.adjust(a2);
        assertIndexes(h);

        h.poll();
        h.poll();
        h.poll();
        assertEquals(1000, h.poll().a);

        h.add(a1);
        h.add(a2);
        h.add(a3);
        h.add(a4);
        assertIndexes(h);

        a2.a = -1000;
        h.adjust(a2);
        assertIndexes(h);

        assertEquals(-1000, h.poll().a);
    }

    /**
     * Test that SimleHeap behaves like a heap. The top of the heap is always
     * the same as the first element in a sorted list.
     *
     * @throws Exception
     */
    @Test
    public void testHeapFunctionality() throws Exception
    {
        final Random        rand = new Random();
        final SimpleHeap<A> h    = new SimpleHeap<A>(50);
        final ArrayList<A>  arr  = new ArrayList<A>(1000);
        for (int i = 0; i < 1000; i++)
        {
            final A a = new A(rand.nextInt());
            h.add(a);
            arr.add(a);
        }

        Collections.sort(arr);

        for (int i = 0; i < 1000; i++)
        {
            final A a1 = h.poll();
            final A a2 = arr.remove(0);
            assertEquals("SimpleHeap does not match Array at " + i, a1.a, a2.a);

            if (rand.nextInt(100) < 30)
            {
                // Make sure SimpleHeap works in the face of random insertions.
                final A a = new A(rand.nextInt());
                h.add(a);
                arr.add(a);

                Collections.sort(arr);
            }

        }
    }

    /**
     * Test that heap properties are maintained in face of property changes and
     * adjustments.
     *
     * @throws Exception
     */
    @Test
    public void testHeapAdjust() throws Exception
    {
        final Random        rand = new Random();
        final SimpleHeap<A> h    = new SimpleHeap<A>(50);
        final ArrayList<A>  arr  = new ArrayList<A>(1000);
        for (int i = 0; i < 1000; i++)
        {
            final A a = new A(rand.nextInt());
            h.add(a);
            arr.add(a);
        }

        Collections.sort(arr);

        for (int i = 0; i < 2000; i++)
        {
            final A a1 = h.poll();
            final A a2 = arr.remove(0);

            assertEquals("SimpleHeap does not match Array at " + i, a1.a, a2.a);

            if (h.size() == 0)
            {
                break;
            }

            if (rand.nextInt(100) < 70)
            {
                // Make sure SimpleHeap works in the face of random adjusts.
                final int idx = rand.nextInt(h.size());
                final A   a   = h.getElementAt(idx);
                a.a = rand.nextInt();

                h.adjust(a);
                Collections.sort(arr);
            }

        }
    }

    private void assertIndexes(final SimpleHeap<A> h)
    {
        for (int i = 0; i < h.size(); i++)
        {
            assertEquals(i, (h.getElementAt(i)).idx);
        }
    }

    private static class A implements HeapNode
    {
        int a;

        int idx;

        public A(final int i)
        {
            a = i;
        }

        public int compareTo(final Object o)
        {
            if (this == o)
                return 0;
            final A a2 = (A) o;
            if (a == a2.a)
                return 0;
            else if (a < a2.a)
                return -1;
            else
                return 1; // Explicit comparison handles all values of integers better.
            // return a - a2.a;
        }

        public int getHeapIndex()
        {
            return idx;
        }

        public void setHeapIndex(final int heapIndex)
        {
            idx = heapIndex;
        }
    }

}

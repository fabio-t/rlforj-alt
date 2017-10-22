package rlforj.util;

/**
 * A Simple heap. Behaves pretty much like priority queue.
 * <p>
 * Differences: Objects are allowed to change, but the heap must be notified
 * immediately by calling adjust(Object).
 * Each object MUST implement HeapNode interface. This allows for storing
 * and retrieving the heap index directly and hence speeding up the adjust
 * process greatly. Objects should not modify the index stored.
 * <p>
 * Downside: Each object can only be stored in ONE SimpleHeap. Hopefully this
 * is the typical case.
 *
 * @param <T> The type
 */
public class SimpleHeap<T extends HeapNode>
{
    Object[] queue; // package-level for testing purpose
    private int size = 0;

    public SimpleHeap(int initialCapacity)
    {
        this.queue = new Object[initialCapacity];
    }

    /**
     * Add a new value. Null cannot be added.
     *
     * @param e
     * @return
     */
    public boolean add(T e)
    {
        if (e == null)
            throw new NullPointerException();
        int i = size;
        if (i >= queue.length)
            grow(i + 1);
        size = i + 1;
        if (i == 0)
        {
            queue[0] = e;
            e.setHeapIndex(0);
        }
        else
        {
            siftUp(i, e);
        }
        return true;
    }

    private void siftUp(int k, T x)
    {
        Comparable<? super T> key = (Comparable<? super T>) x;
        while (k > 0)
        {
            int parent = (k - 1) >>> 1;
            T   e      = (T) queue[parent];
            if (key.compareTo(e) >= 0)
                break;
            queue[k] = e;
            e.setHeapIndex(k);
            k = parent;
        }
        queue[k] = x;
        x.setHeapIndex(k);
    }

    private void siftDown(int k, T x)
    {
        Comparable<? super T> key  = (Comparable<? super T>) x;
        int                   half = size >>> 1;        // loop while a non-leaf
        while (k < half)
        {
            int child = (k << 1) + 1; // assume left child is least
            T   c     = (T) queue[child];
            int right = child + 1;
            if (right < size && c.compareTo(queue[right]) > 0)
                c = (T) queue[child = right];
            if (key.compareTo(c) <= 0)
                break;
            queue[k] = c;
            c.setHeapIndex(k);
            k = child;
        }
        queue[k] = x;
        x.setHeapIndex(k);
    }

    /**
     * Get the top element from the heap, removing it from the heap.
     * Returns null if none are left.
     *
     * @return
     */
    public T poll()
    {
        if (size == 0)
            return null;
        int s      = --size;
        T   result = (T) queue[0];
        T   x      = (T) queue[s];
        queue[s] = null;
        if (s != 0)
            siftDown(0, x);
        result.setHeapIndex(-1); // Mark it as not in heap
        return result;
    }

    public void adjust(T x)
    {
        if (x.getHeapIndex() < 0 || x.getHeapIndex() >= size)
            return;
        siftUp(x.getHeapIndex(), x);
        siftDown(x.getHeapIndex(), x);
    }

    public boolean contains(T x)
    {
        return x.getHeapIndex() >= 0 && x.getHeapIndex() < size && queue[x.getHeapIndex()] == x;
    }

    //    private void heapify() {
    //        for (int i = (size >>> 1) - 1; i >= 0; i--)
    //            siftDown(i, (T) queue[i]);
    //    }
    //
    //    private T removeAt(int i) {
    //        assert i >= 0 && i < size;
    //        int s = --size;
    //        if (s == i) // removed last element
    //            queue[i] = null;
    //        else {
    //            T moved = (T) queue[s];
    //            queue[s] = null;
    //            siftDown(i, moved);
    //            if (queue[i] == moved) {
    //                siftUp(i, moved);
    //                if (queue[i] != moved)
    //                    return moved;
    //            }
    //        }
    //        return null;
    //    }

    private void grow(int minCapacity)
    {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = ((oldCapacity < 64) ? ((oldCapacity + 1) * 2) : ((oldCapacity / 2) * 3));
        if (newCapacity < 0) // overflow
            newCapacity = Integer.MAX_VALUE;
        if (newCapacity < minCapacity)
            newCapacity = minCapacity;
        Object[] oldQueue = queue;
        queue = new Object[newCapacity];
        System.arraycopy(oldQueue, 0, queue, 0, oldQueue.length);
    }

    public int size()
    {
        return size;
    }

    public void clear()
    {
        for (int i = 0; i < size; i++)
            queue[i] = null;
        size = 0;
    }

    /**
     * Meant for testing rather than actual use.
     *
     * @param index
     * @return
     */
    public T getElementAt(int index)
    {
        if (index >= 0 && index < queue.length)
            return (T) queue[index];
        else
            return null;
    }
}

package rlforj.los;

import java.util.ListIterator;

/**
 * An iterator that behaves like C iterators
 *
 * @param <T>
 * @author sdatta
 */
public class CLikeIterator<T>
{

    ListIterator<T> it;
    T               curr;
    boolean atEnd = false, atBegin = false;

    public CLikeIterator(ListIterator<T> it)
    {
        super();
        this.it = it;
        if (it.hasNext())
            curr = it.next();
        else
        {
            curr = null;
            atEnd = true;
        }
        //		checkPrevNext();
    }

    //	private final void checkPrevNext()
    //	{
    ////		if(it.hasNext())
    ////			atEnd=false;
    ////		else
    ////			atEnd=true;
    //
    //		if(it.hasPrevious())
    //			atBegin=false;
    //		else
    //			atBegin=true;
    //	}

    public final T getCurrent()
    {
        return curr;
    }

    public void gotoNext()
    {
        if (it.hasNext())
        {
            curr = it.next();
        }
        else
        {
            atEnd = true;
            curr = null;
        }
        //		checkPrevNext();
    }

    public void gotoPrevious()
    {
        if (it.hasPrevious())
        {
            curr = it.previous();
        }
        //		else
        //		{
        //			curr=null;
        //		}
        //		checkPrevNext();
    }

    public boolean isAtEnd()
    {
        return atEnd;
    }

    public void removeCurrent()
    {
        it.remove();
        gotoNext();
    }

    public void insertBeforeCurrent(T t)
    {
        it.previous();
        it.add(t);
        //		checkPrevNext();
    }
}

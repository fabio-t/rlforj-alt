package rlforj.los;

/**
 * Exception thrown when a LOS function has not been implemented.
 *
 * @author sdatta
 */
public class LosNotImplementedException extends LosException
{

    /**
     *
     */
    private static final long serialVersionUID = -8455823610495925062L;

    public LosNotImplementedException()
    {
        super("Function not implemented");
    }

}

package httprequester;

import java.io.IOException;

/**
 *
 * @author Benjamin
 */
public class MyException extends IOException
{
    public MyException(String message)
    {
        super(message);
    }
}

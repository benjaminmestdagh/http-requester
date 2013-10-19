package httprequester;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Benjamin
 */
public class NotifyObject
{
    public static final int ERROR = 0, RESPONSE = 1, MESSAGE = 2, STARTING = 3, FINISHED = 4;
    private int status;
    private String message, data;
    private Map<String, List<String>> headers;
    
    public NotifyObject(String message, int status)
    {
        this.message = message;
        this.status = status;
        this.data = "";
        this.headers = null;
    }
    
    public NotifyObject()
    {
        this("", ERROR);
    }
    
    public void flush()
    {
        this.message = "";
        this.status = ERROR;
        this.data = "";
        this.headers = null;
    }

    public String getMessage()
    {
        return message;
    }

    public int getStatus()
    {
        return status;
    }

    public String getData()
    {
        return data;
    }

    public Map<String, List<String>> getHeaders()
    {
        if(headers == null)
        {
            Map<String, List<String>> result = new HashMap<>();
            result.put(null, null);
            return result;
        }
        else
        {
            return headers;
        }
    }
    
    public String getHeaderString()
    {
        if(headers == null)
        {
            return "";
        }
        else
        {
            StringBuilder result = new StringBuilder();

            for(Map.Entry e : getHeaders().entrySet())
            {
                if(e.getKey() != null)
                {
                    result.append((String)e.getKey());
                    result.append(": ");

                    for(String s : (List<String>)e.getValue())
                    {
                        result.append(s);
                    }
                }

                result.append("\n");
            }

            result.deleteCharAt(result.length() - 1);
            return result.toString();
        }
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public void setHeaders(Map<String, List<String>> headers)
    {
        this.headers = headers;
    }
}

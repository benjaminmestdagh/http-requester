package httprequester;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.List;

/**
 *
 * @author Benjamin
 */
public class RequestSender
{
    private HttpURLConnection con;
    private String data;
    private boolean getData;
    
    public RequestSender(String link, boolean getData)
    {
        try
        {
            this.getData = getData;
            URL url = new URL(link);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestProperty("User-Agent", "HttpRequester/" + HttpRequester.VERSION);
        } catch (Exception ex)
        {
        }
    }
    
    public void sendRequest() throws MyException
    {
        try
        {
            con.connect();
            
            if(getData)
            {
                StringBuilder sb = new StringBuilder();
                
                InputStream in = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = reader.readLine();
                sb.append(line);
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                
                data = sb.toString();
            }
            
            con.disconnect();
        }
        catch(UnknownHostException uex)
        {
            throw new MyException("Unknown host \""+ con.getURL() + "\". Check your url.");
        }
        catch(ConnectException cex)
        {
            throw new MyException("Unable to establish a connection. The host is probably down.");
        }
        catch(Exception iex)
        {
            //throw new MyException("An error occured: " + iex.getMessage());
        }
    }
    
    public String getResponse() throws MyException
    {
        return con.getHeaderField(0);
    }
    
    public boolean isSuccess() throws MyException
    {
        try
        {
            return con.getResponseCode() == 200;
        }
        catch(Exception ex)
        {
            throw new MyException(ex.getMessage());
        }
    }

    public String getData() throws MyException
    {
        if(getData)
        {
            return data;
        }
        else
        {
            throw new MyException("There is no data in this RequestSender.");
        }
    }
    
    public Map<String, List<String>> getHeaders()
    {
        return con.getHeaderFields();
    }
}

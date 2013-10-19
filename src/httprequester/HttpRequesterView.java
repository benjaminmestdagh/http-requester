package httprequester;

/**
 *
 * @author Benjamin
 */
public interface HttpRequesterView
{
    public void showView();
    
    public void errorUpdate();
    public void startingUpdate();
    public void finishingUpdate();
    
    public int getThreads();
    public int getRequests();
    public int getSleep();
    public String getUrl();
    public boolean getCheckUrl();
    public boolean getReturnData();
    public boolean getReturnHeaders();
}

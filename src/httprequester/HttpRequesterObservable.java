package httprequester;

/**
 *
 * @author Benjamin
 */
public interface HttpRequesterObservable
{
    public void addObserver(HttpRequesterObserver o);
    
    public void removeObserver(HttpRequesterObserver o);
    
    public void notifyObservers(NotifyObject arg);
    
    //public void notifyObservers();
}

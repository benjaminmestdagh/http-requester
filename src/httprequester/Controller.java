package httprequester;

import httprequesterviews.*;

/**
 *
 * @author Benjamin
 */
public class Controller implements HttpRequesterController, HttpRequesterObserver
{
    public static final int GUI = 1, CLI = 0;
    
    private HttpRequester hr;
    private HttpRequesterView view;
    
    public Controller(HttpRequester hr, int viewtype, String[] args)
    {
        this.hr = hr;
        hr.addObserver(this);
        
        if(viewtype == GUI)
            view = new HttpRequesterGui(hr, this);
        else
            view = new HttpRequesterCli(hr, this, args);
        
        view.showView();
    }
    
    @Override
    public void startHttpRequester()
    {
        hr.flush();
        hr.setGetData(view.getReturnData());
        hr.setGetHeaders(view.getReturnHeaders());
        hr.setRequests(view.getRequests());
        hr.setThreads(view.getThreads());
        hr.setSleep(view.getSleep());
        hr.setUrl(view.getUrl());
        hr.setUrlCheck(view.getCheckUrl());
        
        new Thread() {

            @Override
            public void run()
            { 
                hr.start();
            }
            
        }.start();
    }

    @Override
    public void update(NotifyObject arg)
    {
        switch(arg.getStatus())
        {
            case NotifyObject.STARTING:
                view.startingUpdate();
                break;
            case NotifyObject.ERROR:
                view.errorUpdate();
                break;
            case NotifyObject.FINISHED:
                view.finishingUpdate();
                break;
        }
    }
}

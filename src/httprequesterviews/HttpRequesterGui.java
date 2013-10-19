package httprequesterviews;

import httprequester.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Benjamin
 */
public class HttpRequesterGui implements HttpRequesterView, HttpRequesterObserver
{
    private JFrame theFrame;
    private GuiRequesterPanel thePanel;
    private HttpRequesterObservable hr;
    private HttpRequesterController controller;
    
    private int responseMessages;
    private int errorMessages;
    
    public HttpRequesterGui(HttpRequesterObservable hr, HttpRequesterController controller)
    {
        this.hr = hr;
        this.controller = controller;
        
        theFrame = new JFrame();
        setFrame();
    }
    
    @Override
    public void showView()
    {
        hr.addObserver(this);
        theFrame.setVisible(true);
    }

    private void setFrame()
    {
        Dimension dimension = new Dimension(600,400);
        thePanel = new GuiRequesterPanel();
        
        theFrame.setMinimumSize(dimension);
        theFrame.setPreferredSize(dimension);
        theFrame.add(thePanel);
        theFrame.setTitle("HttpRequester");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setLocationRelativeTo(null);
        theFrame.pack();
    }

    @Override
    public void errorUpdate()
    {
        thePanel.enableComponents(true);
    }

    @Override
    public void startingUpdate()
    {
        this.responseMessages = 0;
        this.errorMessages = 0;
        ((DefaultMutableTreeNode)thePanel.treeResult.getModel().getRoot()).removeAllChildren();
        ((DefaultTreeModel)thePanel.treeResult.getModel()).reload();
        thePanel.txtErrors.setText("");
        setPanelTitles();
        thePanel.enableComponents(false);
    }

    @Override
    public void finishingUpdate()
    {
        thePanel.enableComponents(true);
    }

    @Override
    public int getThreads()
    {
        return (int)thePanel.spnThreads.getValue();
    }

    @Override
    public int getRequests()
    {
        return (int)thePanel.spnRequests.getValue();
    }

    @Override
    public int getSleep()
    {
        return (int)thePanel.spnSleep.getValue();
    }

    @Override
    public String getUrl()
    {
        return thePanel.txtAddress.getText();
    }

    @Override
    public boolean getCheckUrl()
    {
        return thePanel.chkUrl.isSelected();
    }

    @Override
    public boolean getReturnData()
    {
        return thePanel.chkData.isSelected();
    }

    @Override
    public boolean getReturnHeaders()
    {
        return thePanel.chkHeaders.isSelected();
    }

    @Override
    public void update(NotifyObject arg)
    {
        switch(arg.getStatus())
        {
            case NotifyObject.STARTING:
                thePanel.lblMessage.setText(arg.getMessage());
                break;
            case NotifyObject.MESSAGE:
                //JOptionPane.showMessageDialog(theFrame, arg.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
                thePanel.txtErrors.setText(thePanel.txtErrors.getText() + arg.getMessage() + "\n");
                break;
            case NotifyObject.RESPONSE:
                this.responseMessages++;
                setPanelTitles();
                SwingUtilities.invokeLater(new TreeUpdateThread(arg));
                break;
            case NotifyObject.ERROR:
                this.errorMessages++;
                setPanelTitles();
                thePanel.txtErrors.setText(thePanel.txtErrors.getText() + "\n" + arg.getMessage());
                break;
            case NotifyObject.FINISHED:
                thePanel.lblMessage.setText("Finished. " + arg.getMessage());
                break;
        }
    }

    private void setPanelTitles()
    {
        thePanel.southPane.setTitleAt(0, "Responses (" + responseMessages + ")");
        thePanel.southPane.setTitleAt(1, "Errors (" + errorMessages + ")");
    }
    
    private class GuiRequesterPanel extends JPanel
    {
        JLabel lblAddress, lblRequests, lblThreads, lblSleep, lblMessage;
        JButton btnStart;
        JTree treeResult;
        JTextField txtAddress;
        JTextArea txtErrors;
        JSpinner spnRequests, spnThreads, spnSleep;
        JCheckBox chkUrl, chkHeaders, chkData;
        JScrollPane pnlResultText, pnlErrorText;
        JTabbedPane southPane;

        public GuiRequesterPanel()
        {
            setComponents();
            layoutComponents();
            addListeners();
        }

        private void setComponents()
        {
            lblAddress = new JLabel("Address");
            lblRequests = new JLabel("Requests:");
            lblThreads = new JLabel("Threads:");
            lblSleep = new JLabel("Sleep (ms):");
            lblMessage = new JLabel();
            btnStart = new JButton("Start");
            txtAddress = new JTextField();
            spnRequests = new JSpinner();
            spnThreads = new JSpinner();
            spnSleep = new JSpinner();
            chkUrl = new JCheckBox("Check before starting");
            chkHeaders = new JCheckBox("Show response headers");
            chkData = new JCheckBox("Show response data");
            pnlResultText = new JScrollPane();
            pnlErrorText = new JScrollPane();
            treeResult = new JTree();
            txtErrors = new JTextArea();
            southPane = new JTabbedPane();

            btnStart.setEnabled(false);

            txtAddress.setPreferredSize(new Dimension(350, 20));

            TreeModel tm = new DefaultTreeModel(new DefaultMutableTreeNode());
            treeResult.setCellRenderer(new HttpRequesterTreeCellRenderer());
            treeResult.setModel(tm);
            treeResult.setRootVisible(false);
            pnlResultText.getViewport().add(treeResult);
            
            txtErrors.setEditable(false);
            pnlErrorText.getViewport().add(txtErrors);

            SpinnerModel rspm = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
            spnRequests.setPreferredSize(new Dimension(50, 20));
            spnRequests.setModel(rspm);

            SpinnerModel tspm = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
            spnThreads.setPreferredSize(new Dimension(50, 20));
            spnThreads.setModel(tspm);

            SpinnerModel sspm = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
            spnSleep.setPreferredSize(new Dimension(50, 20));
            spnSleep.setModel(sspm);
        }

        private void layoutComponents()
        {
            setLayout(new BorderLayout());

            JPanel pnlNorth = new JPanel();
            {
                pnlNorth.setPreferredSize(new Dimension(600, 150));
                pnlNorth.setLayout(new GridLayout(4,1));

                JPanel pnlGridNorth = new JPanel();
                {
                    pnlGridNorth.setLayout(new FlowLayout(FlowLayout.LEADING));
                    pnlGridNorth.add(lblAddress);
                    pnlGridNorth.add(txtAddress);
                    pnlGridNorth.add(chkUrl);
                }

                JPanel pnlGridCenter = new JPanel();
                {
                    pnlGridCenter.setLayout(new FlowLayout(FlowLayout.LEADING));

                    JPanel pnlRequests = new JPanel();
                    {
                        pnlRequests.add(lblRequests);
                        pnlRequests.add(spnRequests);
                    }

                    JPanel pnlThreads = new JPanel();
                    {
                        pnlThreads.add(lblThreads);
                        pnlThreads.add(spnThreads);
                    }

                    JPanel pnlSleep = new JPanel();
                    {
                        pnlSleep.add(lblSleep);
                        pnlSleep.add(spnSleep);
                    }

                    pnlGridCenter.add(pnlRequests);
                    pnlGridCenter.add(pnlThreads);
                    pnlGridCenter.add(pnlSleep);
                }

                JPanel pnlGridSouth = new JPanel();
                {
                    pnlGridSouth.setLayout(new FlowLayout(FlowLayout.LEADING));
                    pnlGridSouth.add(chkHeaders);
                    pnlGridSouth.add(chkData);
                }

                JPanel pnlStart = new JPanel();
                {
                    pnlStart.setLayout(new FlowLayout(FlowLayout.LEADING));
                    pnlStart.add(btnStart);
                    pnlStart.add(lblMessage);
                }


                pnlNorth.add(pnlGridNorth);
                pnlNorth.add(pnlGridCenter);
                pnlNorth.add(pnlGridSouth);
                pnlNorth.add(pnlStart);
            }

            JPanel pnlSouth = new JPanel();
            pnlSouth.setLayout(new GridLayout(1,0));
            {
                southPane.addTab("Responses", pnlResultText);
                southPane.addTab("Errors", pnlErrorText);
                
                pnlSouth.add(southPane);
            }

            add(pnlNorth, BorderLayout.NORTH);
            add(pnlSouth, BorderLayout.CENTER);
        }

        private void addListeners()
        {
            btnStart.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.startHttpRequester();
                }
            });

            txtAddress.addKeyListener(new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e)
                {
                    btnStart.setEnabled(!txtAddress.getText().equals(""));
                }

            });
        }

        public void enableComponents(boolean enable)
        {  
            for(Component c : getComponents())
            {
                enablePanelComponents((JPanel)c, enable);
            }
        }

        private void enablePanelComponents(JPanel c, boolean enable)
        {
            for(Component com : c.getComponents())
            {
                if(!com.equals(southPane))
                {
                    com.setEnabled(enable);
                    if(com instanceof JPanel) enablePanelComponents((JPanel)com, enable);
                }
            }
        }
        
        private void addResponseToTree(NotifyObject arg)
        {
            DefaultMutableTreeNode response = new DefaultMutableTreeNode(arg.getMessage());
            if(chkHeaders.isSelected())
            {
                response.add(createHeadersNode(arg.getHeaders()));
            }

            if(chkData.isSelected())
            {
                DefaultMutableTreeNode data = new DefaultMutableTreeNode("Data");
                data.add(new DefaultMutableTreeNode(arg.getData()));
                response.add(data);
            }

            DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeResult.getModel().getRoot();
            ((DefaultTreeModel)treeResult.getModel()).insertNodeInto(response, root, root.getChildCount());
            ((DefaultTreeModel)treeResult.getModel()).reload();
        }

        private MutableTreeNode createHeadersNode(Map<String, java.util.List<String>> headerList)
        {
            DefaultMutableTreeNode headers = new DefaultMutableTreeNode("Headers");

            for(Map.Entry e : headerList.entrySet())
            {
                if(e.getKey() != null)
                {
                    DefaultMutableTreeNode header = new DefaultMutableTreeNode(e.getKey().toString());

                    for(String s : (java.util.List<String>)e.getValue())
                    {
                        header.add(new DefaultMutableTreeNode(s));
                    }

                    headers.add(header);
                }
            }

            return headers;
        }
    }
    
    private class TreeUpdateThread extends Thread
    {
        private NotifyObject arg;
        
        public TreeUpdateThread(NotifyObject arg)
        {
            this.arg = arg;
        }
        
        @Override
        public void run()
        {
            thePanel.addResponseToTree(arg);
        }
    }
}

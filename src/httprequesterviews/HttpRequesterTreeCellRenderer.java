package httprequesterviews;

import java.awt.Component;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Benjamin
 */
public class HttpRequesterTreeCellRenderer extends DefaultTreeCellRenderer
{
    ImageIcon dataIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/httprequestericons/data.png")));
    ImageIcon responseIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/httprequestericons/response.png")));
    ImageIcon headersIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/httprequestericons/headers.png")));
    ImageIcon headerIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/httprequestericons/header.png")));
    ImageIcon textIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/httprequestericons/text.png")));
    ImageIcon dataContentIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/httprequestericons/data-content.png")));
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree,
      Object value,boolean sel,boolean expanded,boolean leaf,
      int row,boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, 
          expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object userObject = node.getUserObject();
        
        if(node.getLevel() == 1)
        {
            setIcon(responseIcon);
        }
        else if(userObject != null)
        {
            switch (node.getUserObject().toString()) 
            {
                case "Data":
                    setIcon(dataIcon);
                    break;
                case "Headers":
                    setIcon(headersIcon);
                    break;
                default:
                    setChildIcon(node);
                    break;
            }
        }
        
        return this;
    }

    private void setChildIcon(DefaultMutableTreeNode node)
    {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
        if(parent != null)
        {
            switch(parent.getUserObject().toString())
            {
                case "Data":
                    setIcon(dataContentIcon);
                    break;
                case "Headers":
                    setIcon(headerIcon);
                    break;
                default:
                    setIcon(textIcon);
                    break;
            }
        }
    }
}

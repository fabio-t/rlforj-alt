package rlforj.ui;

import javax.swing.*;
import javax.swing.plaf.ToolTipUI;
import javax.swing.plaf.metal.MetalToolTipUI;
import java.awt.*;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * A class that displays multiple lines of Tooltip, without resorting to HTML
 * in the standard setting.
 * <p>
 * $Header: /cvsroot/applejuicejava/AJClientGUI/src/de/applejuicenet/client/shared/MultiLineToolTip.java,v 1.12 2005/02/28 14:58:19 maj0r Exp $
 * <p>
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI fuer den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 */
public class MultiLineToolTip extends JToolTip
{

    public MultiLineToolTip()
    {
        setUI(new MultiLineToolTipUI());
    }

    public MultiLineToolTip(MetalToolTipUI toolTipUI)
    {
        setUI(toolTipUI);
    }

    public static void main(String[] args)
    {
        // create a frame, add a label, and set the tooltip of the label to use a test tooltip...
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new JFrame("Test");
                frame.getContentPane().setLayout(new BorderLayout());
                JLabel testLabel = new JLabel("This is a label with a tooltip")
                {
                    public JToolTip createToolTip()
                    {
                        MultiLineToolTip tip = null;
                        tip = new MultiLineToolTip();
                        tip.setComponent(this);
                        return tip;
                    }
                };
                testLabel.setToolTipText("Hello!|  I am a tooltip text string|Hi");

                frame.getContentPane().add(testLabel, BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    /**
     * Set which character in the tooltip will be considered to be
     * the newline, the character at which display goes to the next line
     * Default is \n
     *
     * @param delim
     */
    public void setDelimiter(String delim)
    {
        ToolTipUI _ui = getUI();
        if (_ui instanceof MultiLineToolTipUI)
        {
            MultiLineToolTipUI mui = (MultiLineToolTipUI) _ui;
            mui.setDelimiter(delim);
        }
    }

    private class MultiLineToolTipUI extends MetalToolTipUI
    {
        private String[] strs;
        private String delim = "\n";

        public void paint(Graphics g, JComponent c)
        {
            FontMetrics metrics = c.getFontMetrics(c.getFont());
            Dimension   size    = c.getSize();
            g.setColor(c.getBackground());
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(c.getForeground());
            if (strs != null)
            {
                int length = strs.length;
                for (int i = 0; i < length; i++)
                {
                    g.drawString(strs[i], 3, (metrics.getHeight()) * (i + 1));
                }
            }
        }

        public Dimension getPreferredSize(JComponent c)
        {
            FontMetrics metrics = c.getFontMetrics(c.getFont());
            String      tipText = ((JToolTip) c).getTipText();
            if (tipText == null)
            {
                tipText = "";
            }
            StringTokenizer st       = new StringTokenizer(tipText, delim);
            int             maxWidth = 0;
            Vector<String>  v        = new Vector<String>();
            while (st.hasMoreTokens())
            {
                String token = st.nextToken();
                int    width = SwingUtilities.computeStringWidth(metrics, token);
                maxWidth = (maxWidth < width) ? width : maxWidth;
                v.addElement(token);
            }
            int lines = v.size();
            if (lines < 1)
            {
                strs = null;
                lines = 1;
            }
            else
            {
                strs = new String[lines];
                int i = 0;
                for (Enumeration e = v.elements(); e.hasMoreElements(); i++)
                {
                    strs[i] = (String) e.nextElement();
                }
            }
            int height = metrics.getHeight() * lines;
            return new Dimension(maxWidth + 6, height + 4);
        }

        public void setDelimiter(String delim)
        {
            this.delim = delim;
        }
    }
}

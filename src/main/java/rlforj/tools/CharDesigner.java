package rlforj.tools;

import rlforj.ui.ascii.CharVisual;
import rlforj.ui.ascii.CharVisualCanvas;
import rlforj.ui.ascii.ICharDisplayable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class CharDesigner
{

    static JTextArea ta = new JTextArea();

    public CharDesigner()
    {
    }

    public static void main(String[] args)
    {
        //TODO: move all to constructor
        final AffineTransform transform = AffineTransform.getScaleInstance(5, 5);
        new CharVisualCanvas();//force initialize
        final CharVisual cv = new CharVisual('X', Color.white);
        cv.font = CharVisualCanvas.getBaseFont();
        //		cv.font=new Font("Courier New", Font.PLAIN, 10);
        final CharVisualCanvas cvc = new CharVisualCanvas(new ICharDisplayable()
        {
            public boolean contains(int x, int y)
            {
                return (x == 0 && y == 0);
            }

            public CharVisual getCharAt(int x, int y)
            {
                return cv;
            }

            public int getHeight()
            {
                return 1;
            }

            public int getWidth()
            {
                return 1;
            }

        });
        final CharVisualCanvas cvc9 = new CharVisualCanvas(new ICharDisplayable()
        {
            public boolean contains(int x, int y)
            {
                return (x >= 0 && x < 5 && y >= 0 && y < 5);
            }

            public CharVisual getCharAt(int x, int y)
            {
                return cv;
            }

            public int getHeight()
            {
                return 5;
            }

            public int getWidth()
            {
                return 5;
            }

        });
        cvc.setTileTransform(transform);
        cvc9.setTileTransform(AffineTransform.getScaleInstance(2.5, 2.5));

        cvc.addMouseWheelListener(new MouseWheelListener()
        {
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (e.getWheelRotation() > 0)
                    applyTransformToCV(cv, cvc, cvc9, AffineTransform.getScaleInstance(.9, .9));
                else
                    applyTransformToCV(cv, cvc, cvc9, AffineTransform.getScaleInstance(1.1, 1.1));
            }

        });

        class DragMoveRot implements MouseListener, MouseMotionListener
        {
            int lastx, lasty;
            int button;

            public void mouseClicked(MouseEvent e)
            {
                // TODO Auto-generated method stub

            }

            public void mouseEntered(MouseEvent e)
            {
                // TODO Auto-generated method stub

            }

            public void mouseExited(MouseEvent e)
            {
                // TODO Auto-generated method stub

            }

            public void mousePressed(MouseEvent e)
            {
                lastx = e.getX();
                lasty = e.getY();
                button = e.getButton();
                System.out.println("Press " + e.getButton());
            }

            public void mouseReleased(MouseEvent e)
            {
                System.out.println("release");
            }

            public void mouseDragged(MouseEvent e)
            {
                System.out.println("drag " + lastx + " " + lasty + " " + e.getX() + " " + e.getY());
                System.out.println("Button " + e.getButton());
                if (button == MouseEvent.BUTTON1)
                {
                    applyTransformToCV(cv,
                                       cvc,
                                       cvc9,
                                       AffineTransform
                                           .getTranslateInstance((e.getX() - lastx) / 10.0, (e.getY() - lasty) / 10.0));
                }
                else if (button == MouseEvent.BUTTON3)
                {
                    double angle     = Math.atan2(e.getY(), e.getX());
                    double lastangle = Math.atan2(lasty, lastx);
                    applyTransformToCV(cv, cvc, cvc9, AffineTransform.getRotateInstance(angle - lastangle));

                }
                lastx = e.getX();
                lasty = e.getY();
            }

            public void mouseMoved(MouseEvent e)
            {
                // TODO Auto-generated method stub

            }

        }

        DragMoveRot quickTransform = new DragMoveRot();
        cvc.addMouseMotionListener(quickTransform);
        cvc.addMouseListener(quickTransform);

        final JFrame jf = new JFrame();

        //		Container cp=jf.getContentPane();
        //		cp.setLayout(new FlowLayout());

        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        jf.getContentPane().add(cp, BorderLayout.CENTER);

        JPanel top = new JPanel();
        jf.getContentPane().add(top, BorderLayout.NORTH);
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        jf.getContentPane().add(left, BorderLayout.WEST);
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        jf.getContentPane().add(right, BorderLayout.EAST);
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        jf.getContentPane().add(bottom, BorderLayout.SOUTH);
        JPanel bottom1 = new JPanel();
        bottom.add(bottom1, BorderLayout.SOUTH);
        JPanel bottom2 = new JPanel();
        bottom.add(bottom2, BorderLayout.SOUTH);

        JButton trxp = new JButton("Translate X +"), trxm = new JButton("Translate X -");
        trxp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getTranslateInstance(1, 0));
            }
        });
        trxm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getTranslateInstance(-1, 0));
            }
        });

        right.add(trxp);
        left.add(trxm);

        JButton tryp = new JButton("Translate Y +"), trym = new JButton("Translate Y -");
        tryp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getTranslateInstance(0, 1));
            }
        });
        trym.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getTranslateInstance(0, -1));
            }
        });

        bottom1.add(tryp);
        top.add(trym);

        JButton scxp = new JButton("Scale X +"), scxm = new JButton("Scale X -");
        scxp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getScaleInstance(1.1, 1));
            }
        });
        scxm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getScaleInstance(.9, 1));
            }
        });

        right.add(scxp);
        left.add(scxm);

        JButton scyp = new JButton("Scale Y +"), scym = new JButton("Scale Y -");
        scyp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getScaleInstance(1, 1.1));
            }
        });
        scym.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getScaleInstance(1, .9));
            }
        });

        bottom1.add(scyp);
        top.add(scym);

        JButton flipx = new JButton("Flip X"), flipy = new JButton("Flip Y");
        flipx.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                AffineTransform tr = new AffineTransform();
                tr.scale(-1, 1);
                tr.translate(-5.4, 0);

                applyTransformToCV(cv, cvc, cvc9, tr);
            }
        });
        flipy.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                AffineTransform tr = new AffineTransform();
                tr.scale(1, -1);
                tr.translate(0, 6);

                applyTransformToCV(cv, cvc, cvc9, tr);
            }
        });
        bottom2.add(flipx);
        bottom2.add(flipy);

        JButton rotp = new JButton("Rotate +"), rotm = new JButton("Rotate -");
        rotp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getRotateInstance(Math.PI / 32));
            }
        });
        rotm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                applyTransformToCV(cv, cvc, cvc9, AffineTransform.getRotateInstance(-Math.PI / 32));
            }
        });
        JButton fc = new JButton("Set Character/Font");
        fc.addActionListener(new ActionListener()
        {
            CharChooser c = new CharChooser();

            public void actionPerformed(ActionEvent e)
            {
                c.displayDialog(null);
                Character c1 = c.getSelectedChar();
                if (c1 != null)
                {
                    Font f = c.getSelectedFont();
                    //					System.out.println(Integer.toString(c1)+" "+f);
                    cv.font = f;
                    cv.disp = c1;
                    cvc.forceRedrawAll();
                    cvc.repaint();
                    cvc9.forceRedrawAll();
                    cvc9.repaint();
                    refreshCode(cv);
                }
            }
        });
        bottom2.add(fc);

        JButton colorC = new JButton("Choose Color");
        colorC.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Color c1 = JColorChooser.showDialog(null, "Choose Color", cv.col);
                if (c1 != null)
                    cv.col = c1;

                cvc.forceRedrawAll();
                cvc.repaint();
                cvc9.forceRedrawAll();
                cvc9.repaint();
                refreshCode(cv);
            }
        });
        bottom2.add(colorC);

        right.add(rotp);
        left.add(rotm);

        JLabel lbl1 = new JLabel("Tile Preview");
        lbl1.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        cp.add(lbl1);
        cp.add(cvc);
        JLabel lbl2 = new JLabel("Tile as flood terrain Preview");
        lbl2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        cp.add(lbl2);
        cp.add(cvc9);
        cp.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel taLab = new JLabel("The code needed to create this CharVisual");
        taLab.setLabelFor(ta);

        ta.setEditable(false);
        ta.setBackground(Color.white);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        refreshCode(cv);
        ta.setBorder(BorderFactory.createEtchedBorder());

        JPanel bottom3 = new JPanel();
        bottom3.setLayout(new BoxLayout(bottom3, BoxLayout.Y_AXIS));
        bottom.add(bottom3);
        bottom3.add(taLab);
        bottom3.add(Box.createVerticalStrut(20));
        bottom3.add(new JScrollPane(ta));
        bottom3.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        jf.pack();
        jf.addWindowListener(new WindowListener()
        {
            public void windowActivated(WindowEvent e)
            {
            }

            public void windowClosed(WindowEvent e)
            {
            }

            public void windowClosing(WindowEvent e)
            {
                jf.setVisible(false);
                jf.dispose();
            }

            public void windowDeactivated(WindowEvent e)
            {
            }

            public void windowDeiconified(WindowEvent e)
            {
            }

            public void windowIconified(WindowEvent e)
            {
            }

            public void windowOpened(WindowEvent e)
            {
            }
        });
        jf.setVisible(true);
        //		CharVisual cv=new CharVisual('s', Color.white);
        //		CharVisualCanvas cvc=new CharVisualCanvas();
        //		CharChooser c=new CharChooser();
        //		c.displayDialog(null);
        //		System.out.println("Done");
        //		System.exit(0);
    }

    /**
     * @param cv
     * @param cvc
     * @param cvc9
     * @param transform
     */
    static void applyTransformToCV(final CharVisual cv, final CharVisualCanvas cvc, final CharVisualCanvas cvc9,
                                   AffineTransform transform)
    {
        AffineTransform tr = cv.font.getTransform();
        tr.preConcatenate(transform);
        cv.font = cv.font.deriveFont(tr);
        //				cvc.setTileTransform(transform);
        cvc.forceRedrawAll();
        cvc.repaint();
        cvc9.forceRedrawAll();
        cvc9.repaint();

        refreshCode(cv);
    }

    static void refreshCode(CharVisual cv)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CharVisual cv=new CharVisual((char)").append((int) cv.disp).append(", new Color(")
          .append(cv.col.getRed()).append(", ").append(cv.col.getGreen()).append(", ").append(cv.col.getBlue())
          .append("));\n");

        if (cv.font != null)
        {
            Font f = cv.font;

            if (f.equals(CharVisualCanvas.getBaseFont()))
            {
                sb.append("cv.font=CharVisualCanvas.getBaseFont();\n");
            }
            else
            {
                sb.append("cv.font=new Font(\"").append(f.getFamily()).append("\", ");

                boolean style = false;
                if (f.isPlain())
                    sb.append("Font.PLAIN");
                if (f.isBold())
                {
                    style = true;
                    sb.append("Font.BOLD");
                }
                if (f.isItalic())
                {
                    if (style)
                        sb.append("+");
                    sb.append("Font.ITALIC");
                }
                sb.append(", ");

                sb.append(f.getSize()).append(");\n");
            }
            AffineTransform t = f.getTransform();
            if (!t.isIdentity())
            {
                double[] mat = new double[6];
                t.getMatrix(mat);
                sb.append("cv.font=cv.font.deriveFont(new AffineTransform(new double[]{\n\t");
                for (int i = 0; i < 6; i++)
                {
                    sb.append(mat[i]).append(", ");
                    if (i == 2)
                        sb.append("\n\t");
                }
                sb.append(" }));");
            }
        }
        ta.setText(sb.toString());
    }
}

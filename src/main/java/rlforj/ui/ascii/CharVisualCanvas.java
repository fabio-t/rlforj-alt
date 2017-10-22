package rlforj.ui.ascii;

import rlforj.math.Point2I;
import rlforj.ui.ITileInfoProvider;
import rlforj.ui.MultiLineToolTip;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A widget that can display a 2D set of colored ASCII
 * characters. The default font is Courier New, size 10.
 * <p>
 * The size of the font can be changed , or any
 * AffineTransform can be applied to all displayed characters
 * using setFontSize(int size) or setTileTransform(AffineTransform tr).
 * <p>
 * The character to be displayed is encapsulated in a
 * CharVisual class. The class can also have a different font
 * defined, in which this character is shown in that font. The
 * character displayed in the new font will be clipped by the
 * boundaries of the base font.
 * If the font is null, the default font is used.
 * <p>
 * A null character to be displayed displays an empty box.
 * <p>
 * The widget can also display information about each tile as tooltip.
 * For that purpose, a TIleInfoProvider object must be specified.
 *
 * @author sdatta
 */
public class CharVisualCanvas extends JLabel implements Scrollable
{

    /**
     *
     */
    private static final long serialVersionUID = 7410223976326988419L;

    /**
     * The base default font.
     */
    protected static Font baseFont;

    static
    {
        try
        {
            baseFont = Font.createFont(Font.TRUETYPE_FONT,
                                       CharVisualCanvas.class.getResourceAsStream("/fonts/dejavu/DejaVuSansMono.ttf"))
                           .deriveFont(10f);
        } catch (Exception e)
        {
            e.printStackTrace();
            baseFont = new Font("Monospaced", Font.PLAIN, 10);
        }

    }

    /**
     * Current font to use to display, usually baseFont with an
     * AffineTransform applied.
     */
    Font f = baseFont;

    /**
     * Current font cell size
     */
    int fontW = -1, fontH = -1;

    /**
     * The offset to the baseline of string
     */
    int offsety;
    /**
     * The visuals that are displayed as a 2D array are cached,
     * which helps to calcuate dirty rectangles.
     * <p>
     * The 2D array is flattened in a 1D array for faster access.
     */
    CharVisual[] cacheChars = new CharVisual[0];
    /**
     * Size of the 2D array of ASCII.
     */
    int          cacheW     = 0, cacheH = 0;
    /**
     * Size of this widget
     */
    Dimension size;
    /**
     * The provider used to show tooltips.
     * If this is set to null, tooltip display is
     * turned off.
     */
    ITileInfoProvider        infoProvider = null;
    /**
     * An overlay of temporary characters to be displayed on the map.
     * Makes displaying projectiles, AOE spell effects, etc display
     * easier.
     * Maybe this functionality should be moved to the model (ICharDisplayable)
     * ?
     */
    Map<Point2I, CharVisual> overlay      = new HashMap<Point2I, CharVisual>();
    /**
     * When the display area is smaller than the available area, the display
     * is centered.
     */
    int                      imageStartX  = 0, imageStartY = 0;
    /**
     * A cache of rendered images for all characters, hopefully making
     * display faster.
     */
    Map<CharVisual, Image> rendered         = new HashMap<CharVisual, Image>();
    /**
     * The current transform used to display the font. Used to control the
     * size of the font, also any other fancy effects if required.
     */
    AffineTransform        currentTransform = new AffineTransform();//identity
    Composite composite;
    /**
     * The backbuffer
     */
    private BufferedImage backImage = null;

    /**
     * CharVisualDisplay with empty map.
     */
    public CharVisualCanvas()
    {
        this(null);
    }

    /**
     * CharVisualCanvas with a map.
     *
     * @param wmap
     */
    public CharVisualCanvas(ICharDisplayable wmap)
    {
        FontMetrics fm = getFontMetrics(f);

        fontW = fm.charWidth('X');//fm.getMaxAdvance();
        fontH = fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading();
        offsety = fm.getMaxAscent();
        System.out.println("Size " + fontW + " " + fontH);

        if (wmap != null)
            size = new Dimension(fontW * wmap.getWidth(), fontH * wmap.getHeight());
        else
            size = new Dimension(1, 1);

        backImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
                                       .createCompatibleImage(size.width, size.height);

        if (wmap != null)
            setMap(wmap);

        //		setBackground(Color.black);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    public static Font getBaseFont()
    {
        return baseFont;
    }

    /**
     * Set the font size used to display the characters.
     * <p>
     * Any old transform applied/font size is lost.
     *
     * @param size
     */
    public void setFontSize(int size)
    {
        float scale = size * 1.0F / baseFont.getSize2D();

        setTileTransform(AffineTransform.getScaleInstance(scale, scale));
    }

    /**
     * Force the backbuffer to be recreated, all prerendered images
     * to be removed, and everything created from scratch.
     */
    public void forceRedrawAll()
    {
        recreateBackBuffer();

        rendered.clear();

        doReDraw(null);

        invalidate();
    }

    @Override
    public int getWidth()
    {
        return size.width;
    }

    ;

    @Override
    public int getHeight()
    {
        return size.height;
    }

    ;

    /**
     * Create a backbuffer image which can display all the characters
     */
    private void recreateBackBuffer()
    {
        this.size = new Dimension(fontW * cacheW, fontH * cacheH + 2);
        backImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
                                       .createCompatibleImage(this.size.width,
                                                              this.size.height,
                                                              Transparency.TRANSLUCENT);

        //		setIcon(new ImageIcon(backImage));
    }

    /**
     * set/update the 2D array of characters.
     * <p>
     * Only chacaters which changed are redrawn, thus making the
     * function fast.
     *
     * @param wmap
     */
    public void setMap(ICharDisplayable wmap)
    {
        if (wmap.getWidth() != cacheW || wmap.getHeight() != cacheH)
        {
            cacheW = wmap.getWidth();
            cacheH = wmap.getHeight();
            cacheChars = new CharVisual[cacheW * cacheH];

            recreateBackBuffer();
            invalidate();
        }
        boolean[] dirty = new boolean[cacheW * cacheH];
        Arrays.fill(dirty, false);

        int idx = 0;
        for (int i = 0; i < cacheW; i++)
            for (int j = 0; j < cacheH; j++)
            {
                if (wmap.getCharAt(i, j) != cacheChars[idx])
                {
                    cacheChars[idx] = (CharVisual) wmap.getCharAt(i, j);
                    dirty[idx] = true;
                }
                idx++;
            }

        doReDraw(dirty);

        repaint();
    }

    /**
     * Redraw the whole screen, given a flattened array of which
     * tiles are displayed dirty.
     * If dirty is null, all characters are redrawn.
     *
     * @param dirty
     */
    private void doReDraw(boolean[] dirty)
    {
        Graphics2D g = backImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setFont(f);

        //		g.setColor(Color.BLACK);
        //		g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        Color transparent = new Color(0, 0, 0, 0);
        int   idx         = 0;
        for (int i = 0; i < cacheW; i++)
            for (int j = 0; j < cacheH; j++)
            {
                if (dirty == null || dirty[idx])
                {
                    //					g.setColor(Color.black);
                    //					g.fillRect(i*fontW, offsety+j*fontH-fontH+1, fontW, fontH);

                    CharVisual cv = cacheChars[idx];
                    //					char c=' '; Color col=transparent;
                    //					if(cv!=null)
                    //					{
                    //						c=((CharVisual)cv).disp;
                    //						col=((CharVisual)cv).col;
                    ////						if(cv.font!=null)
                    ////							g.setFont(cv.font);
                    ////						else
                    ////							g.setFont(f);
                    //					}

                    if (cv != null)
                    {
                        Image im = rendered.get(cv);
                        if (im == null)
                        {
                            im = createRenderedImage(cv);
                            rendered.put(cv, im);
                        }

                        g.drawImage(im, i * fontW, j * fontH, this);
                    }
                    else
                    {
                        g.setColor(transparent);
                        g.fillRect(i * fontW, j * fontH, fontW, fontH);
                    }
                    //					g.setClip(i*fontW, j*fontH, fontW, fontH);
                    //					g.setColor(col);
                    //					g.drawString(Character.toString(c), i*fontW, offsety+j*fontH);
                    //					g.setClip(null);
                }
                idx++;
            }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return size;
    }

    @Override
    public Dimension getMinimumSize()
    {
        return size;
    }

    @Override
    public Dimension getMaximumSize()
    {
        return size;
    }

    /**
     * draw the visible part of the backbuffer, while centering it.
     * <p>
     * Cannot give this task to setIcon() since I need to know the offset of the
     * backImage, to know which tile lies under the mouse, etc.
     */
    public void paint(Graphics g)
    {
        Rectangle r1;//=g.getClip().getBounds();
        Container p = getParent();
        if (p instanceof JViewport)
        {
            JViewport vp = (JViewport) p;
            r1 = vp.getVisibleRect();
        }
        else
        {
            r1 = p.getBounds();
        }
        Rectangle r = g.getClip().getBounds();

        int iw = backImage.getWidth(), ih = backImage.getHeight();
        if (r.width == r1.width && r.height == r1.height)
        {
            imageStartX = imageStartY = 0;
            if (iw < r1.width)
                imageStartX = (r1.width - iw) / 2;
            if (ih < r1.height)
                imageStartY = (r1.height - ih) / 2;
        }

        Graphics2D g2   = (Graphics2D) g;
        Composite  oldC = null;
        if (composite != null)
        {
            oldC = g2.getComposite();
            g2.setComposite(composite);
        }
        g.drawImage(backImage, r.x, r.y, r.x + r.width, r.y + r.height, r.x - imageStartX, r.y - imageStartY,
                    //negative start coords for image seem to be automatically handled
                    r.x - imageStartX + r.width, r.y - imageStartY + r.height, this);

        if (composite != null)
            g2.setComposite(oldC);
    }

    /**
     * export the current display to disk.
     *
     * @param file
     * @param format
     */
    public void writeImage(File file, String format)
    {
        try
        {
            ImageIO.write(backImage, format, file);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Given x, y as a MouseEvent provides, returns which cell/tile is it on
     *
     * @param x
     * @param y
     * @return
     */
    public Point2I screenToCellCoords(int x, int y)
    {
        return new Point2I((x - imageStartX) / fontW, (y - imageStartY) / fontH);
    }

    /**
     * Used to implement tile tooltip display.
     */
    @Override
    public String getToolTipText(MouseEvent e)
    {
        return (infoProvider == null) ?
                   null :
                   infoProvider.getTileInfo((e.getX() - imageStartX) / fontW, (e.getY() - imageStartY) / fontH);
    }

    /**
     * Used to implement tile tooltip display.
     */
    @Override
    public Point getToolTipLocation(MouseEvent e)
    {
        Point p = e.getPoint();//returns a new point
        p.x = p.x - (p.x % fontW) + fontW;
        p.y = p.y - (p.y % fontH) + fontH;

        return p;
    }

    /**
     * Use multiline tooltips
     */
    public JToolTip createToolTip()
    {
        MultiLineToolTip tip = new MultiLineToolTip();
        //      tip.setDelimiter(",");
        tip.setComponent(this);
        return tip;
    }

    /**
     * Call this function to set the object that provides tile
     * information.
     * A null ITileInfoProvider disables tile information display.
     *
     * @param infoProvider
     */
    public void setInfoProvider(ITileInfoProvider infoProvider)
    {
        if (infoProvider == null)
            ToolTipManager.sharedInstance().unregisterComponent(this);
        else
            ToolTipManager.sharedInstance().registerComponent(this);
        this.infoProvider = infoProvider;
    }

    /**
     * Tries to bring the specified map location to the center of the view.
     * <p>
     * Useful to keep the player at the center of view, preventing off the
     * screen attacks.
     *
     * @param x
     * @param y
     */
    public void centerOn(int x, int y)
    {
        if (x < 0 || y < 0 || x >= cacheW || y >= cacheH)
        {
            return;//outside range
        }

        if (getParent() instanceof JViewport)
        {
            JViewport containing = (JViewport) getParent();
            int       cx         = x * fontW + fontW / 2;
            int       cy         = y * fontH + fontH / 2;

            Rectangle bounds = containing.getBounds();
            bounds.setLocation(cx - bounds.width / 2 + getX(), cy - bounds.height / 2 + getY());
            containing.scrollRectToVisible(bounds);
        }
    }

    /**
     * Supposed to report when a location(usually player char) is
     * close to the edge of the viewport, so that we can center it.
     * <p>
     * Problem: if x is close to edge, but also close to edge of map hence
     * cannot be centered, and y is not close to edge of viewport, then
     * this function is always true, which leads to always centering behavior
     * which is not what we are trying to do with this function
     *
     * @param x
     * @param y
     * @param percentage
     * @return
     */
    public boolean closeToEdge(int x, int y, int percentage)
    {
        if (getParent() instanceof JViewport)
        {
            JViewport containing = (JViewport) getParent();

            int cx = x * fontW + fontW / 2;
            int cy = y * fontH + fontH / 2;

            Rectangle bounds = containing.getBounds();
            System.out.println(bounds + " " + cx + " " + cy);

            if (cx - bounds.x < percentage * bounds.width / 100)
                return true;
            System.out.println(1);
            if (cy - bounds.y < percentage * bounds.height / 100)
                return true;
            System.out.println(2);
            if (bounds.x + bounds.width - cx < percentage * bounds.width / 100)
                return true;
            System.out.println(3);
            if (bounds.y + bounds.height - cy < percentage * bounds.height / 100)
                return true;
            System.out.println(4);
        }
        return false;
    }

    /**
     * @return how many characters are visible to the
     * user right now.
     */
    public Rectangle viewPortHoldsHowmany()
    {
        Rectangle rect = getParent().getBounds();//hopefully parent is a viewport
        rect.width /= fontW;
        rect.height /= fontH;

        return rect;
    }

    /**
     * Clears the overlay to be empty.
     */
    public void clearOverlay()
    {
        Graphics2D graphics = backImage.createGraphics();
        for (Point2I pt : overlay.keySet())
        {
            writeCharInImage(graphics, pt.x, pt.y, cacheChars[pt.x * cacheH + pt.y]);
        }
        overlay.clear();
    }

    /**
     * A special character to display at a particular location temporarily.
     *
     * @param p
     * @param v
     */
    public void setOverlay(Point2I p, CharVisual v)
    {
        writeCharInImage(backImage.createGraphics(), p.x, p.y, v);
        overlay.put(p, v);
    }

    /**
     * Write just one character in the backbuffer image.
     *
     * @param g
     * @param x
     * @param y
     * @param cv
     */
    private void writeCharInImage(Graphics2D g, int x, int y, CharVisual cv)
    {
        if (x < 0 || y < 0 || x >= cacheW || y >= cacheH)
            return;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(f);
        //		g.setColor(Color.black);
        //		g.fillRect(x*fontW, offsety+y*fontH-fontH+1, fontW, fontH);

        char  c   = ' ';
        Color col = Color.black;
        if (cv != null)
        {
            c = ((CharVisual) cv).disp;
            col = ((CharVisual) cv).col;
            if (cv.font != null)
                g.setFont(cv.font);
            else
                g.setFont(f);
        }

        Image im = rendered.get(cv);
        if (im == null)
        {
            im = createRenderedImage(cv);
            rendered.put(cv, im);
        }

        g.drawImage(im, x * fontW, y * fontH, this);

        //		g.setClip(x*fontW, offsety+y*fontH, fontW, fontH);
        //		g.setColor(col);
        //		g.drawString(Character.toString(c), x*fontW, offsety+y*fontH);
    }

    /**
     * Creates a rendered version of the CharVisual given.
     *
     * @param cv
     * @return
     */
    protected Image createRenderedImage(CharVisual cv)
    {
        BufferedImage im = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                                              .getDefaultConfiguration().createCompatibleImage(fontW, fontH);

        Graphics2D g2 = im.createGraphics();
        g2.setColor(cv.bgCol);
        g2.fillRect(0, 0, fontW, fontH);
        if (cv.font != null)
            g2.setFont(cv.font);
        else
            g2.setFont(baseFont);

        //		FontMetrics fm=getFontMetrics(getFont());
        //		LineMetrics lm = fm.getLineMetrics(new char[]{cv.disp}, 0, 1, g2);
        //		System.out.println(lm.get);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.transform(currentTransform);
        g2.setColor(cv.col);
        g2.drawString(Character.toString(cv.disp), 0, offsety - 1);
        g2.dispose();
        return im;
    }

    public AffineTransform getTileTransform()
    {
        return currentTransform;
    }

    /**
     * Apply a new transform to all characters displayed.
     */
    public void setTileTransform(AffineTransform transform)
    {
        currentTransform = transform;
        f = baseFont.deriveFont(currentTransform);
        FontMetrics fm = getFontMetrics(f);

        fontW = fm.getMaxAdvance(); //hack
        fontH = fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading();
        //		System.out.println("Size "+fontW+" "+fontH);
        //		offsety=fm.getMaxAscent();

        forceRedrawAll();
    }

    public Point2I getTileCoords(Point loc)
    {
        int x = (int) (loc.getX() / fontW);
        int y = (int) (loc.getY() / fontH);

        return new Point2I(x, y);
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return getScrollableUnitIncrement(visibleRect, orientation, direction) * 10;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        if (orientation == SwingConstants.HORIZONTAL)
            return fontW;
        else
            return fontH;
    }
}

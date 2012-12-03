package asciiPanel;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.screens.ColorUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This simulates a code page 437 ASCII terminal display.
 * @author Trystan Spangler
 */
public class AsciiPanel extends JPanel {
	private static final long serialVersionUID = -4167851861147593092L;

    /**
     * The color black (pure black).
     */
    public static Color black = new Color(0, 0, 0);

    /**
     * The color red.
     */
    public static Color red = new Color(128, 0, 0);

    public static Color brown =  new Color(0x994444);

    /**
     * The color green.
     */
    public static Color green = new Color(0, 128, 0);

    /**
     * The color yellow.
     */
    public static Color yellow = new Color(128, 128, 0);

    /**
     * The color blue.
     */
    public static Color blue = new Color(0, 0, 128);

    /**
     * The color magenta.
     */
    public static Color magenta = new Color(128, 0, 128);

    /**
     * The color cyan.
     */
    public static Color cyan = new Color(0, 128, 128);

    /**
     * The color white (light gray).
     */
    public static Color white = new Color(192, 192, 192);

    /**
     * A brighter black (dark gray).
     */
    public static Color brightBlack = new Color(128, 128, 128);

    /**
     * A brighter red.
     */
    public static Color brightRed = new Color(255, 0, 0);

    /**
     * A brighter green.
     */
    public static Color brightGreen = new Color(0, 255, 0);

    /**
     * A brighter yellow.
     */
    public static Color brightYellow = new Color(255, 255, 0);

    /**
     * A brighter blue.
     */
    public static Color brightBlue = new Color(0, 0, 255);

    /**
     * A brighter magenta.
     */
    public static Color brightMagenta = new Color(255, 0, 255);

    /**
     * A brighter cyan.
     */
    public static Color brightCyan = new Color(0, 255, 255);
    
    /**
     * A brighter white (pure white).
     */
    public static Color brightWhite = new Color(255, 255, 255);


    public final Point screenTL = PointUtil.POINT_ORIGIN;
    public final Point screenBR;
    private final Point charTL = screenTL;
    private final Point charBr = new Point(18,18);
    private Color defaultBackgroundColor;
    private Color defaultForegroundColor;
    private Point cursor;
    private static final int GLYPH_SIZE = 256;
    private Map<ForeGroundBackGround, BufferedImage[]> glyphs;
    private Map<ForeGroundBackGround, BufferedImage[]> text;
    private BufferedImage humanoidsSprite;
    private BufferedImage[] humanoids;
    private BufferedImage crittersSprite;
    private BufferedImage[] critters;
    private Point mousePosition = PointUtil.POINT_ORIGIN;
    private int[][] entities;
    private char[][] backgroundChars;
    private char[][] textChars;
    private Color[][] backgroundColors;
    private Color[][] foregroundColors;

    private char[][] popupChars;
    private char[][] popupText;

    private Color mouseColor = Color.YELLOW;

    /**
     * Gets the height, in pixels, of a character.
     * @return
     */
    public Point charBr(){
        return charBr;
    }

    /**
     * Class constructor.
     * Default size is 80x24.
     */
    public AsciiPanel() {
        this(80, 24);
    }

    /**
     * Class constructor specifying the width and height in characters.
     * @param width
     * @param height
     */
    public AsciiPanel(int width, int height) {
        super();

        if (width < 1)
            throw new IllegalArgumentException("width " + width + " must be greater than 0." );

        if (height < 1)
            throw new IllegalArgumentException("height " + height + " must be greater than 0." );

        screenBR = new Point(width, height);
        setPreferredSize(new Dimension(charBr.x() * width, charBr.y() * height));

        defaultBackgroundColor = black;
        defaultForegroundColor = white;

        backgroundChars = new char[width][height];
        textChars = new char[width][height];
        backgroundColors = new Color[width][height];
        foregroundColors = new Color[width][height];

        popupChars = new char[width][height];
        popupText = new char[width][height];

        glyphs = new HashMap<ForeGroundBackGround, BufferedImage[]>();
        text = new HashMap<ForeGroundBackGround, BufferedImage[]>();

        entities= new int[width][height];
        humanoids = new BufferedImage[144];
        critters = new BufferedImage[768];
        loadCharacters();

        AsciiPanel.this.clear();
    }
    
    @Override
    public void update(Graphics g) {
         paint(g); 
    } 

    @Override
    public void paint(Graphics g) {
        if (g == null)
            throw new NullPointerException();
        Graphics2D g2d = (Graphics2D)g.create();
        Composite old = g2d.getComposite();

        for (int x = 0; x < screenBR.x(); x++) {
            for (int y = 0; y < screenBR.y(); y++) {
                Color fg = foregroundColors[x][y];
                Color bg = backgroundColors[x][y];
                ForeGroundBackGround bgfg = new ForeGroundBackGround(fg, bg);
                Point translate = new Point(x,y).multiply(charBr);
                g2d.setComposite(old);
                g2d.fillRect(translate.x(), translate.y(), charBr.x(), charBr.y());
                if (!glyphs.containsKey(bgfg)){
                    loadGlyphs(fg, bg);
                }
                if (!text.containsKey(bgfg)){
                    loadText(fg, bg);
                }
                if (textChars[x][y] != 0){
                    g2d.drawImage(text.get(bgfg)[textChars[x][y]], translate.x(), translate.y(), null);
                    textChars[x][y] =0;
                } else {
                    g2d.drawImage(glyphs.get(bgfg)[backgroundChars[x][y]], translate.x(), translate.y(), null);
                }
                if (entities[x][y] != 0){
                    if (entities[x][y] > humanoids.length){
                        g.drawImage(critters[entities[x][y] - humanoids.length], translate.x(), translate.y(), null);
                    } else {
                        g.drawImage(humanoids[entities[x][y]], translate.x(), translate.y(), null);
                    }
                    entities[x][y] = 0;
                }
                if (popupChars[x][y] != 0){
                    g2d.drawImage(glyphs.get(bgfg)[popupChars[x][y]], translate.x(), translate.y(), null);
                    popupChars[x][y] = 0;
                }
                if (popupText[x][y] != 0){
                    g2d.drawImage(text.get(bgfg)[popupText[x][y]], translate.x(), translate.y(), null);
                    popupText[x][y] = 0;
                }

            }
        }
        if (mousePosition != null){
            g2d = (Graphics2D)g2d.create();
            g2d.setColor(mouseColor);
            Point mouseTrans = mousePosition.multiply(charBr);
            g2d.drawRect(mouseTrans.x(), mouseTrans.y(), charBr.x(), charBr.y());
            mousePosition = null;
        }
        g2d.dispose();
    }

    private BufferedImage toCompatibleImage(BufferedImage image)
    {
        // obtain the current system graphical settings
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        // image is not optimized, so create a new image that is
        BufferedImage new_image = gfx_config.createCompatibleImage(
                image.getWidth(), image.getHeight(), image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        Graphics2D g2d = (Graphics2D) new_image.getGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return new_image;
    }
    
    private void loadCharacters(){
        try {
            humanoidsSprite = ImageIO.read(AsciiPanel.class.getResource("humanoids2.png"));
            crittersSprite = ImageIO.read(AsciiPanel.class.getResource("critters2.png"));
        } catch (IOException e) {
            System.err.println("loadCharacters() " + e.getMessage());
        }
        for (int i = 0; i < 144; i++){
            int sx = (i % 12) * charBr.x();
            int sy = (i / 12) * charBr.y();
            humanoids[i] = new BufferedImage(charBr.x(), charBr.y(), BufferedImage.TYPE_INT_ARGB);
            humanoids[i].getGraphics().drawImage(humanoidsSprite, 0,0, charBr.x(), charBr.y(), sx, sy, sx + charBr.x(), sy + charBr.y(), null);
            humanoids[i] = toCompatibleImage(humanoids[i]);
        }
        for (int i = 0; i < 736; i++){
            int sx = (i % 32) * charBr.x();
            int sy = (i / 32) * charBr.y();
            critters[i] = new BufferedImage(charBr.x(), charBr.y(), BufferedImage.TYPE_INT_ARGB);
            critters[i].getGraphics().drawImage(crittersSprite, 0,0, charBr.x(), charBr.y(), sx, sy, sx + charBr.x(), sy + charBr.y(), null);
            critters[i] = toCompatibleImage(critters[i]);
        }

    }

    private void loadGlyphs(Color fg, Color bg) {
        Image glyphSprite = null;
        try {
            glyphSprite = ImageIO.read(AsciiPanel.class.getResource("ironhand.png"));
        } catch (IOException e) {
            System.err.println("loadGlyphs(): " + e.getMessage());
        }
        BufferedImage[] imgs = new BufferedImage[GLYPH_SIZE];

        for (int i = 0; i < GLYPH_SIZE; i++) {
            int sx = (i % 16) * charBr.x();
            int sy = (i / 16) * charBr.y();

            imgs[i] = new BufferedImage(charBr.x(), charBr.y(), BufferedImage.TYPE_INT_ARGB);
            imgs[i].getGraphics().drawImage(glyphSprite, 0, 0, charBr.x(), charBr.y(), sx, sy, sx + charBr.x(), sy + charBr.y(), null);
            Image img2 = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imgs[i].getSource(), new MixerFilter(fg, bg)));
            imgs[i].getGraphics().drawImage(img2, 0,0, null);
            imgs[i] = toCompatibleImage(imgs[i]);
        }
        glyphs.put(new ForeGroundBackGround(fg, bg), imgs);
    }

    private void loadText(Color fg, Color bg) {
        Image textSprite = null;
        try {
            textSprite = ImageIO.read(AsciiPanel.class.getResource("text.png"));
        } catch (IOException e) {
            System.err.println("loadText(): " + e.getMessage());
        }
        BufferedImage[] imgs = new BufferedImage[GLYPH_SIZE];

        for (int i = 0; i < GLYPH_SIZE; i++) {
            int sx = (i % 16) * charBr.x();
            int sy = (i / 16) * charBr.y();

            imgs[i] = new BufferedImage(charBr.x(), charBr.y(), BufferedImage.TYPE_INT_ARGB);
            imgs[i].getGraphics().drawImage(textSprite, 0, 0, charBr.x(), charBr.y(), sx, sy, sx + charBr.x(), sy + charBr.y(), null);
            Image img2 = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imgs[i].getSource(), new MixerFilter(fg, bg)));
            imgs[i].getGraphics().drawImage(img2, 0,0, null);
            imgs[i] = toCompatibleImage(imgs[i]);
        }
        text.put(new ForeGroundBackGround(fg, bg), imgs);
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear() {
        return clear(' ', screenTL, screenBR, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Clear the section of the screen with the specified character and whatever the specified foreground and background colors are.
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(char character, Point topLeft, Point bottomRight, Color foreground, Color background) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );
        if (!topLeft.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("top left corner not within range of %d %d and %d %d",
                    screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        };

        if (bottomRight.x() < 1 || bottomRight.y() < 1) {
            throw new IllegalArgumentException("bottom right corner cannot be zero");
        }

        for (int xo = topLeft.x(); xo < bottomRight.x(); xo++) {
            for (int yo = topLeft.y(); yo < bottomRight.y(); yo++) {
                write(character, new Point(xo,yo), foreground, background);
            }
        }
        return this;
    }

    /**
     * Write a character to the cursor's position with the specified foreground color.
     * This updates the cursor's position but not the default foreground color.
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, Color foreground) {
        return write(character, cursor, foreground, defaultBackgroundColor);
    }

    /**
     * Write a character to the specified position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, Point position, Color foreground, Color background) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("WritePosition %d %d not within range of %d %d and %d %d",
                    screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        };
        if (foreground == null) foreground = defaultForegroundColor;
        if (background == null) background = defaultBackgroundColor;

        backgroundChars[position.x()][position.y()] = character;
        foregroundColors[position.x()][position.y()] = foreground;
        backgroundColors[position.x()][position.y()] = background;
        cursor= position.add(new Point(1,0));
        return this;
    }

    private AsciiPanel writeTextChar(char character, Point position, Color foreground, Color background) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("WritePosition %d %d not within range of %d %d and %d %d",
                    screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        }
        if (foreground == null) foreground = defaultForegroundColor;
        if (background == null) background = defaultBackgroundColor;

        textChars[position.x()][position.y()] = character;
        foregroundColors[position.x()][position.y()] = foreground;
        backgroundColors[position.x()][position.y()] = background;
        cursor= position.add(new Point(1,0));
        return this;
    }

    public AsciiPanel writeHumanoid(int catPosition, Point position, Tile background) {

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("WritePosition %d %d not within range of %d %d and %d %d",
                    screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        }

        entities[position.x()][position.y()] = catPosition;
        foregroundColors[position.x()][position.y()] = background.colorFG();
        backgroundColors[position.x()][position.y()] = background.colorBG();
        cursor = position.add(new Point(1,0));
        return this;
    }

    public AsciiPanel writePopup(char character, Point position, Color fg, Color bg){
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("WritePosition %d %d not within range of %d %d and %d %d",
                    screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        };
        if (fg == null) fg = defaultForegroundColor;
        if (bg == null) bg = defaultBackgroundColor;

        popupChars[position.x()][position.y()] = character;
        foregroundColors[position.x()][position.y()] = fg;
        backgroundColors[position.x()][position.y()] = bg;
        cursor= position.add(new Point(1,0));
        return this;
    }

    private AsciiPanel writePopupTextChar(char character, Point position, Color fg, Color bg){
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("WritePosition %d %d not within range of %d %d and %d %d",
                    position.x(), position.y(), screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        }
        if (fg == null) fg = defaultForegroundColor;
        if (bg == null) bg = defaultBackgroundColor;

        popupText[position.x()][position.y()] = character;
        foregroundColors[position.x()][position.y()] = fg;
        backgroundColors[position.x()][position.y()] = bg;
        cursor= position.add(new Point(1,0));
        return this;
    }

    /**
     * Write a string to the specified position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param string     the string to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writePopup(String string, Point position, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (position.x() + string.length() > screenBR.x())
            throw new IllegalArgumentException("String too big to fit on terminal.  " );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("Write start position %d %d not within range of %d %d and %d %d",
                    position.x(), position.y(), screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        };

        if (foreground == null)
            foreground = defaultForegroundColor;

        if (background == null)
            background = defaultBackgroundColor;

        for (int i = 0; i < string.length(); i++) {
            writePopup(string.charAt(i), position.add(new Point(i,0)), foreground, background);
        }
        return this;
    }


    /**
     * Write a string to the specified position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param string     the string to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writePopupText(String string, Point position, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (position.x() + string.length() > screenBR.x())
            throw new IllegalArgumentException("String too big to fit on terminal.  " );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("Write start position %d %d not within range of %d %d and %d %d",
                    position.x(), position.y(), screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        };

        if (foreground == null)
            foreground = defaultForegroundColor;

        if (background == null)
            background = defaultBackgroundColor;

        for (int i = 0; i < string.length(); i++) {
            writePopupTextChar(string.charAt(i), position.add(new Point(i,0)), foreground, background);
        }
        return this;
    }

    public AsciiPanel writeTile(Tile tile, Point viewPort) {
        return write(tile.glyph(), viewPort, tile.colorFG(), tile.colorBG());
    }


    public AsciiPanel writeDarkTile(Tile tile, Point viewPort, int amount) {
        return write(tile.glyph(), viewPort, ColorUtil.darken(tile.colorFG(), amount), ColorUtil.darken(tile.colorBG(), amount));
    }

    /**
     * Write a string to the specified position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param string     the string to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string, Point position, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (position.x() + string.length() > screenBR.x())
            throw new IllegalArgumentException("String too big to fit on terminal.  " );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("Write start position %d %d not within range of %d %d and %d %d",
                    position.x(), position.y(), screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        }

        if (foreground == null)
            foreground = defaultForegroundColor;

        if (background == null)
            background = defaultBackgroundColor;

        for (int i = 0; i < string.length(); i++) {
            write(string.charAt(i), position.add(new Point(i,0)), foreground, background);
        }
        return this;
    }

    /**
     * Write a string to the specified position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param string     the string to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writeText(String string, Point position, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (position.x() + string.length() > screenBR.x())
            throw new IllegalArgumentException("String too big to fit on terminal.  " );

        if (!position.withinRect(this.screenTL, screenBR)) {
            throw new IllegalArgumentException(String.format("Write start position %d %d not within range of %d %d and %d %d",
                    position.x(), position.y(), screenTL.x(), screenTL.y(), screenBR.x(), screenBR.y()));
        }

        if (foreground == null)
            foreground = defaultForegroundColor;

        if (background == null)
            background = defaultBackgroundColor;

        for (int i = 0; i < string.length(); i++) {
            writeTextChar(string.charAt(i), position.add(new Point(i, 0)), foreground, background);
        }
        return this;
    }

    /**
     * Write a string to the center of the panel at the specified y position.
     * This updates the cursor's position.
     * @param string     the string to write
     * @param y          the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writeCenter(String string, int y) {
        int x = (screenBR.x() - string.length()) / 2;

        return write(string, new Point(x,y), defaultForegroundColor, defaultBackgroundColor);
    }

    public void withEachTile(TileTransformer transformer){
		withEachTile(screenTL, screenBR, transformer);
    }
    
    public void withEachTile(Point topLeft, Point bottomRight, TileTransformer transformer){
		AsciiCharacterData data = new AsciiCharacterData();
		
    	for (int x0 = topLeft.x(); x0 < bottomRight.x(); x0++)
    	for (int y0 = topLeft.y(); y0 < bottomRight.y(); y0++){
    		Point current = new Point(x0, y0);
    		if (current.withinRect(screenTL, screenBR))
    			continue;
    		
    		data.character = backgroundChars[x0][y0];
    		data.foregroundColor = foregroundColors[x0][y0];
    		data.backgroundColor = backgroundColors[x0][y0];
    		
    		transformer.transformTile(x0,y0, data);
    		
    		backgroundChars[x0][y0] = data.character;
    		foregroundColors[x0][y0] = data.foregroundColor;
    		backgroundColors[x0][y0] = data.backgroundColor;
    	}
    }

    public void highlight(Point mousePos, Color color) {
        this.mousePosition = mousePos;
        this.mouseColor = color;
    }
}

package asciiPanel;

import com.sun.xml.internal.bind.v2.model.core.MaybeElement;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
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

    private int widthInCharacters;
    private int heightInCharacters;
    private int charWidth = 18;
    private int charHeight = 18;
    private Color defaultBackgroundColor;
    private Color defaultForegroundColor;
    private int cursorX;
    private int cursorY;
    private static final int GLYPH_SIZE = 256;
    private Map<BgFg, BufferedImage[]> glyphs;
    private BufferedImage humanoidsSprite;
    private BufferedImage[] humanoids;
    private int[][] entities;
    private char[][] chars;
    private Color[][] backgroundColors;
    private Color[][] foregroundColors;

    /**
     * Gets the height, in pixels, of a character.
     * @return
     */
    public int getCharHeight() {
        return charHeight;
    }

    /**
     * Gets the width, in pixels, of a character.
     * @return
     */
    public int getCharWidth() {
        return charWidth;
    }

    /**
     * Gets the height in characters.
     * A standard terminal is 24 characters high.
     * @return
     */
    public int getHeightInCharacters() {
        return heightInCharacters;
    }

    /**
     * Gets the width in characters.
     * A standard terminal is 80 characters wide.
     * @return
     */
    public int getWidthInCharacters() {
        return widthInCharacters;
    }

    /**
     * Gets the distance from the left new text will be written to.
     * @return
     */
    public int getCursorX() {
        return cursorX;
    }

    /**
     * Sets the distance from the left new text will be written to.
     * This should be equal to or greater than 0 and less than the the width in characters.
     * @param cursorX the distance from the left new text should be written to
     */
    public void setCursorX(int cursorX) {
        if (cursorX < 0 || cursorX >= widthInCharacters)
            throw new IllegalArgumentException("cursorX " + cursorX + " must be within range [0," + widthInCharacters + ")." );

        this.cursorX = cursorX;
    }

    /**
     * Gets the distance from the top new text will be written to.
     * @return
     */
    public int getCursorY() {
        return cursorY;
    }

    /**
     * Sets the distance from the top new text will be written to.
     * This should be equal to or greater than 0 and less than the the height in characters.
     * @param cursorY the distance from the top new text should be written to
     */
    public void setCursorY(int cursorY) {
        if (cursorY < 0 || cursorY >= heightInCharacters)
            throw new IllegalArgumentException("cursorY " + cursorY + " must be within range [0," + heightInCharacters + ")." );

        this.cursorY = cursorY;
    }

    /**
     * Sets the x and y position of where new text will be written to. The origin (0,0) is the upper left corner.
     * The x should be equal to or greater than 0 and less than the the width in characters.
     * The y should be equal to or greater than 0 and less than the the height in characters.
     * @param x the distance from the left new text should be written to
     * @param y the distance from the top new text should be written to
     */
    public void setCursorPosition(int x, int y) {
        setCursorX(x);
        setCursorY(y);
    }

    /**
     * Gets the default background color that is used when writing new text.
     * @return
     */
    public Color getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    /**
     * Sets the default background color that is used when writing new text.
     * @param defaultBackgroundColor
     */
    public void setDefaultBackgroundColor(Color defaultBackgroundColor) {
        if (defaultBackgroundColor == null)
            throw new NullPointerException("defaultBackgroundColor must not be null.");

        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    /**
     * Gets the default foreground color that is used when writing new text.
     * @return
     */
    public Color getDefaultForegroundColor() {
        return defaultForegroundColor;
    }

    /**
     * Sets the default foreground color that is used when writing new text.
     * @param defaultForegroundColor
     */
    public void setDefaultForegroundColor(Color defaultForegroundColor) {
        if (defaultForegroundColor == null)
            throw new NullPointerException("defaultForegroundColor must not be null.");

        this.defaultForegroundColor = defaultForegroundColor;
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

        widthInCharacters = width;
        heightInCharacters = height;
        setPreferredSize(new Dimension(charWidth * widthInCharacters, charHeight * heightInCharacters));

        defaultBackgroundColor = black;
        defaultForegroundColor = white;

        chars = new char[widthInCharacters][heightInCharacters];
        backgroundColors = new Color[widthInCharacters][heightInCharacters];
        foregroundColors = new Color[widthInCharacters][heightInCharacters];

        glyphs = new HashMap<BgFg, BufferedImage[]>();

        entities= new int[widthInCharacters][heightInCharacters];
        humanoids = new BufferedImage[144];
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

        for (int x = 0; x < widthInCharacters; x++) {
            for (int y = 0; y < heightInCharacters; y++) {
                Color bg = backgroundColors[x][y];
                Color fg = foregroundColors[x][y];
                BgFg bgfg = new BgFg(fg, bg);
                g2d.setColor(bg);
                g2d.setComposite(old);
                g2d.fillRect(x * charWidth, y * charHeight, charWidth, charHeight);
                if (!glyphs.containsKey(bgfg)){
                    loadGlyphs(fg, bg);
                }
                g2d.drawImage(glyphs.get(bgfg)[chars[x][y]], x * charWidth, y * charHeight, null);
                if (entities[x][y] != 0){
                    g.drawImage(humanoids[entities[x][y]], x * charWidth, y * charHeight, null);
                    entities[x][y] = 0;
                }
            }
        }
        g2d.dispose();
    }

    private BufferedImage toCompatibleImage(BufferedImage image)
    {
        // obtain the current system graphical settings
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

//        /*
//         * if image is already compatible and optimized for current system
//         * settings, simply return it
//         */
//        if (image.getColorModel().equals(gfx_config.getColorModel()))
//            return image;

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
        } catch (IOException e) {
            System.err.println("loadCharacters() " + e.getMessage());
        }
        for (int i = 0; i < 144; i++){
            int sx = (i % 12) * charWidth;
            int sy = (i / 12) * charWidth;
            humanoids[i] = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
            humanoids[i].getGraphics().drawImage(humanoidsSprite, 0,0, charWidth, charHeight, sx, sy, sx + charWidth, sy + charHeight, null);
            humanoids[i] = toCompatibleImage(humanoids[i]);
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
            int sx = (i % 16) * charWidth;
            int sy = (i / 16) * charHeight;

            imgs[i] = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
            imgs[i].getGraphics().drawImage(glyphSprite, 0, 0, charWidth, charHeight, sx, sy, sx + charWidth, sy + charHeight, null);
            Image img2 = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imgs[i].getSource(), new MixerFilter(fg, bg)));
            imgs[i].getGraphics().drawImage(img2, 0,0, null);
            imgs[i] = toCompatibleImage(imgs[i]);
        }
        glyphs.put(new BgFg(fg, bg), imgs);
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear() {
        return clear(' ', 0, 0, widthInCharacters, heightInCharacters, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Clear the entire screen with the specified character and whatever the default foreground and background colors are.
     * @param character  the character to write
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(char character) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        return clear(character, 0, 0, widthInCharacters, heightInCharacters, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Clear the entire screen with the specified character and whatever the specified foreground and background colors are.
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(char character, Color foreground, Color background) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        return clear(character, 0, 0, widthInCharacters, heightInCharacters, foreground, background);
    }

    /**
     * Clear the section of the screen with the specified character and whatever the default foreground and background colors are.
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param width      the height of the section to clear
     * @param height     the width of the section to clear
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(char character, int x, int y, int width, int height) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")." );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")." );

        if (width < 1)
            throw new IllegalArgumentException("width " + width + " must be greater than 0." );

        if (height < 1)
            throw new IllegalArgumentException("height " + height + " must be greater than 0." );

        if (x + width > widthInCharacters)
            throw new IllegalArgumentException("x + width " + (x + width) + " must be less than " + (widthInCharacters + 1) + "." );

        if (y + height > heightInCharacters)
            throw new IllegalArgumentException("y + height " + (y + height) + " must be less than " + (heightInCharacters + 1) + "." );


        return clear(character, x, y, width, height, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Clear the section of the screen with the specified character and whatever the specified foreground and background colors are.
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param width      the height of the section to clear
     * @param height     the width of the section to clear
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(char character, int x, int y, int width, int height, Color foreground, Color background) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")" );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        if (width < 1)
            throw new IllegalArgumentException("width " + width + " must be greater than 0." );

        if (height < 1)
            throw new IllegalArgumentException("height " + height + " must be greater than 0." );

        if (x + width > widthInCharacters)
            throw new IllegalArgumentException("x + width " + (x + width) + " must be less than " + (widthInCharacters + 1) + "." );

        if (y + height > heightInCharacters)
            throw new IllegalArgumentException("y + height " + (y + height) + " must be less than " + (heightInCharacters + 1) + "." );

        for (int xo = x; xo < x + width; xo++) {
            for (int yo = y; yo < y + height; yo++) {
                write(character, xo, yo, foreground, background);
            }
        }
        return this;
    }

    /**
     * Write a character to the cursor's position.
     * This updates the cursor's position.
     * @param character  the character to write
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        return write(character, cursorX, cursorY, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a character to the cursor's position with the specified foreground color.
     * This updates the cursor's position but not the default foreground color.
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, Color foreground) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        return write(character, cursorX, cursorY, foreground, defaultBackgroundColor);
    }

    /**
     * Write a character to the cursor's position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param character  the character to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, Color foreground, Color background) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        return write(character, cursorX, cursorY, foreground, background);
    }

    /**
     * Write a character to the specified position.
     * This updates the cursor's position.
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, int x, int y) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")" );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        return write(character, x, y, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a character to the specified position with the specified foreground color.
     * This updates the cursor's position but not the default foreground color.
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, int x, int y, Color foreground) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")" );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        return write(character, x, y, foreground, defaultBackgroundColor);
    }

    /**
     * Write a character to the specified position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, int x, int y, Color foreground, Color background) {
        if (character < 0 || character >= GLYPH_SIZE)
            throw new IllegalArgumentException("character " + character + " must be within range [0," + GLYPH_SIZE + "]." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")" );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        if (foreground == null) foreground = defaultForegroundColor;
        if (background == null) background = defaultBackgroundColor;

        chars[x][y] = character;
        foregroundColors[x][y] = foreground;
        backgroundColors[x][y] = background;
        cursorX = x + 1;
        cursorY = y;
        return this;
    }

    public AsciiPanel writeHumanoid(int position, int x, int y) {
        if (position < 0 || position >= humanoids.length)
            throw new IllegalArgumentException("position " + position + " must be within range [0," + GLYPH_SIZE + "]." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")" );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        entities[x][y] = position;
        foregroundColors[x][y] = defaultForegroundColor;
        backgroundColors[x][y] = defaultBackgroundColor;
        cursorX = x + 1;
        cursorY = y;
        return this;
    }

    /**
     * Write a string to the cursor's position.
     * This updates the cursor's position.
     * @param string     the string to write
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (cursorX + string.length() >= widthInCharacters)
            throw new IllegalArgumentException("cursorX + string.length() " + (cursorX + string.length()) + " must be less than " + widthInCharacters + "." );

        return write(string, cursorX, cursorY, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a string to the cursor's position with the specified foreground color.
     * This updates the cursor's position but not the default foreground color.
     * @param string     the string to write
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string, Color foreground) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (cursorX + string.length() >= widthInCharacters)
            throw new IllegalArgumentException("cursorX + string.length() " + (cursorX + string.length()) + " must be less than " + widthInCharacters + "." );

        return write(string, cursorX, cursorY, foreground, defaultBackgroundColor);
    }

    /**
     * Write a string to the cursor's position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param string     the string to write
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (cursorX + string.length() >= widthInCharacters)
            throw new IllegalArgumentException("cursorX + string.length() " + (cursorX + string.length()) + " must be less than " + widthInCharacters + "." );

        return write(string, cursorX, cursorY, foreground, background);
    }

    /**
     * Write a string to the specified position.
     * This updates the cursor's position.
     * @param string     the string to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string, int x, int y) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (x + string.length() >= widthInCharacters)
            throw new IllegalArgumentException("x + string.length() " + (x + string.length()) + " must be less than " + widthInCharacters + "." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")" );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        return write(string, x, y, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a string to the specified position with the specified foreground color.
     * This updates the cursor's position but not the default foreground color.
     * @param string     the string to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string, int x, int y, Color foreground) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (x + string.length() > widthInCharacters)
            throw new IllegalArgumentException("x + string.length() " + (x + string.length()) + " must be less than " + widthInCharacters + "." );

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")" );

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        return write(string, x, y, foreground, defaultBackgroundColor);
    }

    /**
     * Write a string to the specified position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param string     the string to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string, int x, int y, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null." );
        
        if (x + string.length() > widthInCharacters)
            throw new IllegalArgumentException("x + string.length() " + (x + string.length()) + " must be less than or equal to" + widthInCharacters + "." );

        if (x < 0 || x > widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")." );

        if (y < 0 || y >heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")." );

        if (foreground == null)
            foreground = defaultForegroundColor;

        if (background == null)
            background = defaultBackgroundColor;

        for (int i = 0; i < string.length(); i++) {
            write(string.charAt(i), x + i, y, foreground, background);
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
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (string.length() >= widthInCharacters)
            throw new IllegalArgumentException("string.length() " + string.length() + " must be less than " + widthInCharacters + "." );

        int x = (widthInCharacters - string.length()) / 2;

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        return write(string, x, y, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a string to the center of the panel at the specified y position with the specified foreground color.
     * This updates the cursor's position but not the default foreground color.
     * @param string     the string to write
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writeCenter(String string, int y, Color foreground) {
        if (string == null)
            throw new NullPointerException("string must not be null" );

        if (string.length() >= widthInCharacters)
            throw new IllegalArgumentException("string.length() " + string.length() + " must be less than " + widthInCharacters + "." );

        int x = (widthInCharacters - string.length()) / 2;

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")" );

        return write(string, x, y, foreground, defaultBackgroundColor);
    }

    /**
     * Write a string to the center of the panel at the specified y position with the specified foreground and background colors.
     * This updates the cursor's position but not the default foreground or background colors.
     * @param string     the string to write
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writeCenter(String string, int y, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null." );

        if (string.length() >= widthInCharacters)
            throw new IllegalArgumentException("string.length() " + string.length() + " must be less than " + widthInCharacters + "." );

        int x = (widthInCharacters - string.length()) / 2;
        
        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")." );

        if (foreground == null)
            foreground = defaultForegroundColor;

        if (background == null)
            background = defaultBackgroundColor;

        for (int i = 0; i < string.length(); i++) {
            write(string.charAt(i), x + i, y, foreground, background);
        }
        return this;
    }
    
    public void withEachTile(TileTransformer transformer){
		withEachTile(0, 0, widthInCharacters, heightInCharacters, transformer);
    }
    
    public void withEachTile(int left, int top, int width, int height, TileTransformer transformer){
		AsciiCharacterData data = new AsciiCharacterData();
		
    	for (int x0 = 0; x0 < width; x0++)
    	for (int y0 = 0; y0 < height; y0++){
    		int x = left + x0;
    		int y = top + y0;
    		
    		if (x < 0 || y < 0 || x >= widthInCharacters || y >= heightInCharacters)
    			continue;
    		
    		data.character = chars[x][y];
    		data.foregroundColor = foregroundColors[x][y];
    		data.backgroundColor = backgroundColors[x][y];
    		
    		transformer.transformTile(x, y, data);
    		
    		chars[x][y] = data.character;
    		foregroundColors[x][y] = data.foregroundColor;
    		backgroundColors[x][y] = data.backgroundColor;
    	}
    }
}

package asciiPanel;

import java.awt.*;
import java.awt.image.RGBImageFilter;

/**
 * User: ben
 * Date: 9/9/12
 * Time: 10:18 AM
 */
public class BGFilter extends RGBImageFilter {

    private Color bg;
    public BGFilter(Color bg){
        this.bg = bg;
    }
    @Override
    public int filterRGB(int x, int y, int rgb) {
        return bg.getRGB();
    }
}

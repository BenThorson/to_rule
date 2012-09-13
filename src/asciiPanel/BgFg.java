package asciiPanel;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/13/12
 * Time: 8:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class BgFg {

    Color bg;
    Color fg;

    public BgFg(Color fg, Color bg) {
        this.bg = bg;
        this.fg = fg;
    }

    public Color getBg() {
        return bg;
    }

    public Color getFg() {
        return fg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BgFg bgFg = (BgFg) o;

        if (!bg.equals(bgFg.bg)) return false;
        if (!fg.equals(bgFg.fg)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bg.hashCode();
        result = 31 * result + fg.hashCode();
        return result;
    }
}

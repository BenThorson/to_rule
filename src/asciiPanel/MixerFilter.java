package asciiPanel;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class MixerFilter extends RGBImageFilter
{
    private float fgRatioR;
    private float fgRatioG;
    private float fgRatioB;
    private float bgRatioR;
    private float bgRatioG;
    private float bgRatioB;

    Color bg;

    public MixerFilter(Color fg, Color bg)
    {
        fgRatioR = fg.getRed() / 255f;
        fgRatioG = fg.getGreen() / 255f;
        fgRatioB = fg.getBlue() / 255f;
        bgRatioR = bg.getRed() / 255f;
        bgRatioG = bg.getGreen() / 255f;
        bgRatioB = bg.getBlue() / 255f;

        this.bg = bg;

    }

    public int filterRGB(int x, int y, int rgb)
    {
        int b = rgb & 0xff;
        int g = (rgb >> 8)  & 0xff;
        int r = (rgb >> 16) & 0xff;
        int nr,ng,nb;
        if (rgb == 0){
            return bg.getRGB();
        }
        int a = (rgb >> 24) & 0xff;

        if (Math.abs(r - g) + Math.abs(r - b) >  20){
            return rgb;
        } else if (r > 0x3f){
            nr = (int) Math.round(r * fgRatioR);
            ng = (int) Math.round(g * fgRatioG);
            nb = (int) Math.round(b * fgRatioB);
        } else {
            nr = (int) Math.round(r * bgRatioR);
            ng = (int) Math.round(g * bgRatioG);
            nb = (int) Math.round(b * bgRatioB);
        }
//
//        if (a != 0xFF){
//            float aRatio = (0xff - a)/255f;
//            nr += (int) Math.round(r * aRatio * bgRatioR);
//            ng += (int) Math.round(g * aRatio * bgRatioG);
//            nb += (int) Math.round(b * aRatio * bgRatioB);
//        }

        if (r < 0) r = 0; else if (r > 255) r = 255;
        if (g < 0) g = 0; else if (g > 255) g = 255;
        if (b < 0) b = 0; else if (b > 255) b = 255;
        return (0xff000000) + (nr << 16) + (ng << 8) + nb;
    }
}
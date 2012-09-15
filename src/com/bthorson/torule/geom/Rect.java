package com.bthorson.torule.geom;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 1:24 AM
 */
public class Rect {

    public Point[][] pts;

    public Rect(Point ratio, int units, Point center){

        int start = (int)Math.sqrt(units);
        for (;start < units; start++){
            Point transform = scale(ratio.x(), ratio.y(), start, start, true);
            if (transform.x() * transform.y() > units) {
                buildPoints(transform, center);
            }
        }
    }

    public Rect(Point[][] pts) {
        this.pts = pts;
    }

    private void buildPoints(Point transform, Point center) {
        pts = new Point[transform.x()][transform.y()];

        Point topRight = new Point(center.x() - transform.x() / 2, center.y() - transform.y() / 2);
        for (int x = 0; x < transform.x(); x++){
            for (int y = 0; y < transform.y(); y++){
                pts[x][y] = new Point(x,y).add(topRight);
            }
        }

    }

    public Point[][] getPoints(){
        return pts;
    }

    public Point scale(int sX, int sY, int mX, int mY, boolean growBigger){
        int nw = mY * sX / sY;
        int nh = mX * sY / sX;

        if (nw == 0) nw = 1;
        if (nh == 0) nh = 1;

        if (growBigger ^ (nw >= mX)){
            return new Point(nw, mY);
        }
        return new Point(mX, nh);

    }


    public Rect add(Point point) {
        Point zzz[][] = new Point[pts.length][pts[0].length];
        for (int x = 0; x < pts.length; x++){
            for (int y = 0; y < pts[x].length; y++){
                zzz[x][y] = new Point(x,y).add(point);
            }
        }
        return new Rect(zzz);
    }
}

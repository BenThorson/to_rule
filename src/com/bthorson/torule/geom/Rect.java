package com.bthorson.torule.geom;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 1:24 AM
 */
public class Rect {

    private Point[] pts;
    private Point dimension;

    public Rect(Point ratio, int units){

        for (int start = 1;start <= units; start++){
            Point transform = scale(ratio.x(), ratio.y(), start, start, true);
            if (transform.x() * transform.y() >= units) {
                dimension = transform;
                buildPoints(transform, units);
                break;
            }
        }
    }

    public Rect(Point[] pts) {
        this.pts = pts;
    }

    private void buildPoints(Point transform, int units) {
        pts = new Point[units];
        for (int i = 0; i < units; i++){
                pts[i] = new Point(i % transform.x(),i / transform.x());
        }
        System.out.println("xx");
    }

    public Point[] getPoints(){
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
        Point zzz[] = new Point[pts.length];
        for (int x = 0; x < pts.length; x++){
            zzz[x] = pts[x].add(point);
        }
        return new Rect(zzz);
    }

    public synchronized void rotateLeft(){

        Point[] rotatedCoordinates = new Point[pts.length];
        Point origin = pts[pts.length / 2];

        for(int i = 0; i < rotatedCoordinates.length; i++){

            // Translates current coordinate to be relative to (0,0)
            Point translationCoordinate = pts[i].subtract(origin);

            // Java coordinates start at 0 and increase as a point moves down, so
            // multiply by -1 to reverse
            translationCoordinate = translationCoordinate.invertY();

            // Clone coordinates, so I can use translation coordinates
            // in upcoming calculation
            rotatedCoordinates[i] = translationCoordinate.cloneDeep();

            // May need to round results after rotation
            int tx = (int)Math.round(translationCoordinate.x() * Math.cos(Math.PI/2) - translationCoordinate.y() * Math.sin(Math.PI/2));
            int ty = (int)Math.round(translationCoordinate.x() * Math.sin(Math.PI/2) + translationCoordinate.y() * Math.cos(Math.PI/2));
            rotatedCoordinates[i] = new Point(tx, ty).invertY().add(origin);

        }
        pts = rotatedCoordinates;
    }

    public Point getDimension() {
        return dimension;
    }
}

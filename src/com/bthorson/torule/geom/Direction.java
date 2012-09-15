package com.bthorson.torule.geom;

/**
 * User: ben
 * Date: 9/14/12
 * Time: 10:57 PM
 */
public enum Direction {

    NORTH(0,-1){
        @Override
        public Direction opposite() {
            return SOUTH;
        }
    },
    NORTHEAST(1,-1){
        @Override
        public Direction opposite() {
            return SOUTHWEST;
        }
    },
    EAST(1,0){
        @Override
        public Direction opposite() {
            return WEST;
        }
    },
    SOUTHEAST(1,1){
        @Override
        public Direction opposite() {
            return NORTHWEST;
        }
    },
    SOUTH(0,1){
        @Override
        public Direction opposite() {
            return NORTH;
        }
    },
    SOUTHWEST(-1,1){
        @Override
        public Direction opposite() {
            return NORTHEAST;
        }
    },
    WEST(-1,0){
        @Override
        public Direction opposite() {
            return EAST;
        }
    },
    NORTHWEST(-1,-1){
        @Override
        public Direction opposite() {
            return SOUTHEAST;
        }
    };

    private Point pnt;

    private Direction(int x, int y) {
        pnt = new Point(x,y);
    }

    public abstract Direction opposite();

    public Point point(){
        return pnt;
    }

    public static Direction directionOf(Point delta) {
        for (Direction d : Direction.values()){
            if (d.pnt.equals(delta)){
                return d;
            }
        }
        return null;
    }
}

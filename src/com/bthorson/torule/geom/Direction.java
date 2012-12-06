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

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{NORTH_WEST,NORTH_EAST};
        }
    },
    NORTH_EAST(1,-1){
        @Override
        public Direction opposite() {
            return SOUTH_WEST;
        }

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{NORTH, EAST};
        }
    },
    EAST(1,0){
        @Override
        public Direction opposite() {
            return WEST;
        }

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{NORTH_EAST, SOUTH_EAST};
        }
    },
    SOUTH_EAST(1,1){
        @Override
        public Direction opposite() {
            return NORTH_WEST;
        }

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{SOUTH, EAST};
        }
    },
    SOUTH(0,1){
        @Override
        public Direction opposite() {
            return NORTH;
        }

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{SOUTH_EAST, SOUTH_WEST};
        }
    },
    SOUTH_WEST(-1,1){
        @Override
        public Direction opposite() {
            return NORTH_EAST;
        }

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{SOUTH, WEST};
        }
    },
    WEST(-1,0){
        @Override
        public Direction opposite() {
            return EAST;
        }

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{SOUTH_WEST, NORTH_WEST};
        }
    },
    NORTH_WEST(-1,-1){
        @Override
        public Direction opposite() {
            return SOUTH_EAST;
        }

        @Override
        public Direction[] neighboringDirections() {
            return new Direction[]{NORTH_WEST};
        }
    };

    private Point pnt;

    private Direction(int x, int y) {
        pnt = new Point(x,y);
    }

    public abstract Direction opposite();

    public abstract Direction[] neighboringDirections();
    
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

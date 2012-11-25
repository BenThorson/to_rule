package com.bthorson.torule.map;

import com.bthorson.torule.geom.Point;

/**
 * User: Ben Thorson
 * Date: 11/25/12
 * Time: 11:56 AM
 */
public class MapConstants {

    public static final int LOCAL_SIZE_X = 100;
    public static final int LOCAL_SIZE_Y = 100;

    public static final int REGION_X_IN_LOCALS = 10;
    public static final int REGION_Y_IN_LOCALS = 10;

    public static final int REGION_SIZE_X = LOCAL_SIZE_X * REGION_X_IN_LOCALS;
    public static final int REGION_SIZE_Y = LOCAL_SIZE_Y * REGION_Y_IN_LOCALS;

    public static final Point LOCAL_SIZE_POINT = new Point(LOCAL_SIZE_X, LOCAL_SIZE_Y);

}

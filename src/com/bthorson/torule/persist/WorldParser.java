package com.bthorson.torule.persist;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.LocalType;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.Tile;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 4:20 PM
 */
public class WorldParser {


    public Local[][] parse(BufferedReader bufferedReader) throws IOException {


        String line = bufferedReader.readLine();
        Local[][] locals = getLocal(line);
        while ((line = bufferedReader.readLine()) != null){
            String[] split = line.split(" ");
            String type = split[0];
            int x = Integer.parseInt(split[1]);
            int y = Integer.parseInt(split[2]);
            locals[x][y] = buildTile(type, new Point(x,y), bufferedReader);
        }
        return locals;
    }

    private Local buildTile(String type, Point point, BufferedReader bufferedReader) throws IOException {
        Tile[][] tiles = new Tile[MapConstants.LOCAL_SIZE_X][MapConstants.LOCAL_SIZE_Y];
        for (int x = 0;x < MapConstants.LOCAL_SIZE_X; x++){
            String line = bufferedReader.readLine();
            String[] split = line.trim().split(" ");
            for (int y = 0; y < MapConstants.LOCAL_SIZE_Y; y++) {
                tiles[x][y] = Tile.valueOf(Integer.parseInt(split[y]));
            }
        }
        Local local = new Local(point.multiply(MapConstants.LOCAL_SIZE_POINT), tiles);
        local.setType(LocalType.valueOf(type));
        return local;
    }

    private Local[][] getLocal(String line) {
        String[] split = line.split(" ");
        int x = Integer.parseInt(split[1]);
        int y = Integer.parseInt(split[2]);
        return new Local[x][y];
    }
}

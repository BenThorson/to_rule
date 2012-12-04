package com.bthorson.torule.persist;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.Local;
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


    public void parse(BufferedReader bufferedReader) throws IOException {


        String line = bufferedReader.readLine();
        Local[][] locals = getLocal(line);
        while ((line = bufferedReader.readLine()) != null){
            if (line.startsWith("L")) {
                String[] split = line.split(" ")[1].split(";");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                locals[x][y] = buildTile(new Point(x,y), bufferedReader);
            }
        }
    }

    private Local buildTile(Point point, BufferedReader bufferedReader) throws IOException {
        Tile[][] tiles = new Tile[MapConstants.LOCAL_SIZE_X][MapConstants.LOCAL_SIZE_Y];
        for (int x = 0;x < MapConstants.LOCAL_SIZE_X; x++){
            String line = line = bufferedReader.readLine();
            String[] split = line.trim().split(" ");
            for (int y = 0; y < MapConstants.LOCAL_SIZE_Y; y++) {
                tiles[x][y] = Tile.valueOf(Integer.parseInt(split[y]));
            }
        }
        return new Local(point, tiles);
    }

    private Local[][] getLocal(String line) {
        String[] split = line.split(" ");
        String[] huh = split[1].split(";");
        int x = Integer.parseInt(huh[0]);
        int y = Integer.parseInt(huh[1]);
        return new Local[x][y];
    }
}

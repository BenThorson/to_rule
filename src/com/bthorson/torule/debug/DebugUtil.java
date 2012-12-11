package com.bthorson.torule.debug;

import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.quest.ScriptedSpawn;
import com.bthorson.torule.worldgen.spawn.SpawnAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/8/12
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugUtil {




    public static void debugSpawnGoblin() {

        Player player = EntityManager.getInstance().getPlayer();
        List<ScriptedSpawn> spawns = new ArrayList<ScriptedSpawn>();
        ScriptedSpawn spawn = new ScriptedSpawn();
        spawn.setMin(5);
        spawn.setMax(5);
        spawn.setCreatureType("goblin");
        spawns.add(spawn);
        Point point = new Point(PointUtil.randomPoint(player.position().subtract(player.visionRadius()),
                player.position().add(player.visionRadius())));

        new SpawnAction().createCreatures(spawns, point);
    }

    public static void teleportPlayer() {
        Scanner scanner = new Scanner(System.in);
        Player player = EntityManager.getInstance().getPlayer();
        player.setPosition(scanner.nextInt(), scanner.nextInt());
    }
}

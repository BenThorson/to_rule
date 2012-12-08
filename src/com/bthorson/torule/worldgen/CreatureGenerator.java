package com.bthorson.torule.worldgen;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.ai.GuardAI;
import com.bthorson.torule.entity.ai.LimitedRadiusAggroAI;
import com.bthorson.torule.entity.ai.PatrolAI;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.LocalType;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.Tile;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_POINT;
import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_X;
import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_Y;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 1:10 AM
 */
public class CreatureGenerator {

    public void createHostileMobs(){

        //todo ripe for refactoring
        Point localAmounts = World.getInstance().seCorner().divide(LOCAL_SIZE_POINT);
        Random random = new Random();
        for (int x = 0; x < localAmounts.x(); x++){
            for (int y = 0; y < localAmounts.y(); y++) {
                Point local = new Point(x,y);
                int distToNearestTown = getTownDistance(local);
                if (distToNearestTown == 0){
                    continue;
                }
                else if (distToNearestTown == 1){
                    int numGentle = random.nextInt(15);
                    for (int i = 0; i < numGentle; i++){
                        Point transformedLocal = local.multiply(LOCAL_SIZE_POINT);
                        Point candidate = transformedLocal.add(PointUtil.randomPoint(LOCAL_SIZE_POINT));
                        if (!World.getInstance().isOccupied(candidate)){
                            Creature cow = CreatureFactory.INSTANCE.createCreature("cow", candidate);
                            cow.setAi(new WanderAI(cow, transformedLocal, transformedLocal.add(LOCAL_SIZE_POINT), null, false));
                            cow.setFaction(EntityManager.getInstance().getAggressiveAnimalFaction());
                        }  else {
                            i--;
                        }
                    }
                }
                else if (distToNearestTown == 2){
                    int numGentle = random.nextInt(7);
                    for (int i = 0; i < numGentle; i++){
                        Point transformedLocal = local.multiply(LOCAL_SIZE_POINT);
                        Point candidate = transformedLocal.add(PointUtil.randomPoint(LOCAL_SIZE_POINT));
                        if (!World.getInstance().isOccupied(candidate)){
                            Creature cow = CreatureFactory.INSTANCE.createCreature("cow", candidate);
                            cow.setAi(new WanderAI(cow, transformedLocal, transformedLocal.add(LOCAL_SIZE_POINT), null, false));
                            cow.setFaction(EntityManager.getInstance().getPassiveAnimalFaction());
                        }  else {
                            i--;
                        }
                    }
                    int numAggressive = random.nextInt(7);
                    for (int i = 0; i < numAggressive; i++){
                        Point transformedLocal = local.multiply(LOCAL_SIZE_POINT);
                        Point candidate = transformedLocal.add(PointUtil.randomPoint(LOCAL_SIZE_POINT));
                        if (!World.getInstance().isOccupied(candidate)){
                            Creature boar = CreatureFactory.INSTANCE.createCreature("boar", candidate);
                            boar.setAi(new LimitedRadiusAggroAI(boar, null, 5, new WanderAI(boar,
                                    PointUtil.floorToNearest100(boar.position()),
                                    PointUtil.floorToNearest100(boar.position()).add(100), null, false)));
                            boar.setFaction(EntityManager.getInstance().getAggressiveAnimalFaction());
                        }  else {
                            i--;
                        }
                    }
                }
                else if (distToNearestTown == 3){
                    int numAggressive = random.nextInt(10);
                    for (int i = 0; i < numAggressive; i++){
                        Point transformedLocal = local.multiply(LOCAL_SIZE_POINT);
                        Point candidate = transformedLocal.add(PointUtil.randomPoint(LOCAL_SIZE_POINT));
                        if (!World.getInstance().isOccupied(candidate)){
                            Creature wolf = CreatureFactory.INSTANCE.createCreature("wolf", candidate);
                            wolf.setAi(new WanderAI(wolf, transformedLocal, transformedLocal.add(LOCAL_SIZE_POINT), null, true));
                            wolf.setFaction(EntityManager.getInstance().getAggressiveAnimalFaction());
                        }  else {
                            i--;
                        }
                    }
                } else {
                    int numAggressive = random.nextInt(distToNearestTown * 3);
                     for (int i = 0; i < numAggressive; i++){
                         Point transformedLocal = local.multiply(LOCAL_SIZE_POINT);
                         Point candidate = transformedLocal.add(PointUtil.randomPoint(LOCAL_SIZE_POINT));
                         if (!World.getInstance().isOccupied(candidate)){
                             Creature honeyBadger = CreatureFactory.INSTANCE.createCreature("honeyBadger", candidate);
                             honeyBadger.setAi(new WanderAI(honeyBadger, transformedLocal, transformedLocal.add(LOCAL_SIZE_POINT), null, true));
                             honeyBadger.setFaction(EntityManager.getInstance().getAggressiveAnimalFaction());
                         }  else {
                             i--;
                         }
                     }
                    
                }
            }
        }
    }

    private int getTownDistance(Point local) {
        int min = Integer.MAX_VALUE;
        for (Town town : EntityManager.getInstance().getTowns()){
            int dist = PointUtil.manhattanDist(town.getRegionalPosition(), local);
            if (dist < min){
                min = dist;
            }
        }
        return min;
    }


    public void createPlayer(String playerName, Town town){
        Point placement = town.getRegionalPosition().multiply(new Point(MapConstants.LOCAL_SIZE_X, MapConstants.LOCAL_SIZE_Y));
        Player player = (Player)CreatureFactory.INSTANCE.createCreature("player", placement.add(new Point(50, 50)));
        player.setFaction(town.getFaction());
        player.setName(playerName);
        EntityManager.getInstance().setPlayer(player);
    }

    public void makeTownsmen(Town town, int numberOfTownsmen){
        for (int i = 0; i < numberOfTownsmen; i++){
            Point candidate = PointUtil.randomPoint(town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound());
            if (!World.getInstance().isOccupied(candidate) && !candidate.equals(new Point(50,50))){
                Creature villager = CreatureFactory.INSTANCE.createCreature("villager", candidate);
                villager.setFaction(town.getFaction());
                villager.setAi(new WanderAI(villager, town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound(), null, true));
            } else {
                i--;
            }
        }
    }

    public void createMayor(Town town){
        Point nwCorner = town.getRegionalPosition().multiply(MapConstants.LOCAL_SIZE_POINT);
        Creature mayor = CreatureFactory.INSTANCE.createCreature("mayor", nwCorner.add(84, 97));
        mayor.setAi(new GuardAI(mayor, mayor.position(), null));
        mayor.setFaction(town.getFaction());
    }

    public void makePuppies(Town town, int numberOfTownsmen){
        for (int i = 0; i < numberOfTownsmen; i++){
            Point candidate = PointUtil.randomPoint(town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound());
            if (!World.getInstance().isOccupied(candidate) && !candidate.equals(new Point(50,50))){
                Creature puppy = CreatureFactory.INSTANCE.createCreature("puppy", candidate);
                puppy.setFaction(town.getFaction());
                puppy.setAi(new WanderAI(puppy, town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound(), null, true));
            } else {
                i--;
            }

        }
    }

    public void createShopOwners(Town town, Building building){
        Creature shopOwner = CreatureFactory.INSTANCE.createCreature("merchant", building.getNwCorner().add(new Point(1, 1)));
        shopOwner.setFaction(town.getFaction());
        shopOwner.setAi(new WanderAI(shopOwner,
                                     building.getNwCorner().add(Direction.SOUTH_EAST.point()),
                                     building.getSeCorner().add(Direction.NORTH_WEST.point()), null, true));
        shopOwner.addOwnedProperty("shop", building);
        building.setOwner(shopOwner);
        for (Item item : building.getInventory()){
            item.setOwnedBy(shopOwner);
        }
    }

    public void createMilitia(Town town){

        Point nwCorner = town.getRegionalPosition().multiply(MapConstants.LOCAL_SIZE_POINT);
        String templateName = "city.guard";

        for (Direction d : Direction.values()){
            if (town.getGates().get(d) != null){
                createGateGuards(town, town.getGates().get(d).add(nwCorner),
                                 templateName,
                                 Arrays.asList(Direction.EAST, Direction.WEST).contains(d));
            }
        }
        createPatrollers(town, templateName);

        createKeepGuards(town, templateName, nwCorner);

    }

    private void createKeepGuards(Town town, String templateName, Point offset) {
        Point row1 = offset.add(82, 74);
        Point row2 = offset.add(86, 74);

        for (int i = 0; i < 20; i+= 2){
            Creature guard1 = CreatureFactory.INSTANCE.createCreature(templateName, row1.add(0, i));
            Creature guard2 = CreatureFactory.INSTANCE.createCreature(templateName, row2.add(0,i));
            guard1.setAi(new GuardAI(guard1, guard1.position(), null));
            guard1.setFaction(town.getFaction());
            guard2.setAi(new GuardAI(guard2, guard2.position(), null));
            guard2.setFaction(town.getFaction());
        }
    }

    private void createGateGuards(Town town, Point gate, String templateName, boolean eastWest) {
        Point offset1 = eastWest ? new Point(0,-2) : new Point(-2, 0);
        Point offset2 = eastWest ? new Point(0,1) : new Point(1, 0);
        Creature guard1 = CreatureFactory.INSTANCE.createCreature(templateName, gate.add(offset1));
        Creature guard2 = CreatureFactory.INSTANCE.createCreature(templateName, gate.add(offset2));
        guard1.setAi(new GuardAI(guard1, guard1.position(), null));
        guard1.setFaction(town.getFaction());
        guard2.setAi(new GuardAI(guard2, guard2.position(), null));
        guard2.setFaction(town.getFaction());
    }

    private void createPatrollers(Town town, String templateName){
        Point nwCorner = town.getRegionalPosition().multiply(MapConstants.LOCAL_SIZE_POINT);
        List<Point> point = Arrays.asList(
                nwCorner.add(new Point(50, 83)),
                nwCorner.add(new Point(16, 83)),
                nwCorner.add(new Point(16, 16)),
                nwCorner.add(new Point(50, 16)),
                nwCorner.add(new Point(84, 16)),
                nwCorner.add(new Point(84, 50)),
                nwCorner.add(new Point(60, 50)),
                nwCorner.add(new Point(60, 60)),
                nwCorner.add(new Point(39, 60)),
                nwCorner.add(new Point(39, 39)),
                nwCorner.add(new Point(60, 39)),
                nwCorner.add(new Point(48, 48)));
        Creature guard1 = CreatureFactory.INSTANCE.createCreature(templateName, point.get(0));
        guard1.setFaction(town.getFaction());
        guard1.setAi(new PatrolAI(guard1, point, 0, null));
        Creature guard2 = CreatureFactory.INSTANCE.createCreature(templateName, point.get(5));
        guard2.setFaction(town.getFaction());
        guard2.setAi(new PatrolAI(guard2, point, 4, null));


    }


}

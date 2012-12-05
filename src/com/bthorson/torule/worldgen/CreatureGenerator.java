package com.bthorson.torule.worldgen;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.ai.WanderAI;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.LocalType;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;

import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_POINT;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 1:10 AM
 */
public class CreatureGenerator {

    public void createHostileMobs(){
        Point localAmounts = World.getInstance().seCorner().divide(LOCAL_SIZE_POINT);
        for (int x = 0; x < localAmounts.x(); x++){
            for (int y = 0; y < localAmounts.y(); y++) {
                Point local = new Point(x,y);
                if (LocalType.WILDERNESS.equals(World.getInstance().getLocal(local).getType())) {
                    for (int i = 0; i < 20; i++){
                        Point transformedLocal = local.multiply(LOCAL_SIZE_POINT);
                        Point candidate = transformedLocal.add(PointUtil.randomPoint(LOCAL_SIZE_POINT));
                        if (!World.getInstance().isOccupied(candidate)){
                            Creature wolf = CreatureFactory.INSTANCE.createCreature("wolf", candidate);
                            wolf.setAi(new WanderAI(wolf, transformedLocal, transformedLocal.add(LOCAL_SIZE_POINT)));
                            wolf.setFaction(EntityManager.getInstance().getAggressiveAnimalFaction());
                        }  else {
                            i--;
                        }
                    }
                }
            }
        }
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
                villager.setAi(new WanderAI(villager, town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound()));
            } else {
                i--;
            }
        }
    }

    public void makePuppies(Town town, int numberOfTownsmen){
        for (int i = 0; i < numberOfTownsmen; i++){
            Point candidate = PointUtil.randomPoint(town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound());
            if (!World.getInstance().isOccupied(candidate) && !candidate.equals(new Point(50,50))){
                Creature puppy = CreatureFactory.INSTANCE.createCreature("puppy", candidate);
                puppy.setFaction(town.getFaction());
                puppy.setAi(new WanderAI(puppy, town.getLocal().getNwBoundWorldCoord(), town.getLocal().getSeBoundWorldBound()));
            } else {
                i--;
            }

        }
    }

    public void createShopOwners(Town town, Building building){
        Creature shopOwner = CreatureFactory.INSTANCE.createCreature("merchant", building.getNwCorner().add(new Point(1, 1)));
        shopOwner.setFaction(town.getFaction());
        shopOwner.setAi(new WanderAI(shopOwner,
                                     building.getNwCorner().add(Direction.SOUTHEAST.point()),
                                     building.getSeCorner().add(Direction.NORTHWEST.point())));
        shopOwner.addOwnedProperty("shop", building);
        building.setOwner(shopOwner);
        for (Item item : building.getInventory()){
            item.setOwnedBy(shopOwner);
        }
    }


}

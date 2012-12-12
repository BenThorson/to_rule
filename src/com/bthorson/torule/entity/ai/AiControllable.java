package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;
import sun.org.mozilla.javascript.internal.ScriptableObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: ben
 * Date: 9/20/12
 * Time: 11:07 AM
 */
public interface AiControllable {

    public boolean move(Point point);
    public Set<Faction> getFactionEnemies();

    public List<Creature> getVisibleCreatures();

    boolean canSee(Point position);

    public void attack(Creature entity);

    public Point position();

    Creature getLeader();

    Point getTarget();

    Faction getFaction();

    Direction getHeading();

    boolean isEnemy(Creature facing);

    Creature closestVisibleHostile();

    boolean isHostile(Creature other);

    boolean isWithinRange(Point point, int range);

    Map<String, String> getAggressionLevel();
}

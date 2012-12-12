package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A rather poor interface for merging all ai controlled entities into one interface.
 * User: ben
 * Date: 9/20/12
 * Time: 11:07 AM
 */
public interface AiControllable {

    /**
     * Moves the controllable object the amount indicated by the Point
     * @param point delta value to attempt to move by, not the desired position to move to.
     * @return true or false depending on whether the move was actually accomplished
     */
    public boolean move(Point point);

    /**
     * Get all enemies of the controllable object's faction
     * @return the enemies
     */
    public Set<Faction> getFactionEnemies();

    /**
     * returns all creatures that the object can "see"
     * @return List of creatures
     */
    public List<Creature> getVisibleCreatures();

    /**
     * returns if the object can see a coordinate
     * @param position the coordinate to check if visible
     * @return true if it's visible
     */
    public boolean canSee(Point position);

    /**
     * Attempts to hit and damage another object
     * @param entity creature to attack
     */
    public void attack(Creature entity);

    /**
     * Current position of the object in the world
     * @return the objects position
     */
    public Point position();

    /**
     * Get the entities Leader
     * @return Creature leader
     */
    public Creature getLeader();

    /**
     * Get the entities Target.  This should probably go away and be handled only by the AI
     * @return Point target
     */
    public Point getTarget();

    /**
     * returns the faction of the object
     * @return the faction the object belongs to
     */
    public Faction getFaction();

    /**
     * the {@link Direction} the object is facing
     * @return {@link Direction} enum
     */
    public Direction getHeading();

    /**
     * determines if a creature is an enemy, probably unnecessary due to getFactionEnemies() //todo look at this
     * @param facing Creature to check enemy status
     * @return True if yes
     */
    boolean isEnemy(Creature facing);

    /**
     * determines closest hostile creature, probably unnecessary again //todo look at this
     * @return closest visible
     */
    public Creature closestVisibleHostile();

    /**
     * wtf.  is this method needed too?  //todo I'm a code maintainer's nighmare
     * @param other creature if hostile
     * @return true if yes
     */
    boolean isHostile(Creature other);

    /**
     * Determines if a point is *range* units away or less from the object
     * @param point position to check
     * @param range distance limit from the object to the position
     * @return True if yes
     */
    boolean isWithinRange(Point point, int range);

    /**
     * Map of object's innate hostility tendancies
     * @return map
     */
    public Map<String, String> getAggressionLevel();
}

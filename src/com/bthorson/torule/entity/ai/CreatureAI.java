package com.bthorson.torule.entity.ai;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The "decider" on what a {@link AiControllable} does in the game world.  The subclasses of this base class
 * will often mix and match with each other to create composite behaviors or state based transitions.  The previous
 * field retrieves whatever AI behavior was being used prior to the current one, so when the current AI behavior is done,
 * the old one can be returned for execution next update.
 * User: ben
 * Date: 9/7/12
 * Time: 8:48 PM
 */
public abstract class CreatureAI {

    protected CreatureAI previous;
    protected AiControllable self;

    public CreatureAI(AiControllable self, CreatureAI previous){
        this.previous = previous;
        this.self = self;
    }

    /**
     * Method that determines the {@link Creature} of interest for the behavior.  Intended to be overridable.
     * @return the Creature
     */
    protected Creature getTarget(){
        return self.closestVisibleHostile();
    }

    /**
     * Returns whichever behavior was in place when this one is terminated
     * @return previous AI
     */
    public CreatureAI getPrevious(){
        return previous;
    }

    /**
     * Decision making logic for the movement of the controlled object.
     * @return The {@link CreatureAI} that should be executed the next time the {@link AiControllable} executes
     */
    public abstract CreatureAI execute();

    /**
     * Decision making logic when the controlled object contacts
     * @param entity Entity to interact with
     * @return boolean whether a successful interaction took place
     */
    public abstract boolean interact(Entity entity);

    /**
     * Turns the object into a JSON format.  Only data members that are in the constructor are required.
     * Anything that is used in the constructor must be serialized.  All subclasses that have differing constructor
     * signatures must override this method
     * @return
     */
    public JsonElement serialize(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", getClass().getSimpleName());
        jsonObject.addProperty("self", ((Entity)self).id);
        if (previous != null){
            jsonObject.add("previous", previous.serialize());
        } else {
            jsonObject.addProperty("previous", 0);
        }
        return jsonObject;
    }
}

package com.bthorson.torule.conversation.determiners;

import com.bthorson.torule.entity.Creature;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/1/12
 * Time: 1:12 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Determiner {

    public int determine(Creature creature);
}

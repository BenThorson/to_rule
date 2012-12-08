package com.bthorson.torule.screens;

import com.bthorson.torule.entity.Entity;

/**
 * User: Ben Thorson
 * Date: 12/7/12
 * Time: 2:51 PM
 */
public interface EntityRenderer<T extends Entity> {

    public String render(T input);
}

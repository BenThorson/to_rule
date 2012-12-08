package com.bthorson.torule.screens.component;

import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.screens.EntityRenderer;

/**
 * User: Ben Thorson
 * Date: 12/7/12
 * Time: 10:08 PM
 */
public class DefaultRenderer<T extends Entity> implements EntityRenderer<T> {

    @Override
    public String render(T input) {
        return input.getName();
    }
}

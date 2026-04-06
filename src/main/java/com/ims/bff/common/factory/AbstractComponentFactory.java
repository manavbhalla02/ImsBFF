package com.ims.bff.common.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ims.bff.common.component.SupportsType;

public abstract class AbstractComponentFactory<K, C extends SupportsType<K>> {

    private final Map<K, C> components = new HashMap<>();

    protected AbstractComponentFactory(List<C> components) {
        components.forEach(component -> this.components.put(component.supports(), component));
    }

    protected C getComponent(K key, RuntimeException exception) {
        C component = components.get(key);
        if (component == null) {
            throw exception;
        }
        return component;
    }
}

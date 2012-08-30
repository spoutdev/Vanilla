package org.spout.vanilla.component.components;

import java.util.Collection;
import java.util.HashMap;

import org.spout.api.component.Component;
import org.spout.api.component.ComponentHolder;
import org.spout.api.component.components.EntityComponent;
import org.spout.vanilla.protocol.entity.VanillaEntityProtocol;

public abstract class VanillaEntityComponent extends EntityComponent implements ComponentHolder {
	private final HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();
	
	public abstract VanillaEntityProtocol getEntityProtocol();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Component> T addComponent(T component) {
		if (component.attachTo(this)) {
			Class<? extends Component> clazz = component.getClass();
			if (hasComponent(clazz)) {
				return (T) getComponent(clazz);
			}
			components.put(clazz, component);
			component.onAttached();
			return (T) component;
		} else {
			return null;
		}
	}

	@Override
	public boolean removeComponent(Class<? extends Component> aClass) {
		if (!hasComponent(aClass)) {
			return false;
		}
		getComponent(aClass).onDetached();
		components.remove(aClass);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Component> T getComponent(Class<T> aClass) {
		for(Class<? extends Component> c : components.keySet()){
			if(aClass.isAssignableFrom(c)) return (T) components.get(c);
		}
		return null;
	}

	@Override
	public boolean hasComponent(Class<? extends Component> aClass) {
		for(Class<? extends Component> c : components.keySet()){
			if(aClass.isAssignableFrom(c)) return true;
		}
		return false;
	}

	@Override
	public Collection<Component> getComponents() {
		return components.values();
	}
}

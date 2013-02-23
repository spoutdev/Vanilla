package org.spout.vanilla;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.spout.api.Engine;
import org.spout.api.component.BaseComponentHolder;
import org.spout.api.component.Component;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;

public class EntityMocker {

	public static Entity mockEntity() {
		EngineFaker.setupEngine();

		final Entity entity = Mockito.mock(Entity.class);
		final EntityComponentAnswer componentHolder = new EntityComponentAnswer(entity);

		//Set up component holder methods
		Mockito.when(entity.add(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.get(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.getExact(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.detach(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.getData()).thenAnswer(componentHolder);

		//Set up entity tick
		Mockito.doAnswer(new EntityTickAnswer(entity)).when(entity).onTick(Mockito.anyFloat());

		//Set up event manager
		return entity;
	}
	
	private static class EntityTickAnswer implements Answer<Object> {
		private final Entity entity;
		EntityTickAnswer(Entity entity) {
			this.entity = entity;
		}

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			float dt = (Float)invocation.getArguments()[0];
			for (Component c : entity.values()) {
				c.tick(dt);
			}
			return null;
		}
	}

	private static class EntityComponentAnswer extends BaseComponentHolder implements Answer<Component> {
		private final Entity entity;
		EntityComponentAnswer(Entity entity) {
			this.entity = entity;
		}

		@Override
		protected void attachComponent(Class<? extends Component> key, Component component, boolean attach) throws Exception{
			if (component.attachTo(entity)) {
				components.put(key, component);
				if (attach) {
					try {
						component.onAttached();
					} catch (Exception e) {
						components.remove(key);
						throw e;
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component answer(InvocationOnMock invocation) throws Throwable {
			if (invocation.getMethod().getName().equals("getData")) {
				return getData();
			}
			Class<? extends EntityComponent> clazz = (Class<? extends EntityComponent>) invocation.getArguments()[0];
			if (invocation.getMethod().getName().equals("add")) {
				return add(clazz);
			} else if (invocation.getMethod().getName().equals("get")) {
				return get(clazz);
			} else if (invocation.getMethod().getName().equals("getExact")) {
				return getExact(clazz);
			} else {
				return detach(clazz);
			}
		}
	}

	private static class ClassOrSubclassMatcher<T> extends BaseMatcher<Class<T>> {
	    private final Class<T> targetClass;

	    public ClassOrSubclassMatcher(Class<T> targetClass) {
	        this.targetClass = targetClass;
	    }

	    @SuppressWarnings("unchecked")
	    public boolean matches(Object obj) {
            if (obj instanceof Class) {
                return targetClass.isAssignableFrom((Class<T>) obj);
            }
	        return false;
	    }

	    public void describeTo(Description desc) {
	        desc.appendText("Matches a class or subclass");
	    }       
	}
}

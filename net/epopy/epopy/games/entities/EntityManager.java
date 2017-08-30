package net.epopy.epopy.games.entities;

import java.util.LinkedList;
import java.util.List;

public class EntityManager {
	
	private List<Entity> entities = new LinkedList<>();
	
	public EntityManager() {
	
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	public void update() {// TODO render que les entities a voir

		for (Entity entity : entities)
			entity.update();

	}
	
	public void update(double x, double y) {
		for (Entity entity : entities)
			entity.update(x, y);

	}
	
	public Entity getEntity(String name) {
		for (Entity entity : entities)
			if (entity.getName().equals(name)) return entity;

		return null;
	}
	
	public void render() {
		for (Entity entity : entities)
			entity.render();
			
	}
}

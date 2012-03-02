package org.spout.vanilla.command;

import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Entity;
import org.spout.api.exception.CommandException;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.entity.sky.NormalSky;

public class TestCommands {
	private final VanillaPlugin plugin = VanillaPlugin.getInstance();
	
	
	@Command(aliases = {"getsky"}, usage = "justdoit]", desc = "should respond with 0, if not royawesome know what the fuck he is doing")
	public void getsky(CommandContext args, CommandSource source) throws CommandException {
		Set<Entity> ents = VanillaPlugin.spawnWorld.getAll(NormalSky.class);
		Spout.getLogger().info("Size of ents: " + ents.size());
		
	}
}

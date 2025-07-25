package blueprint.blueprintlib;

import blueprint.blueprintlib.debug.DebugItem;
import blueprint.blueprintlib.util.TeslaArcHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlueprintLibrary implements ModInitializer {
	public static final String MOD_ID = "blueprintlib";

	public static final Item DEBUG_ITEM = new DebugItem(new Item.Settings());


	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Registry.register(Registries.ITEM,
				new Identifier("blueprintlib", "debug_item"),
				DEBUG_ITEM
		);

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			TeslaArcHandler.tick(server);
		});

		LOGGER.info("Hello Fabric world!");
	}
}
package org.charcoalwhite.takeme;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeMe implements ModInitializer {
	public static final String MOD_ID = "takeme";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("[TakeMe] *HeavyHeavyHeavy-*");

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!entity.isPlayer()) {
				return ActionResult.PASS;
			}

			if (player.isSneaking()) {
				if (!player.hasPassengers()) {
					entity.startRiding(player, true);
				} else
				if (player.hasPassenger(entity) && (int) player.getPitch() == 90) {
					entity.stopRiding();
				} else {
					Entity entity2 = player.getFirstPassenger();
					if (!entity.hasPassengers()) {
						entity2.startRiding(entity, true);
					}
				}
			} else
			if (!entity.hasPassengers()) {
				player.startRiding(entity, true);
			}
			return ActionResult.PASS;
		});
	}
}
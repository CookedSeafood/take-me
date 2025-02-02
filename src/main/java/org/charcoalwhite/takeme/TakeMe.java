package org.charcoalwhite.takeme;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
			if (!(entity.isPlayer() && player.getMainHandStack().isEmpty() && player.getOffHandStack().isEmpty())) {
				return ActionResult.PASS;
			}

			PlayerEntity player2 = (PlayerEntity) entity;
			if (player.isSneaking()) {
				if (!player.hasPassengers()) {
					player2.startRiding(player, true);
				} else
				if (player.hasPassenger(player2) && (int) player.getPitch() == 90) {
					player2.stopRiding();
				} else {
					Entity entity2 = player.getFirstPassenger();
					if (!entity2.isPlayer()) {
						return ActionResult.PASS;
					}

					PlayerEntity player3 = (PlayerEntity) entity2;
					if (!player2.hasPassengers()) {
						player3.startRiding(player2, true);
					}
				}
			} else
			if (!player2.hasPassengers()) {
				player.startRiding(player2, true);
			}
			return ActionResult.PASS;
		});
	}
}
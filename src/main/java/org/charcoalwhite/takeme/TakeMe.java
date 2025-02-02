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
			if (!entity.isPlayer()) {
				return ActionResult.PASS;
			}

			if (!(player.getMainHandStack().isEmpty() && player.getOffHandStack().isEmpty())) {
				return ActionResult.PASS;
			}

			PlayerEntity usedPlayer = (PlayerEntity) entity;
			if (!player.isSneaking()) {
				if (!usedPlayer.hasPassengers()) {
					player.startRiding(usedPlayer, true);
				}
				return ActionResult.PASS;
			}

			int pitch = (int) player.getPitch();
			if (!player.hasPassengers()) {
				if (!(pitch == -90) && !usedPlayer.hasPassengers()) {
					usedPlayer.startRiding(player, true);
				}
				return ActionResult.PASS;
			}

			if (player.hasPassenger(usedPlayer)) {
				if (pitch == -90) {
					usedPlayer.stopRiding();
				}
				return ActionResult.PASS;
			}

			Entity passengerEntity = player.getFirstPassenger();
			if (!passengerEntity.isPlayer()) {
				return ActionResult.PASS;
			}

			PlayerEntity passengerPlayer = (PlayerEntity) passengerEntity;
			passengerPlayer.startRiding(usedPlayer, true);
			if (!usedPlayer.hasPassengers()) {
				return ActionResult.PASS;
			}

			Entity passengerEntity2 = usedPlayer.getFirstPassenger();
			if (!passengerEntity2.isPlayer()) {
				return ActionResult.PASS;
			}

			PlayerEntity passengerPlayer2 = (PlayerEntity) passengerEntity2;
			passengerPlayer2.startRiding(player, true);
			return ActionResult.PASS;
		});
	}
}
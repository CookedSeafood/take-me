package net.cookedseafood.takeme;

import net.cookedseafood.takeme.command.TakeMeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TakeMe implements ModInitializer {
	public static final String MOD_ID = "takeme";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_PATCH = 1;

	public static final boolean MAIN_HAND_FILTER_MODE = false;
	public static final boolean OFF_HAND_FILTER_MODE = false;
	public static final Set<String> MAIN_HAND_FILTER_ITEMS = Stream.of("minecraft:air").collect(Collectors.toUnmodifiableSet());
	public static final Set<String> OFF_HAND_FILTER_ITEMS = MAIN_HAND_FILTER_ITEMS;

	public static boolean mainHandFilterMode = false;
	public static boolean offHandFilterMode = false;
	public static Set<String> mainHandFilterItems = Stream.of("minecraft:air").collect(Collectors.toUnmodifiableSet());
	public static Set<String> offHandFilterItems = mainHandFilterItems;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("[TakeMe] *HeavyHeavyHeavy-*");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TakeMeCommand.register(dispatcher, registryAccess));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			TakeMeCommand.executeReload(server.getCommandSource());
		});

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!entity.isPlayer()) {
				return ActionResult.PASS;
			}

			if (mainHandFilterMode == mainHandFilterItems.contains(player.getMainHandStack().getRegistryEntry().getIdAsString())
				|| offHandFilterMode == offHandFilterItems.contains(player.getOffHandStack().getRegistryEntry().getIdAsString())
			) {
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
			if (!usedPlayer.hasPassengers()) {
				passengerPlayer.startRiding(usedPlayer, true);
			}
			return ActionResult.PASS;
		});
	}

	public static int reload() {
		String configString;
		try {
			configString = FileUtils.readFileToString(new File("./config/takeme.json"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			reset();
			return 1;
		}

		JsonObject configObject = new Gson().fromJson(configString, JsonObject.class);

        mainHandFilterMode =
            configObject.has("mainHandFilterMode") ?
            configObject.get("mainHandFilterMode").getAsBoolean() :
            MAIN_HAND_FILTER_MODE;
        offHandFilterMode =
            configObject.has("offHandFilterMode") ?
            configObject.get("offHandFilterMode").getAsBoolean() :
            OFF_HAND_FILTER_MODE;
        mainHandFilterItems =
            configObject.has("mainHandFilterItems") ?
            configObject.get("mainHandFilterItems").getAsJsonArray().asList().stream().map(jsonElement -> jsonElement.getAsString()).collect(Collectors.toUnmodifiableSet()) :
            MAIN_HAND_FILTER_ITEMS;
        offHandFilterItems =
            configObject.has("offHandFilterItems") ?
            configObject.get("offHandFilterItems").getAsJsonArray().asList().stream().map(jsonElement -> jsonElement.getAsString()).collect(Collectors.toUnmodifiableSet()) :
            OFF_HAND_FILTER_ITEMS;
        return 2;
	}

	public static void reset() {
		mainHandFilterMode = MAIN_HAND_FILTER_MODE;
        offHandFilterMode = OFF_HAND_FILTER_MODE;
        mainHandFilterItems = MAIN_HAND_FILTER_ITEMS;
        offHandFilterItems = OFF_HAND_FILTER_ITEMS;
	}
}

package net.cookedseafood.takeme;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.cookedseafood.takeme.command.TakeMeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeMe implements ModInitializer {
	public static final String MOD_ID = "take-me";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_PATCH = 4;

	public static final boolean MAIN_HAND_FILTER_MODE = false;
	public static final boolean OFF_HAND_FILTER_MODE = false;
	public static final Set<String> MAIN_HAND_FILTER_ITEMS = Stream.of("minecraft:air").collect(Collectors.toUnmodifiableSet());
	public static final Set<String> OFF_HAND_FILTER_ITEMS = MAIN_HAND_FILTER_ITEMS;

	public static boolean mainHandFilterMode;
	public static boolean offHandFilterMode;
	public static Set<String> mainHandFilterItems;
	public static Set<String> offHandFilterItems;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("[Take-Me] *HeavyHeavyHeavy-*");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TakeMeCommand.register(dispatcher, registryAccess));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			TakeMe.reload(server);
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

	public static int reload(MinecraftServer server) {
		String configString;
		try {
			configString = FileUtils.readFileToString(new File("./config/take-me.json"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			reset();
			return -1;
		}

		JsonObject config = new Gson().fromJson(configString, JsonObject.class);
		MutableInt counter = new MutableInt(0);

		if (config.has("mainHandFilterMode")) {
			mainHandFilterMode = config.get("mainHandFilterMode").getAsBoolean();
			counter.increment();
		} else {
			mainHandFilterMode = MAIN_HAND_FILTER_MODE;
		}

		if (config.has("offHandFilterMode")) {
			offHandFilterMode = config.get("offHandFilterMode").getAsBoolean();
			counter.increment();
		} else {
			offHandFilterMode = OFF_HAND_FILTER_MODE;
		}

		if (config.has("mainHandFilterItems")) {
			mainHandFilterItems = config.get("mainHandFilterItems").getAsJsonArray().asList().stream()
				.map(JsonElement::getAsString)
				.collect(Collectors.toUnmodifiableSet());
			counter.increment();
		} else {
			mainHandFilterItems = MAIN_HAND_FILTER_ITEMS;
		}

		if (config.has("offHandFilterItems")) {
			offHandFilterItems = config.get("offHandFilterItems").getAsJsonArray().asList().stream()
				.map(JsonElement::getAsString)
				.collect(Collectors.toUnmodifiableSet());
			counter.increment();
		} else {
			offHandFilterItems = OFF_HAND_FILTER_ITEMS;
		}

        return counter.intValue();
	}

	public static void reset() {
		mainHandFilterMode = MAIN_HAND_FILTER_MODE;
        offHandFilterMode = OFF_HAND_FILTER_MODE;
        mainHandFilterItems = MAIN_HAND_FILTER_ITEMS;
        offHandFilterItems = OFF_HAND_FILTER_ITEMS;
	}
}

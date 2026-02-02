package net.hederamc.takeme;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TakeMe implements ModInitializer {
    public static final String MOD_ID = "take-me";
    public static final String MOD_NAMESPACE = "take_me";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("[Take-Me] *HeavyHeavyHeavy-*");

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(entity instanceof ServerPlayer)) {
                return InteractionResult.PASS;
            }

            if (player.getLastTakePlayerTimestamp() == player.tickCount) {
                return InteractionResult.PASS;
            }

            if (!(player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty())) {
                return InteractionResult.PASS;
            }

            ServerPlayer usedPlayer = (ServerPlayer)entity;
            if (!player.isCrouching()) {
                if (usedPlayer.getPassengers().isEmpty()) {
                    player.startRiding(usedPlayer, true, true);
                    player.setLastTakePlayerTimestamp(player.tickCount);
                }
                return InteractionResult.PASS;
            }

            if (player.getPassengers().isEmpty()) {
                usedPlayer.startRiding(player, true, true);
                player.setLastTakePlayerTimestamp(player.tickCount);
                return InteractionResult.PASS;
            }

            if (player.hasPassenger(usedPlayer)) {
                usedPlayer.stopRiding();
                player.setLastTakePlayerTimestamp(player.tickCount);
                return InteractionResult.PASS;
            }

            Entity passenger = player.getFirstPassenger();
            if (!(passenger instanceof ServerPlayer)) {
                return InteractionResult.PASS;
            }

            ServerPlayer passengerPlayer = (ServerPlayer)passenger;
            if (usedPlayer.getPassengers().isEmpty()) {
                passengerPlayer.startRiding(usedPlayer, true, true);
            }

            Entity usedPlayerPassenger = usedPlayer.getFirstPassenger();
            if (!(usedPlayerPassenger instanceof ServerPlayer)) {
                return InteractionResult.PASS;
            }

            ServerPlayer usedPlayerPassengerPlayer = (ServerPlayer)usedPlayerPassenger;
            usedPlayerPassengerPlayer.stopRiding();
            passengerPlayer.startRiding(usedPlayer, true, true);
            usedPlayerPassengerPlayer.startRiding(player, true, true);
            return InteractionResult.PASS;
        });
    }
}

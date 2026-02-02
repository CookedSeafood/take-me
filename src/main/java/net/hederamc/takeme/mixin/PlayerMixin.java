package net.hederamc.takeme.mixin;

import net.hederamc.takeme.api.PlayerTaker;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerTaker {
    @Unique
    protected int lastTakePlayerTimestamp;

    @Override
    public int getLastTakePlayerTimestamp() {
        return lastTakePlayerTimestamp;
    }

    @Override
    public void setLastTakePlayerTimestamp(int timestamp) {
        this.lastTakePlayerTimestamp = timestamp;
    }
}

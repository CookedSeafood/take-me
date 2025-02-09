package net.cookedseafood.takeme.command;

import com.mojang.brigadier.CommandDispatcher;
import net.cookedseafood.takeme.TakeMe;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class TakeMeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("takeme")
            .then(
                CommandManager.literal("version")
                .executes(context -> executeVersion((ServerCommandSource)context.getSource()))
            )
        );
    }

    public static int executeVersion(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("TakeMe " + TakeMe.versionMajor + "." + TakeMe.versionMinor + "." + TakeMe.versionPatch), false);
        return 0;
    }
}

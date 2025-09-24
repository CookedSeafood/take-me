package net.cookedseafood.takeme.command;

import com.mojang.brigadier.CommandDispatcher;
import net.cookedseafood.takeme.TakeMe;
import net.cookedseafood.takeme.data.TakeMeConfig;
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
            .then(
                CommandManager.literal("reload")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeReload((ServerCommandSource)context.getSource()))
            )
        );
    }

    public static int executeVersion(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Take Me " + TakeMe.VERSION_MAJOR + "." + TakeMe.VERSION_MINOR + "." + TakeMe.VERSION_PATCH), false);
        return 0;
    }

    public static int executeReload(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Reloading Take Me!"), true);
        return Math.abs(TakeMeConfig.reload(source.getServer()));
    }
}

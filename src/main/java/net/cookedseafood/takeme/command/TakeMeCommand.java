package net.cookedseafood.takeme.command;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
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
            .then(
                CommandManager.literal("reload")
                .executes(context -> executeReload((ServerCommandSource)context.getSource()))
            )
        );
    }

    public static int executeVersion(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("TakeMe " + TakeMe.VERSION_MAJOR + "." + TakeMe.VERSION_MINOR + "." + TakeMe.VERSION_PATCH), false);
        return 0;
    }

    public static int executeReload(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Reloading TakeMe!"), true);

        String configString;
		try {
			configString = FileUtils.readFileToString(new File("./config/takeme.json"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return 0;
		}

		JsonObject configObject = new Gson().fromJson(configString, JsonObject.class);

        TakeMe.mainHandFilterMode =
            configObject.has("mainHandFilterMode") ?
            configObject.get("mainHandFilterMode").getAsBoolean() :
            false;
        TakeMe.offHandFilterMode =
            configObject.has("offHandFilterMode") ?
            configObject.get("offHandFilterMode").getAsBoolean() :
            false;
        TakeMe.mainHandFilterItems =
            configObject.has("mainHandFilterItems") ?
            configObject.get("mainHandFilterItems").getAsJsonArray().asList().stream().map(jsonElement -> jsonElement.getAsString()).collect(Collectors.toUnmodifiableSet()) :
            Stream.of("minecraft:air").collect(Collectors.toUnmodifiableSet());
        TakeMe.offHandFilterItems =
            configObject.has("offHandFilterItems") ?
            configObject.get("offHandFilterItems").getAsJsonArray().asList().stream().map(jsonElement -> jsonElement.getAsString()).collect(Collectors.toUnmodifiableSet()) :
            Stream.of("minecraft:air").collect(Collectors.toUnmodifiableSet());

        return 1;
    }
}

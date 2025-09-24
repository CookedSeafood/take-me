package net.cookedseafood.takeme.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public abstract class TakeMeConfig {
    public static final boolean MAIN_HAND_FILTER_MODE = false;
    public static final boolean OFF_HAND_FILTER_MODE = false;
    public static final Set<String> MAIN_HAND_FILTER_ITEMS = Stream.of("minecraft:air").collect(Collectors.toUnmodifiableSet());
    public static final Set<String> OFF_HAND_FILTER_ITEMS = MAIN_HAND_FILTER_ITEMS;
    public static boolean mainHandFilterMode;
    public static boolean offHandFilterMode;
    public static Set<String> mainHandFilterItems;
    public static Set<String> offHandFilterItems;

    public static int reload(MinecraftServer server) {
        String configString;
        try {
            configString = FileUtils.readFileToString(new File("./config/take-me.json"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            reset();
            return 1;
        }

        JsonObject config = new Gson().fromJson(configString, JsonObject.class);
        if (config == null) {
            reset();
            return 1;
        }

        return reload(server, config);
    }

    public static int reload(MinecraftServer server, JsonObject config) {
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

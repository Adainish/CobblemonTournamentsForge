package io.github.adainish.cobblemontournamentsforge.util;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Locale;
import java.util.Optional;

public class ResourceLocationHelper
{
    public static ResourceLocation of(String resourceLocation) {
        try {
            return new ResourceLocation(resourceLocation.toLowerCase(Locale.ROOT));
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
    public static ResourceKey<Level> getDimension(String dimension) {
        return dimension.isEmpty() ? null : getDimension(ResourceLocationHelper.of(dimension));
    }

    public static ResourceKey<Level> getDimension(ResourceLocation key) {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, key);
    }

    public static Optional<ServerLevel> getWorld(ResourceKey<Level> key) {
        return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer().getLevel(key));
    }

    public static Optional<ServerLevel> getWorld(String key) {
        return getWorld(getDimension(key));
    }
}

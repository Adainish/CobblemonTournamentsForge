package io.github.adainish.cobblemontournamentsforge.util;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.util.adapters.ItemStackAdapter;
import com.cobblemon.mod.common.util.adapters.PokemonPropertiesAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Modifier;

public class Adapters
{
    public static Gson PRETTY_MAIN_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(ItemStack.class, ItemStackAdapter.INSTANCE)
            .registerTypeAdapter(PokemonProperties.class, new PokemonPropertiesAdapter(true))
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create();
}

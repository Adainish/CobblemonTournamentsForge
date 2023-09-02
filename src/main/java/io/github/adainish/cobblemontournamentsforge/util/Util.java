package io.github.adainish.cobblemontournamentsforge.util;

import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Util
{
    public static final long DAY_IN_MILLIS = 86400000;
    public static final long HOUR_IN_MILLIS = 3600000;
    public static final long MINUTE_IN_MILLIS = 60000;
    public static final long SECOND_IN_MILLIS = 1000;
    public static String formattedString(String s) {
        return s.replaceAll("&", "§");
    }

    public static List<String> formattedArrayList(List<String> list) {

        List<String> formattedList = new ArrayList<>();
        for (String s : list) {
            formattedList.add(formattedString(s));
        }

        return formattedList;
    }

    public static ServerPlayer getPlayer(UUID uuid) {
        return CobblemonTournamentsForge.getServer().getPlayerList().getPlayer(uuid);
    }

    public static void send(UUID uuid, String message) {
        if (message == null)
            return;
        if (message.isEmpty())
            return;
        ServerPlayer player = getPlayer(uuid);
        if (player != null)
            player.sendSystemMessage(Component.literal(((TextUtil.getMessagePrefix()).getString() + message).replaceAll("&([0-9a-fk-or])", "\u00a7$1")));
    }

    public static ItemStack getPlayerHead(String username) {
        CompoundTag nbt = new CompoundTag();
        ItemStack skullStack;
        Optional<GameProfile> gameprofile = CobblemonTournamentsForge.getServer().getProfileCache().get(username);

        if (gameprofile.isPresent()) {
            nbt.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), gameprofile.get()));
            skullStack = new ItemStack(Items.PLAYER_HEAD);
            skullStack.setTag(nbt);
        } else skullStack = new ItemStack(Items.SKELETON_SKULL);
        return skullStack;
    }

    public static ItemStack returnIcon(Pokemon pokemon) {
        return PokemonItem.from(pokemon, 1);
    }


    public static void send(CommandSourceStack sender, String message) {
        sender.sendSystemMessage(Component.literal(((TextUtil.getMessagePrefix()).getString() + message).replaceAll("&([0-9a-fk-or])", "\u00a7$1")));
    }

    public static ChestTemplate.Builder returnBasicNonPlaceholderTemplateBuilder() {
        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());
        return builder;
    }

    public static ChestTemplate.Builder returnBasicTemplateBuilder() {
        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());

        PlaceholderButton placeHolderButton = new PlaceholderButton();
        LinkedPageButton previous = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Previous Page"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Next Page"))
                .linkType(LinkType.Next)
                .build();

        builder.set(0, 3, previous)
                .set(0, 5, next)
                .rectangle(1, 1, 3, 7, placeHolderButton);
        return builder;
    }

    public static void runCommand(String cmd)
    {
        try {
            CobblemonTournamentsForge.getServer().getCommands().getDispatcher().execute(cmd, CobblemonTournamentsForge.getServer().createCommandSourceStack());
        } catch (CommandSyntaxException e) {
            CobblemonTournamentsForge.getLog().error(e);
        }
    }
    public static GooeyButton filler() {
        return GooeyButton.builder()
                .display(new ItemStack(Items.GRAY_STAINED_GLASS_PANE))
                .build();
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

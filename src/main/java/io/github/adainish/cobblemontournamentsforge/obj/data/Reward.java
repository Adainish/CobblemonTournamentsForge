package io.github.adainish.cobblemontournamentsforge.obj.data;

import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import io.github.adainish.cobblemontournamentsforge.util.Util;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reward
{
    public String id = "";
    public int weight = 5;
    public List<String> commands = new ArrayList<>(Arrays.asList("give %player% minecraft:emerald 1"));

    public Reward(String id)
    {
        this.id = id;
    }

    public void giveRewards(ServerPlayer serverPlayer)
    {
        String userName = serverPlayer.getName().getString();
        if (!commands.isEmpty())
        {
            commands.forEach(command -> {
                Util.runCommand(command.replace("%player%", userName));
            });
        } else CobblemonTournamentsForge.getLog().warn("Could not hand out rewards to %username% as the list was empty!".replace("%username%", userName));
    }
}

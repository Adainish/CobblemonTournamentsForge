package io.github.adainish.cobblemontournamentsforge.obj.arena;

import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import io.github.adainish.cobblemontournamentsforge.obj.player.Player;
import io.github.adainish.cobblemontournamentsforge.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Location
{
    public double posX;
    public double posY;
    public double posZ;
    public String levelName;
    public Location()
    {

    }

    @Nullable
    public ServerLevel getWorld() {
        if (Util.getWorld(this.levelName).isPresent())
            return Util.getWorld(this.levelName).get();
        else return null;
    }


    public void teleport(Player player)
    {

        if (player.getOptionalServerPlayer().isPresent())
        {
            ServerPlayer serverPlayer = player.getOptionalServerPlayer().get();
            if (Util.getWorld(this.levelName).isEmpty())
            {
                player.sendMessage("&4&lThe world for this area was not accessible, please contact a staff member if this issue persists!");
                return;
            }

            ServerLevel world = Util.getWorld(this.levelName).get();
            serverPlayer.teleportTo(world, posX, posY, posZ, serverPlayer.getXRot(), serverPlayer.getYHeadRot());
            player.sendMessage("&aTeleported you to your destination!");
        } else {
            player.sendMessage("&cSomething went wrong while warping you to this area, please contact a member of staff if this issue persists.");
            CobblemonTournamentsForge.getLog().error("Failed to warp %name% to a provided location, this could be a data error. Contact the dev if this persists".replace("%name%", player.userName));
        }
    }

    public List<ServerPlayer> getPlayersNearLocation()
    {
        List<ServerPlayer> entities = new ArrayList<>();

        ServerLevel world = getWorld();
        if (world != null) {
            BlockPos pos1 = new BlockPos(this.posX - 5, this.posY - 10, this.posZ - 5);
            BlockPos pos2 = new BlockPos(this.posX + 5, this.posY + 10, this.posZ + 5);
            AABB isWithinAABB = new AABB(pos1, pos2);
            List<ServerPlayer> playerList = new ArrayList<>(world.getEntitiesOfClass(ServerPlayer.class, isWithinAABB));
            entities.addAll(playerList);
        }
        return entities;
    }

    public boolean isAvailable()
    {
        return this.getPlayersNearLocation().isEmpty();
    }
}

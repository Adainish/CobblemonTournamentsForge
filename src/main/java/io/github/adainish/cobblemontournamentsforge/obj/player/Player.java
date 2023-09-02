package io.github.adainish.cobblemontournamentsforge.obj.player;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import io.github.adainish.cobblemontournamentsforge.storage.PlayerStorage;
import io.github.adainish.cobblemontournamentsforge.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Player
{
    public UUID uuid;
    public String userName;
    public transient List<PokemonProperties> enteredPokemonTeamPropertiesList = new ArrayList<>();
    public List<PokemonProperties> cachedPokemon = new ArrayList<>();
    public Player()
    {

    }

    public Player(UUID uuid)
    {
        this.uuid = uuid;
        this.userName = "";
    }

    public Player(UUID uuid, String userName)
    {
        this.uuid = uuid;
        this.userName = userName;
    }

    public void sendMessage(String msg)
    {
        if (msg == null)
            return;
        if (msg.isEmpty())
            return;
        Util.send(this.uuid, msg);
    }

    public void setUsername(String name)
    {
        this.userName = name;
    }

    public void save()
    {
        PlayerStorage.savePlayer(this);
    }

    public void updateCache()
    {
        CobblemonTournamentsForge.playerCache.playerCache.put(uuid, this);
    }

    public Optional<ServerPlayer> getOptionalServerPlayer()
    {
        return Optional.ofNullable(Util.getPlayer(this.uuid));
    }

    public boolean isOnline()
    {
        return getOptionalServerPlayer().isPresent();
    }

    public void teleportToThisPlayer(Player player)
    {

        if (this.getOptionalServerPlayer().isPresent()) {
            ServerPlayer ownServerPlayer = this.getOptionalServerPlayer().get();
            if (player.getOptionalServerPlayer().isPresent()) {
                ServerPlayer serverPlayer = player.getOptionalServerPlayer().get();

                ServerLevel world = ownServerPlayer.getLevel();
                serverPlayer.teleportTo(world, ownServerPlayer.getX(), ownServerPlayer.getY(), ownServerPlayer.getZ(), serverPlayer.getXRot(), serverPlayer.getYHeadRot());
                player.sendMessage("&aTeleported you to your destination!");
            } else {
                player.sendMessage("&cSomething went wrong while warping you to this area, please contact a member of staff if this issue persists.");
                CobblemonTournamentsForge.getLog().error("Failed to warp %name% to a provided location, this could be a data error. Contact the dev if this persists".replace("%name%", player.userName));
            }
        } else player.sendMessage("&cSomething went wrong while accessing data, please contact staff if this issue persists.");
    }

    public void teleportToOtherPlayer(Player player)
    {

        if (this.getOptionalServerPlayer().isPresent()) {
            ServerPlayer ownServerPlayer = this.getOptionalServerPlayer().get();
            if (player.getOptionalServerPlayer().isPresent()) {
                ServerPlayer serverPlayer = player.getOptionalServerPlayer().get();

                ServerLevel world = serverPlayer.getLevel();
                ownServerPlayer.teleportTo(world, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getXRot(), serverPlayer.getYHeadRot());
                this.sendMessage("&aTeleported you to your destination!");
            } else {
                this.sendMessage("&cSomething went wrong while warping you to this area, please contact a member of staff if this issue persists.");
                CobblemonTournamentsForge.getLog().error("Failed to warp %name% to a provided location, this could be a data error. Contact the dev if this persists".replace("%name%", player.userName));
            }
        } else this.sendMessage("&cSomething went wrong while accessing your data, please contact staff if this issue persists.");
    }

    public Optional<PlayerPartyStore> getOptionalPlayerPartyStore()
    {
        Optional<PlayerPartyStore> playerPartyStore = Optional.empty();

        PlayerPartyStore party = null;
        try {
            party = Cobblemon.INSTANCE.getStorage().getParty(this.uuid);
            playerPartyStore = Optional.of(party);
        } catch (NoPokemonStoreException ignored) {

        }

        return playerPartyStore;
    }

    public Optional<List<Pokemon>> getOptionalActivePartyPokemon()
    {
        Optional<List<Pokemon>> pokemonList = Optional.empty();


        if (getOptionalPlayerPartyStore().isPresent()) {
            List<Pokemon> pokemonArrayList = new ArrayList<>();
            PlayerPartyStore partyStore = getOptionalPlayerPartyStore().get();
            partyStore.forEach(pokemon -> {
                if (pokemon != null)
                    pokemonArrayList.add(pokemon);
            });
            pokemonList = Optional.of(pokemonArrayList);
        }
        return pokemonList;
    }

    public Optional<List<PokemonProperties>> getOptionalActivePartyProperties()
    {
        Optional<List<PokemonProperties>> optionalPokemonPropertiesList = Optional.empty();

        if (getOptionalActivePartyPokemon().isPresent()) {
            List<Pokemon> pokemonList = getOptionalActivePartyPokemon().get();
            if (!pokemonList.isEmpty()) {
                List<PokemonProperties> propertiesList = new ArrayList<>();
                pokemonList.forEach(pokemon -> {
                    propertiesList.add(pokemon.createPokemonProperties(PokemonPropertyExtractor.Companion.getALL()).copy());
                });
                optionalPokemonPropertiesList = Optional.of(propertiesList);
            }
        }


        return optionalPokemonPropertiesList;
    }

    public void healParty()
    {
        if (getOptionalPlayerPartyStore().isPresent())
        {
            getOptionalPlayerPartyStore().get().heal();
            this.sendMessage("&aYour party was healed!");
        } else this.sendMessage("&cYour player data was unable to be retrieved... Could not heal your party!");
    }
}

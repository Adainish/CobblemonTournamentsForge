package io.github.adainish.cobblemontournamentsforge.obj.matches;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleBuilder;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleStartResult;
import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import io.github.adainish.cobblemontournamentsforge.obj.player.Player;
import kotlin.Unit;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Brackets
{

    public List<UUID> enteredPlayers = new ArrayList<>();

    public List<Round> previousRounds = new ArrayList<>();
    public Round activeRound;

    // TODO: 16/08/2023 CHALLONGE Integration and handling https://api.challonge.com/v1
    public Brackets()
    {

    }



    @Nullable
    public UUID startShowdownBattle(Player player, Player secondPlayer) {
        AtomicReference<UUID> matchUUID = new AtomicReference<>();
        if (player.getOptionalServerPlayer().isPresent() && secondPlayer.getOptionalServerPlayer().isPresent()) {
            BattleStartResult battleStartResult = BattleBuilder.INSTANCE.pvp1v1(player.getOptionalServerPlayer().get(),
                    secondPlayer.getOptionalServerPlayer().get(),
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    false,
                    true, trainer -> {
                        PartyStore party = null;
                        try {
                            party = Cobblemon.INSTANCE.getStorage().getParty(trainer.getUUID());
                        } catch (NoPokemonStoreException e) {

                        }
                        return party;
                    });
            battleStartResult.ifSuccessful(pokemonBattle -> {
                try {
                    if (pokemonBattle.getBattleId() != null) {
                        matchUUID.set(pokemonBattle.getBattleId());
                    }
                } catch (Exception ignored)
                {

                }
                return Unit.INSTANCE;
            });
        }
        return matchUUID.get();
    }

    public boolean startMatch(Player player, Player secondPlayer)
    {
        //do queue checks
        if (player.isOnline())
        {
            if (secondPlayer.isOnline()) {
                if (!player.uuid.equals(secondPlayer.uuid)) {

                    //initiate showdown battle
                    UUID battleUUID = startShowdownBattle(player, secondPlayer);
                    if (battleUUID != null) {
                        //maybe get match and update rather than create a new one?
                        Match match = new Match(battleUUID, player.uuid, secondPlayer.uuid);

                        CobblemonTournamentsForge.config.tournamentManager.activeBattles.put(battleUUID, match);
                        return true;
                    } else {
                        player.sendMessage("&cSomething went wrong while starting the showdown match... Please report this to the developer with a description to fix a potential (cobblemon) bug.");
                        secondPlayer.sendMessage("&cSomething went wrong while starting the showdown match... Please report this to the developer with a description to fix a potential (cobblemon) bug.");
                    }
                }
            } else {
                player.sendMessage("&cSomething went wrong while we were trying to load your battle... Please try queueing again!");
            }
        }
        return false;
    }

}

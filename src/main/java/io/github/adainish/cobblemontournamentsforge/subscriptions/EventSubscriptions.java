package io.github.adainish.cobblemontournamentsforge.subscriptions;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import io.github.adainish.cobblemontournamentsforge.obj.matches.Match;
import io.github.adainish.cobblemontournamentsforge.obj.player.Player;
import io.github.adainish.cobblemontournamentsforge.storage.PlayerStorage;
import kotlin.Unit;

import java.util.UUID;

public class EventSubscriptions
{
    public EventSubscriptions()
    {
        init();
    }

    public void loadPlayerSubscriptions()
    {
        CobblemonEvents.PLAYER_JOIN.subscribe(Priority.NORMAL, serverPlayer -> {

            Player player = PlayerStorage.getPlayer(serverPlayer.getUUID());
            if (player == null) {
                PlayerStorage.makePlayer(serverPlayer.getUUID());
                player = PlayerStorage.getPlayer(serverPlayer.getUUID());

            }

            if (player != null) {
                player.setUsername(serverPlayer.getName().getString());
                player.updateCache();
            }

            return Unit.INSTANCE;
        });
        CobblemonEvents.PLAYER_QUIT.subscribe(Priority.NORMAL, serverPlayer -> {

            if (serverPlayer != null) {
                Player player = PlayerStorage.getPlayer(serverPlayer.getUUID());
                if (player != null) {
                    player.save();
                }
            }
            return Unit.INSTANCE;
        });
    }

    public void loadBattleSubscriptions()
    {

        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, battleVictoryEvent -> {
            UUID battleUUID = battleVictoryEvent.getBattle().getBattleId();
            if (!battleVictoryEvent.getBattle().isPvP()) {
                return Unit.INSTANCE;
            }
            if (!CobblemonTournamentsForge.config.tournamentManager.activeBattles.containsKey(battleUUID)) {
                return Unit.INSTANCE;
            }

            Match battle = CobblemonTournamentsForge.config.tournamentManager.activeBattles.get(battleUUID);

            BattleActor winner = null;
            BattleActor loser = null;

            //set player and secondPlayer
            Player player = PlayerStorage.getPlayer(battle.playerOneUUID);
            Player secondPlayer = PlayerStorage.getPlayer(battle.playerTwoUUID);

            for (BattleActor battleActor : battleVictoryEvent.getBattle().getActors()) {
                if (battleVictoryEvent.getWinners().contains(battleActor))
                    winner = battleActor;
                else
                    loser = battleActor;
            }
            if (winner == null || loser == null) {
                CobblemonTournamentsForge.getLog().warn("Could not finish a presumed tournaments battle, either the winner or loser data could not be found.");
                return Unit.INSTANCE;
            }
            if (player == null || secondPlayer == null) {
                CobblemonTournamentsForge.getLog().warn("Could not finish a presumed tournaments battle, the player or secondary player data for the battle could not be found.");
                return Unit.INSTANCE;
            }
            battle.declareResults(winner.getUuid(), loser.getUuid());

            //do end of battle handling

            if (battle.isWinner(player.uuid))
            {

            } else {


            }
            player.healParty();
            secondPlayer.healParty();

            player.updateCache();
            secondPlayer.updateCache();
            //remove battle from cache
            CobblemonTournamentsForge.config.tournamentManager.activeBattles.remove(battleUUID);

            return Unit.INSTANCE;
        });
    }

    public void init()
    {
        loadPlayerSubscriptions();
        loadBattleSubscriptions();
    }
}

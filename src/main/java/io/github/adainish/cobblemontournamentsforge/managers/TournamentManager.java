package io.github.adainish.cobblemontournamentsforge.managers;

import io.github.adainish.cobblemontournamentsforge.obj.Tournament;
import io.github.adainish.cobblemontournamentsforge.obj.matches.Match;
import io.github.adainish.cobblemontournamentsforge.obj.data.TournamentFormat;

import java.util.HashMap;
import java.util.UUID;

public class TournamentManager
{

    public transient Tournament activeTournament;

    public transient long lastEndedTournament;

    public HashMap<String, TournamentFormat> tournamentFormats = new HashMap<>();

    public transient HashMap<UUID, Match> activeBattles = new HashMap<>();


    public TournamentManager()
    {
        this.init();
    }

    public void init()
    {
        if (tournamentFormats.isEmpty())
        {
            for (DefaultFormats format:DefaultFormats.values()) {
                TournamentFormat tournamentFormat = new TournamentFormat(format.name().toLowerCase());
                tournamentFormat.battleRules.bannedAbilities.add("speedboost");
                tournamentFormat.battleRules.bannedMoves.add("batonpass");
                switch (format)
                {
                    case NU -> {
                        tournamentFormat.prettyFormatName = "&bNever Used";
                    }
                    case RU ->
                    {
                        tournamentFormat.prettyFormatName = "&bRarely Used";
                        tournamentFormat.battleRules.bannedMoves.add("auroraveil");
                    }
                    case UU ->
                    {
                        tournamentFormat.prettyFormatName = "&bUnder Used";
                        tournamentFormat.battleRules.bannedAbilities.add("drizzle");
                        tournamentFormat.battleRules.bannedAbilities.add("drought");
                    }
                    case OU -> {
                        tournamentFormat.prettyFormatName = "&bOver Used";
                        tournamentFormat.battleRules.bannedAbilities.add("arenatrap");
                        tournamentFormat.battleRules.bannedAbilities.add("powerconstruct");
                        tournamentFormat.battleRules.bannedAbilities.add("shadowtag");
                    }
                    case UBER -> {
                        tournamentFormat.prettyFormatName = "&bUber";
                        tournamentFormat.battleRules.bannedAbilities.clear();
                        tournamentFormat.battleRules.bannedSpecies.clear();
                    }
                    case AG -> {
                        tournamentFormat.prettyFormatName = "&bAnything Goes";
                        tournamentFormat.battleRules.bannedAbilities.clear();
                        tournamentFormat.battleRules.bannedSpecies.clear();
                        tournamentFormat.battleRules.bannedMoves.clear();
                        tournamentFormat.battleRules.bannedForms.clear();
                        tournamentFormat.battleRules.bannedItems.clear();

                    }
                }
            }
        }
    }

}

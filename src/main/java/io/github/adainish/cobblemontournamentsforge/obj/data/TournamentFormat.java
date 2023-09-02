package io.github.adainish.cobblemontournamentsforge.obj.data;


import java.util.ArrayList;
import java.util.List;

public class TournamentFormat
{
    public String formatID;
    public String prettyFormatName = "";
    public List<String> prettyFormatLore = new ArrayList<>();
    public int minimumPlayerEntries = 4;
    public BattleRules battleRules;
    public boolean useRentals = false;
    public List<String> possibleRewards = new ArrayList<>();

    public TournamentFormat(String id)
    {
        this.formatID = id;
        this.battleRules = new BattleRules();
    }
}

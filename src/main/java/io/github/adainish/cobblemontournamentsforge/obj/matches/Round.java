package io.github.adainish.cobblemontournamentsforge.obj.matches;

import io.github.adainish.cobblemontournamentsforge.util.RandomHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Round
{
    public int roundNumber = 1;
    public List<UUID> roundPlayers = new ArrayList<>();
    public List<UUID> advanced = new ArrayList<>();

    public Bracket bracket = new Bracket();

    public Round(int round, List<UUID> roundPlayers)
    {
        this.roundNumber = round;
        this.roundPlayers = roundPlayers;
        this.generate();
    }


    public boolean isFinished() {
        if (!bracket.finishedMatches.isEmpty())
            return bracket.plannedMatches.isEmpty();
        else return false;
    }

    public void generate()
    {
        this.bracket.plannedMatches.addAll(generateMatches());
    }

    public List<Match> generateMatches()
    {
        List<Match> generatedMatches = new ArrayList<>();
        List<UUID> toUseList = new ArrayList<>(this.roundPlayers);
        List<UUID> usedUUIDS = new ArrayList<>();
        while (!toUseList.isEmpty())
        {
            if (toUseList.size() == 1)
            {
                //automatically advance player as they're the only ones left.
                this.advanced.add(toUseList.stream().findFirst().get());
                break;

            }
            UUID firstPlayer = RandomHelper.getRandomElementFromCollection(toUseList);
            if (usedUUIDS.contains(firstPlayer))
                continue;
            UUID secondPlayer = RandomHelper.getRandomElementFromCollection(toUseList);
            if (usedUUIDS.contains(secondPlayer))
                continue;
            usedUUIDS.add(firstPlayer);
            usedUUIDS.add(secondPlayer);
            //generate match
            Match match = new Match(firstPlayer, secondPlayer);
            generatedMatches.add(match);
        }
        return generatedMatches;
    }
}

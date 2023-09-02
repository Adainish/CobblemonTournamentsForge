package io.github.adainish.cobblemontournamentsforge.obj.matches;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Bracket
{
    public List<Match> finishedMatches = new ArrayList<>();
    public List<Match> plannedMatches = new ArrayList<>();

    public Optional<Match> getNextMatch()
    {
        Optional<Match> optionalMatch = Optional.empty();

        if (!plannedMatches.isEmpty())
            optionalMatch = Optional.of(plannedMatches.get(0));

        return optionalMatch;
    }

    public UUID finishMatch(Match match, UUID winner, UUID loser)
    {
        this.plannedMatches.remove(match);
        match.setWinnerUUID(winner);
        match.setLoserUUID(loser);
        this.finishedMatches.add(match);
        return match.winnerUUID;
    }

}

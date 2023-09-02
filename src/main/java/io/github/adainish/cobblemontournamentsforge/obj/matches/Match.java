package io.github.adainish.cobblemontournamentsforge.obj.matches;

import java.util.UUID;

public class Match
{
    public UUID battleUUID;
    public UUID playerOneUUID;
    public UUID playerTwoUUID;
    public UUID winnerUUID;
    public UUID loserUUID;

    public Match(UUID playerOneUUID, UUID playerTwoUUID) {
        this.playerOneUUID = playerOneUUID;
        this.playerTwoUUID = playerTwoUUID;
    }

    public Match(UUID battleUUID, UUID playerOneUUID, UUID playerTwoUUID) {
        this.battleUUID = battleUUID;
        this.playerOneUUID = playerOneUUID;
        this.playerTwoUUID = playerTwoUUID;
    }

    public void declareResults(UUID winner, UUID loserUUID)
    {
        this.setWinnerUUID(winner);
        this.setLoserUUID(loserUUID);
    }



    public void setWinnerUUID(UUID uuid)
    {
        this.winnerUUID = uuid;
    }

    public void setLoserUUID(UUID uuid)
    {
        this.loserUUID = uuid;
    }

    public boolean isWinner(UUID uuid)
    {
        return this.winnerUUID.equals(uuid);
    }

    public boolean isFinished()
    {
        return winnerUUID != null && loserUUID != null;
    }
}

package io.github.adainish.cobblemontournamentsforge.obj.arena;

public class Arena
{
    public Location playerOneLocation;
    public Location playerTwoLocation;


    public Arena()
    {
        playerOneLocation = new Location();
        playerTwoLocation = new Location();
    }

    public boolean isAvailable()
    {
        return playerOneLocation.isAvailable() && playerTwoLocation.isAvailable();
    }
}

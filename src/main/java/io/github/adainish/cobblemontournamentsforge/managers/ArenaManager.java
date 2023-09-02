package io.github.adainish.cobblemontournamentsforge.managers;

import io.github.adainish.cobblemontournamentsforge.obj.arena.Arena;
import io.github.adainish.cobblemontournamentsforge.obj.arena.Location;
import io.github.adainish.cobblemontournamentsforge.util.RandomHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArenaManager
{
    public boolean useConfiguredArenas = false;
    public List<Arena> arenas = new ArrayList<>();

    public ArenaManager()
    {
        init();
    }

    public void init()
    {
        if (this.arenas.isEmpty())
        {
            Arena arena = new Arena();
            Location location = new Location();
            location.levelName = "minecraft:spawn";
            location.posX = 100.0;
            location.posY = 100.0;
            location.posZ = 100.0;
            arena.playerTwoLocation = location;
            Location secondLocation = new Location();
            secondLocation.levelName = "minecraft:spawn";
            secondLocation.posX = 100.0;
            secondLocation.posY = 100.0;
            secondLocation.posZ = 100.0;
            arena.playerOneLocation = secondLocation;
            arenas.add(arena);
        }
    }
    public List<Arena> getAvailableArenas()
    {
        List<Arena> arenaList = new ArrayList<>();

        this.arenas.forEach(arena -> {
            if (arena.isAvailable())
                arenaList.add(arena);
        });

        return arenaList;
    }

    public Optional<Arena> getRandomOptionalAvailableArena()
    {
        Optional<Arena> optionalArena = Optional.empty();
        List<Arena> arenaList = getAvailableArenas();
        if (!arenaList.isEmpty())
        {
            optionalArena = Optional.ofNullable(RandomHelper.getRandomElementFromCollection(arenaList));
        }
        return optionalArena;
    }
}

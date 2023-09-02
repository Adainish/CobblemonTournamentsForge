package io.github.adainish.cobblemontournamentsforge.cache;

import io.github.adainish.cobblemontournamentsforge.obj.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerCache
{
    public HashMap<UUID, Player> playerCache = new HashMap<UUID, Player>();
}

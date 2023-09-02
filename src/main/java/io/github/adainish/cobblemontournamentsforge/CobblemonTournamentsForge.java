package io.github.adainish.cobblemontournamentsforge;

import ca.landonjw.gooeylibs2.api.tasks.Task;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import io.github.adainish.cobblemontournamentsforge.cache.PlayerCache;
import io.github.adainish.cobblemontournamentsforge.config.Config;
import io.github.adainish.cobblemontournamentsforge.config.LanguageConfig;
import io.github.adainish.cobblemontournamentsforge.obj.player.Player;
import io.github.adainish.cobblemontournamentsforge.subscriptions.EventSubscriptions;
import kotlin.Unit;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CobblemonTournamentsForge.MODID)
public class CobblemonTournamentsForge {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobblemontournamentsforge";

    public static CobblemonTournamentsForge instance;
    public static final String MOD_NAME = "CobblemonTournaments";
    public static final String VERSION = "1.0.0-Beta";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2023";
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MOD_NAME);
    private static File configDir;
    private static File storageDir;

    private static File playerStorageDir;
    private static MinecraftServer server;

    public static Logger getLog() {
        return log;
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static void setServer(MinecraftServer server) {
        CobblemonTournamentsForge.server = server;
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        CobblemonTournamentsForge.configDir = configDir;
    }
    public static File getStorageDir() {
        return storageDir;
    }

    public static void setStorageDir(File storageDir) {
        CobblemonTournamentsForge.storageDir = storageDir;
    }
    public static File getPlayerStorageDir() {
        return playerStorageDir;
    }


    public static void setPlayerStorageDir(File playerStorageDir) {
        CobblemonTournamentsForge.playerStorageDir = playerStorageDir;
    }
    public static PlayerCache playerCache;
    public static EventSubscriptions eventSubscriptions;
    public static Config config;
    public static LanguageConfig languageConfig;

    public CobblemonTournamentsForge() {
        instance = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );

        CobblemonEvents.SERVER_STARTED.subscribe(Priority.NORMAL, minecraftServer -> {
            setServer(minecraftServer);
            //load data from config
            reload();
            //register subscription listeners
            eventSubscriptions = new EventSubscriptions();

            return Unit.INSTANCE;
        });
    }

    public void savePlayerData(boolean shutdown)
    {
        log.warn("Saving all player data that's still cached for ranked battles...");
        List<Player> playerList = new ArrayList<>(playerCache.playerCache.values());
        if (!playerList.isEmpty()) {
            log.warn("Saving cached player data...");
            playerList.forEach(Player::save);
            if (shutdown)
                playerCache.playerCache.clear();
        } else log.warn("No players detected to save!");
    }

    public void initDirs() {
        setConfigDir(new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()) + "/%modname%/".replace("%modname%", MOD_NAME)));
        getConfigDir().mkdir();
        setStorageDir(new File(configDir, "/storage/"));
        getStorageDir().mkdirs();
        setPlayerStorageDir(new File(storageDir, "/players/"));
        getPlayerStorageDir().mkdirs();
    }

    public void save() {
        log.warn("Saving config, and tournament manager data...");
        if (config != null)
            Config.saveConfig(config);
        else log.warn("Failed to save");
    }

    public void initConfigs() {
        log.warn("Loading Config Files");

        Config.writeConfig();
        config = Config.getConfig();
        LanguageConfig.writeConfig();
        languageConfig = LanguageConfig.getConfig();
        log.warn("Loaded Config files");
    }

    public void reload() {
        initDirs();
        initConfigs();
    }
}

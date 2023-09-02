package io.github.adainish.cobblemontournamentsforge.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import io.github.adainish.cobblemontournamentsforge.util.Adapters;

import java.io.*;

public class LanguageConfig
{
    public String prefix;
    public String splitter;

    public LanguageConfig()
    {
        this.prefix = "&6[&bTournaments&6]";
        this.splitter = " &e» ";
    }

    public static void writeConfig()
    {
        File dir = CobblemonTournamentsForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        LanguageConfig config = new LanguageConfig();
        try {
            File file = new File(dir, "language.json");
            if (file.exists())
                return;
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String json = gson.toJson(config);
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            CobblemonTournamentsForge.getLog().warn(e);
        }
    }

    public static void saveConfig(LanguageConfig config)
    {
        File dir = CobblemonTournamentsForge.getConfigDir();
        dir.mkdirs();
        File file = new File(dir, "language.json");
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            CobblemonTournamentsForge.getLog().error("Something went wrong attempting to read the Language Config");
            return;
        }


        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(config));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static LanguageConfig getConfig()
    {
        File dir = CobblemonTournamentsForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "language.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobblemonTournamentsForge.getLog().error("Something went wrong attempting to read the Language Config");
            return null;
        }

        return gson.fromJson(reader, LanguageConfig.class);
    }
}

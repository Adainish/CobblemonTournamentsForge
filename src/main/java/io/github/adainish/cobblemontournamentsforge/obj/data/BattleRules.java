package io.github.adainish.cobblemontournamentsforge.obj.data;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import io.github.adainish.cobblemontournamentsforge.CobblemonTournamentsForge;
import io.github.adainish.cobblemontournamentsforge.obj.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BattleRules
{
    public List<String> bannedSpecies = new ArrayList<>();
    public List<String> bannedForms = new ArrayList<>();
    public List<String> bannedMoves = new ArrayList<>();
    public List<String> bannedAbilities = new ArrayList<>();
    public List<ItemStack> bannedItems = new ArrayList<>();

    public BattleRules()
    {
        this.init();
    }

    public void init()
    {
        CobblemonTournamentsForge.getLog().warn("Initialising battle rules...");
        this.initSpecies();
        this.initForms();
        this.initMoves();
        this.initAbilities();
        this.initItems();
    }

    public void initSpecies()
    {
        CobblemonTournamentsForge.getLog().warn("Generating blacklisted species ids...");
        for (Species species: PokemonSpecies.INSTANCE.getImplemented()) {
            Pokemon pokemon = species.create(1);
            if (pokemon.isLegendary() || pokemon.isUltraBeast())
                this.bannedSpecies.add(species.getResourceIdentifier().toString());
        }
    }

    public void initForms()
    {
        CobblemonTournamentsForge.getLog().warn("Generating blacklisted form ids...");
        this.bannedForms.add("alolan");
    }

    public void initMoves()
    {
        CobblemonTournamentsForge.getLog().warn("Generating blacklisted moves...");
        this.bannedMoves.add("splash");
    }

    public void initAbilities()
    {
        CobblemonTournamentsForge.getLog().warn("Generating blacklisted abilities...");
        this.bannedAbilities.add("moody");
    }

    public void initItems()
    {
        this.bannedItems.add(new ItemStack(CobblemonItems.EXP_SHARE.get()));
    }

    public boolean isTeamLegal(Player player)
    {
        AtomicBoolean isLegal = new AtomicBoolean(true);

        if (player.getOptionalPlayerPartyStore().isPresent()) {
            PartyStore partyStore = player.getOptionalPlayerPartyStore().get();
            partyStore.forEach(pokemon -> {
                if (!isLegal(player, pokemon))
                {
                    isLegal.set(false);
                }
            });
        } else isLegal.set(false);
        return isLegal.get();
    }

    public boolean isLegal(Player player, Pokemon pokemon) {
        AtomicBoolean isLegal = new AtomicBoolean(true);

        if (this.bannedSpecies.contains(pokemon.getSpecies().resourceIdentifier.toString())) {
            if (this.bannedSpecies.contains(pokemon.getSpecies().resourceIdentifier.toString())) {
                isLegal.set(false);
                player.sendMessage("&cYour %species% is illegal in terms of usage in ranked battles."
                        .replace("%species%", pokemon.getSpecies().getName())
                );
                return isLegal.get();
            }
        }

        if (this.bannedForms.contains(pokemon.getForm().getName().toLowerCase())) {
            isLegal.set(false);
            player.sendMessage("&cYour %species% had an illegal form, being %name%"
                    .replace("%species%", pokemon.getSpecies().getName())
                    .replace("%name%", pokemon.getForm().getName())
            );
            return isLegal.get();
        }

        if (!this.bannedItems.isEmpty()) {
            if (!pokemon.heldItem().isEmpty()) {
                this.bannedItems.forEach(item -> {
                    if (item.copy().is(pokemon.heldItem().copy().getItem())) {
                        isLegal.set(false);
                        player.sendMessage("&cYour %species% had an illegal item, being %name%"
                                .replace("%species%", pokemon.getSpecies().getName())
                                .replace("%name%", item.copy().getDisplayName().getString())
                        );
                    }
                });
            }
        }
        pokemon.getMoveSet().getMoves().forEach(move -> {
            if (this.bannedMoves.contains(move.getName())) {
                isLegal.set(false);
                player.sendMessage("&cYour %species% had an illegal move, being %name%"
                        .replace("%species%", pokemon.getSpecies().getName())
                        .replace("%name%", move.getName())
                );
            }
        });

        return isLegal.get();
    }
}

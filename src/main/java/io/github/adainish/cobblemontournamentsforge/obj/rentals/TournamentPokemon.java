package io.github.adainish.cobblemontournamentsforge.obj.rentals;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor;
import com.cobblemon.mod.common.pokemon.Pokemon;

public class TournamentPokemon
{
    public PokemonProperties pokemonProperties;

    public TournamentPokemon(Pokemon pokemon)
    {
        this.pokemonProperties = pokemon.createPokemonProperties(PokemonPropertyExtractor.Companion.getALL()).copy();
    }
}

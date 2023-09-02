package io.github.adainish.cobblemontournamentsforge.obj.rentals;

import java.util.ArrayList;
import java.util.List;

public class RentalTeam
{
    public List<TournamentPokemon> pokemonList = new ArrayList<>();
    public RentalTeam()
    {

    }

    public boolean shouldGiveRentalTeam()
    {
        return false;
    }
}

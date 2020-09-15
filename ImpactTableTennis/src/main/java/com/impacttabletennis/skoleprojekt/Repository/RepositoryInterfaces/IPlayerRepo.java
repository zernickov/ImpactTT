package com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces;

import com.impacttabletennis.skoleprojekt.Model.Player;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Dette er et interface, der bliver implementeret i PlayerRepo klassen.
 * Dette er de metoder, som PlayerRepo klassen, derfor SKAL lave/implementerer.
 */
public interface IPlayerRepo
{
    List<Player> fetchAll();
    Player findPlayerById(String userName);
    void addPlayer(Player player);
    void deletePlayer(String username);
    Player login(String username, String password);
    void revengeELOcheater(String username);
    void updateElo(float elo, HttpServletRequest request) throws Exception;
    String retrievePlayerNameFromInput(String username);
    String retrievePlayerNameFromURL(HttpServletRequest request) throws Exception;
    List<Player> fetchTop10();
    void updatePlayedMatches(HttpServletRequest request) throws Exception;
    float retrievePlayerELO(HttpServletRequest request) throws Exception;
}

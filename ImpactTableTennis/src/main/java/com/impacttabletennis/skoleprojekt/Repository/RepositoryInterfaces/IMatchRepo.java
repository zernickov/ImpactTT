package com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces;

import com.impacttabletennis.skoleprojekt.Model.Match;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Dette er et interface, der bliver implementeret i MatchRepo klassen.
 * Dette er de metoder, som MatchRepo klassen, derfor SKAL lave/implementerer.
 */
public interface IMatchRepo
{
    List<Match> fetchAllCompletedMatches();
    List<Match> fetchRecentCompletedMatches();
    List<Match> fetchAllUpcoming();
    Match fetchCurrentMatch(int id);
    void addMatch(Match match, HttpServletRequest request) throws Exception;
    void completeMatch(int id, HttpServletRequest request) throws Exception;
    void declareWinner(String userName, HttpServletRequest request) throws Exception;
    void declareLoser(String userName, HttpServletRequest request) throws Exception;
    Match checkForCurrentMatch(int id);
    int findHighestMatchID();
}

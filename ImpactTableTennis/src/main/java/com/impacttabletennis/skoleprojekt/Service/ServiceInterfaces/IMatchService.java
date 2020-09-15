package com.impacttabletennis.skoleprojekt.Service.ServiceInterfaces;

import com.impacttabletennis.skoleprojekt.Model.Match;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * Dette er et interface, der bliver implementeret i MatchService klassen.
 * Dette er de metoder, som MatchService klassen, derfor SKAL lave/implementerer.
 */
public interface IMatchService
{
    boolean checkForPlayer2(int id);
    void validateMatch(Match match, HttpServletRequest request) throws Exception;
    void showCurrentMatch(Model model);
}

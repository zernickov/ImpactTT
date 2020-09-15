package com.impacttabletennis.skoleprojekt.Service.ServiceInterfaces;

import javax.servlet.http.HttpServletRequest;

/**
 * Dette er et interface, der bliver implementeret i PlayerService klassen.
 * Dette er de metoder, som PlayerService klassen, derfor SKAL lave/implementerer.
 */
public interface IPlayerService
{
    String retrieveUsername(HttpServletRequest request) throws Exception;
    float Probability(float rating1, float rating2);
    float EloRating(float Ra, float Rb, int K, boolean d, boolean check);
}

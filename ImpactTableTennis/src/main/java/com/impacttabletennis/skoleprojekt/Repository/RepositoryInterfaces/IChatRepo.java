package com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces;

import com.impacttabletennis.skoleprojekt.Model.Chat;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Dette er et interface, der bliver implementeret i ChatRepo klassen.
 * Dette er de metoder, som ChatRepo klassen, derfor SKAL lave/implementerer.
 */
public interface IChatRepo
{
    List<Chat> fetchAll();  // Henter alt data fra database tabellen Chat
    void addChat(Chat chat, HttpServletRequest request) throws Exception; // Tilføjer en person og aflevere en Chat række

}

package com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces;

import com.impacttabletennis.skoleprojekt.Model.SystemLog;

import java.util.List;

/**
 * Dette er et interface, der bliver implementeret i SystemLogRepo klassen.
 * Dette er de metoder, som SystemLogRepo klassen, derfor SKAL lave/implementerer.
 */
public interface ISystemLogRepo
{
    void addAction(SystemLog systemLog, String sysAction);
    List<SystemLog> fetchAllActions();
}

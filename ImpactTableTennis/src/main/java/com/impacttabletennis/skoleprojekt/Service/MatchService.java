package com.impacttabletennis.skoleprojekt.Service;

import com.impacttabletennis.skoleprojekt.Model.Match;
import com.impacttabletennis.skoleprojekt.Repository.MatchRepo;
import com.impacttabletennis.skoleprojekt.Service.ServiceInterfaces.IMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * Klassen er annoteret med @Service, hvilket fortæller at det er her der vil blive anvendt logik.
 * Dette resulterer i at der bliver trukket logik/kode udfra både Repo og controller klasserne, for at opdele ansvarsområder.
 */
@Service
public class MatchService implements IMatchService
{
    /**
     * Denne annotation står for at lave dependency injection på et objekt,
     * bruges på fields, setter metoder og konstruktorer
     */
    @Autowired
    private MatchRepo matchRepo;

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param id en int som parameter, hvis indhold specificeret når metoden kaldes.
     * @return returnerer en boolean.
     * Denne metoder benytter sig af checkForCurrentMatch() metoden fra MatchRepo, hvoraf der er anvendt logik på, i form af et conditional statement (if-statement)
     */
    @Override
    public boolean checkForPlayer2(int id)
    {
        boolean check = false;

        if (matchRepo.checkForCurrentMatch(id).getPlayer2().length()>3)
        {
            check = true;
        }
        return check;
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param match parameter som tager en instans af Match klassen.
     * @param request instans af HttpServletRequest.
     * @throws Exception, da HttpServletRequest kan give en Exception.
     * det er en void metodeda den ikke returnerer noget, men derimod kalder andre metoder på baggrund af if-statements.
     */
    @Override
    public void validateMatch(Match match, HttpServletRequest request) throws Exception
    {
        if(checkForPlayer2(matchRepo.findHighestMatchID()))
        {
            matchRepo.addMatch(match, request);
        }
        else if(!checkForPlayer2(matchRepo.findHighestMatchID()))
        {
            matchRepo.completeMatch(matchRepo.findHighestMatchID(), request);
        }
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param model tager et model objekt som parameter.
     * en void metode da den ikke returnerer noget.
     */
    @Override
    public void showCurrentMatch(Model model)
    {
        if(!checkForPlayer2(matchRepo.findHighestMatchID()))
        {
            model.addAttribute("nextMatch", matchRepo.fetchCurrentMatch(matchRepo.findHighestMatchID()));
        }
    }



}

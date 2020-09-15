package com.impacttabletennis.skoleprojekt.Repository;

import com.impacttabletennis.skoleprojekt.Model.SystemLog;
import com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces.ISystemLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Annotation for at indikerer at dette er en repository klasse eller DAO klasse.
 * Har en indbygget oversættelses feature, så der eksempelvis ikke behøves en try catch blok,
 * da der er en handler til at klare exceptions.
 */
@Repository
public class SystemLogRepo implements ISystemLogRepo
{
    /**
     * Denne annotation står for at lave dependency injection på et objekt,
     * bruges på fields, setter metoder og konstruktorer
     */
    @Autowired
    private JdbcTemplate template;

    /*
    //Singleton Logger

    private SystemLogRepo systemLogRepoInstance = new SystemLogRepo();

    private SystemLogRepo(){

    }

    public static getSystemLogRepo() {
        return systemLogRepoInstance;
    }


    //brug/kald i anden klasse:
    SystemLogRepo systemLogRepo = SystemLogRepo.getSystemLogRepo();
    systemLogRepo.addAction();

     */

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param systemLog tager en instans af SystemLog klassen som parameter.
     * @param sysAction en String, som specificere hvilken handling der sker når metoden bliver kaldt, hvilket er det som bliver sat ind i databasen.
     */
    @Override
    public void addAction(SystemLog systemLog, String sysAction) {
        template.update("INSERT INTO skoleprojekt.systemlog (systemlogID, systemAction, timeOfAction) VALUES (?, ?, CURRENT_TIMESTAMP + INTERVAL 8 HOUR)",
                systemLog.getSystemlogID(), sysAction
        );
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @return returnerer alt fra systemlog tabellen i form af en List. Hvoraf det er sorteret efter timeOfAction, for at få de nyeste handlinger først.
     */
    @Override
    public List<SystemLog> fetchAllActions()
    {
        RowMapper<SystemLog> rowMapper = new BeanPropertyRowMapper<>(SystemLog.class);
        return template.query("SELECT * FROM systemlog ORDER BY timeOfAction DESC", rowMapper);
    }


}

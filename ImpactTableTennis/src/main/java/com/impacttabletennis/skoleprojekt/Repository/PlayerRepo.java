package com.impacttabletennis.skoleprojekt.Repository;


import com.impacttabletennis.skoleprojekt.Model.Player;
import com.impacttabletennis.skoleprojekt.Model.SystemLog;
import com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces.IPlayerRepo;
import com.impacttabletennis.skoleprojekt.Service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Annotation for at indikerer at dette er en repository klasse eller DAO klasse.
 * Har en indbygget oversættelses feature, så der eksempelvis ikke behøves en try catch blok,
 * da der er en handler til at klare exceptions.
 */
@Repository
public class PlayerRepo implements IPlayerRepo
{
    /**
     * Denne annotation står for at lave dependency injection på et objekt,
     * bruges på fields, setter metoder og konstruktorer
     */
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SystemLogRepo systemLogRepo;

    RowMapper<Player> rowMapper = new BeanPropertyRowMapper<>(Player.class);

    @Override
    public List<Player> fetchAll()
    {
        return template.query("SELECT * FROM player", rowMapper);
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param request instans af HttpServletRequest.
     * @throws Exception, da HttpServletRequest kan give en Exception.
     * Det første prepared statement selecter playedMatches kolonnen fra player, ved et givet userName.
     * Det næste opdaterer dernæst playedMatches og tilføjer 1.
     */
    @Override
    public void updatePlayedMatches(HttpServletRequest request) throws Exception
    {
        Player player = template.query("SELECT playedMatches FROM player WHERE userName=?", rowMapper, playerService.retrieveUsername(request)).get(0);

        template.update("UPDATE skoleprojekt.player SET playedMatches=? WHERE userName=?",
                player.getPlayedMatches()+1, playerService.retrieveUsername(request)
        );
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @return de 10 spillere med den højeste ELO i form af en List
     */
    @Override
    public List<Player> fetchTop10()
    {
        return template.query("SELECT * FROM player ORDER BY ELO DESC LIMIT 10", rowMapper);
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param request instans af HttpServletRequest
     * @return returnerer en String, der er sammensat af firstname og lastname, fra playerobjektet.
     * @throws Exception, da HttpServletRequest kan give en Exception.
     */
    @Override
    public String retrievePlayerNameFromURL(HttpServletRequest request) throws Exception
    {
        Player player = template.query("SELECT firstName, lastName FROM player WHERE userName=?", rowMapper,playerService.retrieveUsername(request)).get(0);

        return player.getFirstName()+" "+player.getLastName();
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param username String i parameteren, som bliver brugt som værdi i prepared statement, da det specificeres når metoden kaldes.
     * @return returnerer firstname og lastname fra playerobjektet.
     */
    @Override
    public String retrievePlayerNameFromInput(String username)
    {
        Player player = template.query("SELECT firstName, lastName FROM player WHERE userName=?", rowMapper,username).get(0);

        return player.getFirstName()+" "+player.getLastName();
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param userName String i parameteren, som bliver brugt som værdi i prepared statement, da det specificeres når metoden kaldes.
     * @return returnerer et player object, som vil være én række i player tabellen.
     */
    @Override
    public Player findPlayerById(String userName) {
        return template.query("SELECT * FROM player WHERE userName=?", rowMapper,userName).get(0);
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param player tager et player objekt som parameter.
     * Metoden tilføjer i form af et prepared statement en ny række til player tabellen.
     * Metoden addAction() fra SystemLogRepo kaldes også her, da det skal tilføjes til systemlog tabellen
     */
    @Override
    public void addPlayer(Player player) {
        template.update("INSERT INTO skoleprojekt.player (playerID, userName, userCode, firstName, lastName, ELO, rank, rival, playedMatches, isOnline) VALUES (?, ?, ?, ?, ?, 1000, ?, ?, ?, ?)",
                player.getPlayerID(), player.getUserName(), player.getUserCode(), player.getFirstName(), player.getLastName(), player.getRank(), player.getRival(), player.getPlayedMatches(), player.isOnline()
        );
        SystemLog systemLog = new SystemLog();
        systemLogRepo.addAction(systemLog, player.getFirstName()+" "+player.getLastName()+" created a new account");
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param username, password: parametre hvis indhold specificeret når metoden bliver kaldt.
     * @return returnerer en row, hvor username og password er lig med parameterens input.
     */
    @Override
    public Player login(String username, String password) {
        return template.query("SELECT * FROM player WHERE userName=? AND userCode=?", rowMapper,username, password).get(0);
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param elo parameter hvis indhold bliver specificeret når metoden bliver kaldt.
     * @param request instans af HttpServletRequest
     * @throws Exception, da HttpServletRequest kan give en Exception.
     * en void metode, da den ikke returnerer noget, men blot opdatere i databasen.
     */
    @Override
    public void updateElo(float elo, HttpServletRequest request) throws Exception
    {
        template.update("UPDATE skoleprojekt.player SET ELO=? WHERE userName=?",
                elo, playerService.retrieveUsername(request)
        );
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param username parameter hvis indhold bliver specificeret når metoden bliver kaldt.
     * En void metode da den blot sletter noget fra databasen og ikke skal returnere noget.
     */
    @Override
    public void deletePlayer(String username)
    {
        template.update("DELETE FROM skoleprojekt.player WHERE userName=?",
                username
        );
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param username parameter hvis indhold bliver specificeret når metoden bliver kaldt.
     * Void metode da den blot opdatere databasen og ikke returnerer noget.
     * Metoden decreaser ELO hos en spiller med -200
     */
    @Override
    public void revengeELOcheater(String username)
    {
        template.update("UPDATE skoleprojekt.player SET ELO=ELO-200 WHERE userName=?",
                username
        );
    }

    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param request instans af HttpServletRequest, som bliver brugt da den manglende værdi i vores prepared statement kalder retrieveusername metoden.
     * @return returnerer en float, som er en spillers ELO
     * @throws Exception, da HttpServletRequest kan give en Exception.
     */
    @Override
    public float retrievePlayerELO(HttpServletRequest request) throws Exception {
        Player player = template.query("SELECT ELO FROM player WHERE userName=?", rowMapper, playerService.retrieveUsername(request)).get(0);

        return player.getELO();
    }

}

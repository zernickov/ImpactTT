package com.impacttabletennis.skoleprojekt.Repository;

import com.impacttabletennis.skoleprojekt.Model.Match;
import com.impacttabletennis.skoleprojekt.Model.SystemLog;
import com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces.IMatchRepo;
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
public class MatchRepo implements IMatchRepo {

    /**
     * Denne annotation står for at lave dependency injection på et objekt,
     * bruges på fields, setter metoder og konstruktorer
     */
    @Autowired
    public JdbcTemplate template;

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SystemLogRepo systemLogRepo;

    /**
     * en instans af SystemLog, da den bliver brugt i mange af metoderne
     */
    SystemLog systemLog = new SystemLog();

    /**
     * en instans af Rowmapper, da den bliver brugt i mange af metoderne.
     */
    RowMapper<Match> rowMapper = new BeanPropertyRowMapper<>(Match.class);

    /**
     * annoteret med @Override, da det er en metoder fra det implementeret interface.
     * @return returnerer i form af en List alt (*) fra tabletennismatch med en where-clause i form af et prepared statement.
     */
    @Override
    public List<Match> fetchAllCompletedMatches() {
        return template.query("SELECT * FROM tabletennismatch WHERE loser<>'N/A' AND winner<>'N/A' ORDER BY dateOfMatch DESC", rowMapper);
    }

    /**
     * annoteret med @Override, da det er en metoder fra det implementeret interface.
     * @return returnerer i form af en List også alt (*) fra tabletennismatch med en anden where-clause.
     */
    @Override
    public List<Match> fetchRecentCompletedMatches() {
        return template.query("SELECT * FROM tabletennismatch WHERE loser<>'N/A' AND winner<>'N/A' ORDER BY matchID DESC LIMIT 10", rowMapper);
    }

    /**
     * annoteret med @Override, da det er en metoder fra det implementeret interface.
     * @return returnerer i form af en List også alt (*) fra tabletennismatch med en anden where-clause.
     */
    @Override
    public List<Match> fetchAllUpcoming() {
        return template.query("SELECT * FROM tabletennismatch WHERE (loser='N/A' OR winner='N/A') and player2<>'N/A'", rowMapper);
    }

    /**
     * annoteret med @Override, da det er en metoder fra det implementeret interface.
     * @return returnerer et enkelt row fra resultatsættet.
     */
    @Override
    public Match fetchCurrentMatch(int id) {
        return template.query("SELECT * FROM tabletennismatch WHERE matchID=?", rowMapper, id).get(0);
    }

    /**
     * annoteret med @Override, da det er en metoder fra det implementeret interface.
     * void metode, da den ikke returnerer noget.
     * vha. .update() metoden fra Jdbctemplate, bliver der skrevet et prepared-statement.
     * Dernæst kaldes addMatch funktionen fra SystemLogRepo, da denne handling skal tilføjes til systemLog tabellen.
     */
    @Override
    public void addMatch(Match match, HttpServletRequest request) throws Exception {
        template.update("INSERT INTO skoleprojekt.tabletennismatch (matchID, player1, player1ELO, player1Name) VALUES (?, ?, ?, ?)",
                match.getMatchID(), playerService.retrieveUsername(request), playerRepo.retrievePlayerELO(request), playerRepo.retrievePlayerNameFromURL(request)
        );
        systemLogRepo.addAction(systemLog, "A match was created");
    }

    /**
     * void metode, da den ikke returnerer noget.
     * @param id som parameter, da det bliver brugt som en værdi til voresp reparedstatement, hvoraf denne værdi bliver sat når metoden kaldes.
     * @param request instans af HttpServletRequest
     * @throws Exception, da der bliver brugt HttpServletRequest, som kan give en Exception.
     */
    @Override
    public void completeMatch(int id, HttpServletRequest request) throws Exception {
        template.update("UPDATE skoleprojekt.tabletennismatch SET player2=?, player2ELO=?, player2Name=?, dateOfMatch=CURRENT_TIMESTAMP + INTERVAL 8 HOUR WHERE matchID=?",
                playerService.retrieveUsername(request), playerRepo.retrievePlayerELO(request), playerRepo.retrievePlayerNameFromURL(request), id
        );

    }

    /**
     * void metode, da den ikke returnerer noget.
     * @param userName som parameter, da det bliver brugt som en værdi til voresp reparedstatement, hvoraf denne værdi bliver sat når metoden kaldes.
     * @param request instans af HttpServletRequest
     * @throws Exception, da der bliver brugt HttpServletRequest, som kan give en Exception.
     * Udover dette bliver der kaldt addAction() metoden, for at gemme det i SystemLog tabellen.
     * Derudover er der anvendt logik i denne Repo metode, da den skal bruge match objektet getter metoder.
     * Disse skal bruges for at kalde updateElo metoden, som i dens parameter skal have en boolean der enten er true eller false.
     */
    @Override
    public void declareWinner(String userName, HttpServletRequest request) throws Exception {
        template.update("UPDATE skoleprojekt.tabletennismatch SET winner=? WHERE (player1=? OR player2=?) AND winner='N/A'",
                playerRepo.retrievePlayerNameFromURL(request), userName, userName
        );

        playerRepo.updatePlayedMatches(request);

        Match match = template.query("SELECT * FROM tabletennismatch WHERE (player1=? OR player2=?) AND matchID=?", rowMapper, userName, userName, findHighestMatchID()).get(0);

        systemLogRepo.addAction(systemLog, match.getWinner() + " won a match");

        boolean d = false;
        if (match.getWinner().equals(match.getPlayer1Name())) {
            d = true;
        }

        boolean check = false;
        if (playerRepo.retrievePlayerNameFromURL(request).equals(match.getWinner())) {
            check = true;
        }

        playerRepo.updateElo(playerService.EloRating(match.getPlayer1ELO(), match.getPlayer2ELO(), 30, d, check), request);
    }

    /**
     * void metode, da den ikke returnerer noget.
     * @param userName som parameter, da det bliver brugt som en værdi til voresp reparedstatement, hvoraf denne værdi bliver sat når metoden kaldes.
     * @param request instans af HttpServletRequest
     * @throws Exception, da der bliver brugt HttpServletRequest, som kan give en Exception.
     * Udover dette bliver der kaldt addAction() metoden, for at gemme det i SystemLog tabellen.
     * Derudover er der anvendt logik i denne Repo metode, da den skal bruge match objektet getter metoder.
     * Disse skal bruges for at kalde updateElo metoden, som i dens parameter skal have en boolean der enten er true eller false.
     */
    @Override
    public void declareLoser(String userName, HttpServletRequest request) throws Exception {
        template.update("UPDATE skoleprojekt.tabletennismatch SET loser=? WHERE (player1=? OR player2=?) AND loser='N/A'",
                playerRepo.retrievePlayerNameFromURL(request), userName, userName
        );

        playerRepo.updatePlayedMatches(request);

        Match match = template.query("SELECT * FROM tabletennismatch WHERE (player1=? OR player2=?) AND matchID=?", rowMapper, userName, userName, findHighestMatchID()).get(0);

        systemLogRepo.addAction(systemLog, match.getLoser() + " lost a match");

        boolean d = false;
        if (match.getLoser().equals(match.getPlayer2Name())) {
            d = true;
        }

        boolean check = true;
        if (playerRepo.retrievePlayerNameFromURL(request).equals(match.getLoser())) {
            check = false;
        }


        playerRepo.updateElo(playerService.EloRating(match.getPlayer1ELO(), match.getPlayer2ELO(), 30, d, check), request);
    }

    /**
     * @param id som parameter da det bliver brugt som værdi i vores prepared statement, denne værdi specificeres nemlig først når metoden kaldes.
     * @return returnerer et match object.
     */
    @Override
    public Match checkForCurrentMatch(int id) {
        Match match = template.query("SELECT * FROM tabletennismatch WHERE matchID=?", rowMapper, id).get(0);
        return match;
    }

    /**
     * bruger .query metoden fra JdbcTemplate, som kræver en rowMapper.
     * @return returnerer en int, som er matchID fra et match objekt.
     */
    @Override
    public int findHighestMatchID() {
        Match match = template.query("SELECT * FROM tabletennismatch ORDER BY matchID DESC", rowMapper).get(0);

        return match.getMatchID();
    }



}
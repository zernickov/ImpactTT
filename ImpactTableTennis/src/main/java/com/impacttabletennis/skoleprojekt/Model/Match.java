package com.impacttabletennis.skoleprojekt.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * annoteret som @Entity, hvilket betyder at denne klasse er mappet til en tabel i databasen.
 * Dette betyder også at alle klassens attributer skal være identiske i stavelse/syntax, i forhold til databasens navne på kolonnerne.
 */
@Entity
public class Match
{
    /**
     * Den første attribut i denne klasse er annoteret med @Id og @GeneratedValue
     * @Id specificerer at denne attribut er en primary key i tabellen.
     * @GeneratedValue da kolonnen også er auto-incrementet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matchID;
    private String player1;
    private String player2;
    private String winner;
    private String loser;
    private String dateOfMatch;
    private float player1ELO;
    private float player2ELO;
    private float player1ELOgain;
    private float player2ELOgain;
    private String player1Name;
    private String player2Name;

    /**
     * default constructor for denne klasse.
     */
    public Match(){

    }

    /**
     * Herunder ses getters og setters til alle klassens attributer.
     * note: alle disse er autogenereret igennem IntelliJ
     */
    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }


    public String getDateOfMatch() {
        return dateOfMatch;
    }

    public void setDateOfMatch(String dateOfMatch) {
        this.dateOfMatch = dateOfMatch;
    }

    public float getPlayer1ELO() {
        return player1ELO;
    }

    public void setPlayer1ELO(float player1ELO) {
        this.player1ELO = player1ELO;
    }

    public float getPlayer2ELO() {
        return player2ELO;
    }

    public void setPlayer2ELO(float player2ELO) {
        this.player2ELO = player2ELO;
    }

    public float getPlayer1ELOgain() {
        return player1ELOgain;
    }

    public void setPlayer1ELOgain(float player1ELOgain) {
        this.player1ELOgain = player1ELOgain;
    }

    public float getPlayer2ELOgain() {
        return player2ELOgain;
    }

    public void setPlayer2ELOgain(float player2ELOgain) {
        this.player2ELOgain = player2ELOgain;
    }
}

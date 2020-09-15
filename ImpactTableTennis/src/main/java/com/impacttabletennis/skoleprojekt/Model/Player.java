package com.impacttabletennis.skoleprojekt.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * annoteret som @Entity, hvilket betyder at denne klasse er mappet til en tabel i databasen.
 * Dette betyder også at alle klassens attributer skal være identiske i stavelse/syntax, i forhold til databasens navne på kolonnerne.
 */
@Entity
public class Player
{
    /**
     * Den første attribut i denne klasse er annoteret med @Id og @GeneratedValue
     * @Id specificerer at denne attribut er en primary key i tabellen.
     * @GeneratedValue da kolonnen også er auto-incrementet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerID;
    /**
     * @NotNull, @Size, @Pattern er alle annotationer, der bliver brugt i forhold til saveUser() metoden i Controller klassen.
     * saveUser() metoden gør brug af @Valid annotationen, samt Bindingresult klassen, som tjekker efter disse annotationer.
     */
    @NotNull
    @Size(min=4, max=20)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String userName;
    @NotNull
    @Size(min=4, max=20)
    private String userCode;
    @NotNull
    @Size(min=2, max=20)
    @Pattern(regexp = "^[A-Za-z]+$")
    private String firstName;
    @NotNull
    @Size(min=2, max=20)
    @Pattern(regexp = "^[A-Za-z]+$")
    private String lastName;
    private float ELO;
    private int rank;
    private String rival;
    private int playedMatches;
    private boolean isOnline;

    /**
     * default constructor for denne klasse.
     */
    public Player()
    {

    }

    /**
     * Herunder ses getters og setters til alle klassens attributer.
     * note: alle disse er autogenereret igennem IntelliJ
     */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getELO() {
        return ELO;
    }

    public void setELO(float ELO) {
        this.ELO = ELO;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getRival() {
        return rival;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }

    public int getPlayedMatches() {
        return playedMatches;
    }

    public void setPlayedMatches(int playedMatches) {
        this.playedMatches = playedMatches;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }


}

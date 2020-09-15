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
public class Chat
{
    /**
     * Den første attribut i denne klasse er annoteret med @Id og @GeneratedValue
     * @Id specificerer at denne attribut er en primary key i tabellen.
     * @GeneratedValue da kolonnen også er auto-incrementet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageID;
    private String message;
    private String username;
    private String timeOfMessage;

    /**
     * default constructor for denne klasse.
     */
    public Chat(){

    }

    /**
     * Herunder ses getters og setters til alle klassens attributer.
     * note: alle disse er autogenereret igennem IntelliJ
     */
    public String getTimeOfMessage() {
        return timeOfMessage;
    }

    public void setTimeOfMessage(String timeOfMessage) {
        this.timeOfMessage = timeOfMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

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
public class SystemLog
{
    /**
     * Den første attribut i denne klasse er annoteret med @Id og @GeneratedValue
     * @Id specificerer at denne attribut er en primary key i tabellen.
     * @GeneratedValue da kolonnen også er auto-incrementet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int systemlogID;
    private String systemAction;
    private String timeOfAction;

    /**
     * default constructor for denne klasse.
     */
    public SystemLog()
    {

    }

    /**
     * Herunder ses getters og setters til alle klassens attributer.
     * note: alle disse er autogenereret igennem IntelliJ
     */
    public int getSystemlogID() {
        return systemlogID;
    }

    public void setSystemlogID(int systemlogID) {
        this.systemlogID = systemlogID;
    }

    public String getSystemAction() {
        return systemAction;
    }

    public void setSystemAction(String systemAction) {
        this.systemAction = systemAction;
    }

    public String getTimeOfAction() {
        return timeOfAction;
    }

    public void setTimeOfAction(String timeOfAction) {
        this.timeOfAction = timeOfAction;
    }


}

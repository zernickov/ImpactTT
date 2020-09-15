package com.impacttabletennis.skoleprojekt.Repository;

import com.impacttabletennis.skoleprojekt.Model.Chat;
import com.impacttabletennis.skoleprojekt.Repository.RepositoryInterfaces.IChatRepo;
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
public class ChatRepo implements IChatRepo
{
    /**
     * Denne annotation står for at lave dependency injection på et objekt,
     * bruges på fields, setter metoder og konstruktorer
     */
    @Autowired
    public JdbcTemplate template;

    @Autowired
    private PlayerRepo playerRepo;

    /**
     * annoteret med @Override, da det er en metoder fra det implementeret interface.
     * @return returnerer alt (*) fra chat tabellen i form af et prepared statement.
     * instantierer RowMapper, som er et interface, der bliver brugt af jdbc template til at mappe rows af et resultatsæt.
     * BeanProbertyRowMapper er en RowMapper implementation, der laver en ny instans af match klassen for hver række i resultatsættet.
     */
    @Override
    public List<Chat> fetchAll() {
        RowMapper<Chat> rowMapper = new BeanPropertyRowMapper<>(Chat.class);
        return template.query("SELECT * FROM chat", rowMapper);
    }

    /**
     * en void metode, da den ikke returnerer noget.
     * @param chat objekt af Chat klassen.
     * @param request objekt af HttpServletRequest interfacet.
     * @throws Exception, da den benytter sig at HttpServletRequest, som kan give en Exception (heriblandt typisk en ServletException eller IOException)
     * bruger ved hjælp af .update metoden fra JdbcTemplate et prepared statement til at tilføje en ny row, med de givet værdier.
     */
    @Override
    public void addChat(Chat chat, HttpServletRequest request) throws Exception {
            template.update("INSERT INTO skoleprojekt.chat (messageID, message, username, timeOfMessage) VALUES (?, ?, ?, CURRENT_TIMESTAMP + INTERVAL 8 HOUR)",
                    chat.getMessageID(), chat.getMessage(), playerRepo.retrievePlayerNameFromURL(request)
            );
    }

}

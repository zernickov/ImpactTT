package com.impacttabletennis.skoleprojekt.Service;

import com.impacttabletennis.skoleprojekt.Service.ServiceInterfaces.IPlayerService;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;

/**
 * Klassen er annoteret med @Service, hvilket fortæller at det er her der vil blive anvendt logik.
 * Dette resulterer i at der bliver trukket logik/kode udfra både Repo og controller klasserne, for at opdele ansvarsområder.
 */
@Service
public class PlayerService implements IPlayerService
{
    /**
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param request instans af HttpServletRequest.
     * @return returnerer en string, som er et username der er trukket ud af brugerens URL.
     * @throws Exception, da HttpServletRequest kan give en Exception.
     */
    @Override
    public String retrieveUsername(HttpServletRequest request) throws Exception
    {
        String url = request.getHeader("Referer");
        URI uri = new URI(url);
        String path = uri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        return idStr;
    }


    /**
     * Taget fra https://www.geeksforgeeks.org/elo-rating-algorithm/
     * Dette er en algoritme taget fra det ovenstående link.
     * @param rating1 parameter for en spillers ELO
     * @param rating2 parameter for en spillers ELO
     * @return returnerer en float, som er chancen for at en spiller vinder på baggrund af begge spillers ELO point.
     */
    @Override
    public float Probability(float rating1, float rating2)
    {
        return 1.0f * 1.0f / (1 + 1.0f *
                (float)(Math.pow(10, 1.0f *
                        (rating1 - rating2) / 400)));
    }

    /**
     * Taget fra https://www.geeksforgeeks.org/elo-rating-algorithm/
     * Dette er en algoritme taget fra det ovenstående link, som vi har tilpasset vores system vha. af booleans vi bruger i if-statements.
     * annoteret med @Override, da det er en metode der skal implementeres fra IPlayerRepo interfacet.
     * @param Ra parameter, som er en spillers ELO
     * @param Rb parameter, som er en spillers ELO
     * @param K parameter som er en konstant der bestemmer, hvor stor en stigning eller falding der generelt vil være i kampe.
     * @param d boolean, der tjekker hvorvidt player1 eller player2 er vinderen af kampen.
     * @param check boolean, der tjekker hvorvidt at brugeren er spiller1 eller spiller2 i denne kamp.
     * @return returnerer brugerens nye ELO-rating.
     */
    @Override
    public float EloRating(float Ra, float Rb, int K, boolean d, boolean check)
    {

        // To calculate the Winning
        // Probability of Player B
        float Pb = Probability(Ra, Rb);

        // To calculate the Winning
        // Probability of Player A
        float Pa = Probability(Rb, Ra);

        // Case -1 When Player A wins
        // Updating the Elo Ratings
        if (d == true)
        {
            Ra = Ra + K * (1 - Pa);
            Rb = Rb + K * (0 - Pb);
            if(check)
            {
                return Ra;
            }
            else
            {
                return Rb;
            }
        }

        // Case -2 When Player B wins
        // Updating the Elo Ratings
        else
        {
            Ra = Ra + K * (0 - Pa);
            Rb = Rb + K * (1 - Pb);
            if(check)
            {
                return Rb;
            }
            else
            {
                return Ra;
            }
        }

    }

}

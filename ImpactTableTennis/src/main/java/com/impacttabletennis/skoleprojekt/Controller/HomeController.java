package com.impacttabletennis.skoleprojekt.Controller;

import com.impacttabletennis.skoleprojekt.Model.Chat;
import com.impacttabletennis.skoleprojekt.Model.Match;
import com.impacttabletennis.skoleprojekt.Model.Player;
import com.impacttabletennis.skoleprojekt.Model.SystemLog;
import com.impacttabletennis.skoleprojekt.Repository.ChatRepo;
import com.impacttabletennis.skoleprojekt.Repository.MatchRepo;
import com.impacttabletennis.skoleprojekt.Repository.PlayerRepo;
import com.impacttabletennis.skoleprojekt.Repository.SystemLogRepo;
import com.impacttabletennis.skoleprojekt.Service.MatchService;
import com.impacttabletennis.skoleprojekt.Service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Annotation for at indikere at denne klasse er en Spring controller, bruges i Spring MVC eller Spring WebFlux
 * Implementerer ErrorController interfacet, som bliver brugt til at kende /error pathen, så der kan laves en mapping til /error
 */
@Controller
public class HomeController implements ErrorController
{
    /**
     * Denne annotation står for at lave dependency injection på et objekt,
     * bruges på fields, setter metoder og konstruktorer
     */
    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private MatchRepo matchRepo;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private SystemLogRepo systemLogRepo;

    private SystemLog systemLog = new SystemLog();

    /**
     * getErrorPath er en metode fra ErrorController interfacet, som derfor bliver overridet.
     * @return Den returner mapping kaldet "/error" fra errorRedirect metoden.
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * errorRedirect() metoden er mappet til /error
     * @return Den returnerer "home/index", som er html filen, til vores index fil.
     */
    @RequestMapping("/error")
    public String errorRedirect() {
        return "home/index";
    }

    /**
     * findMatch metoden kalder validateMatch() metoden fra MatchService klassen.
     * @param request en instans lavet af HttpServletRequest
     * @param match et Objekt af Match klassen, som er annoteret som @Modelattribute.
     * @return returnerer "redirect: "+referer, som redirecter én til den side man i forvejen er på, vha. .getheadet metoden der bliver brugt på request objektet.
     * @throws Exception, da HttpServletRequest kan give en Exception (typisk vil denne exception være en ServletException eller IOException)
     */
    @RequestMapping(value = "/findMatch", method = RequestMethod.POST)
    public String findMatch(HttpServletRequest request, @ModelAttribute("match") Match match) throws Exception
    {
        matchService.validateMatch(match, request);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * declareWinner metoden kalder en anden metode ved navn declareWinner fra MatchRepo klassen.
     * @param request en instans lavet af HttpServletRequest
     * @return returnerer "redirect: "+referer, som redirecter én til den side man i forvejen er på, vha. .getheadet metoden der bliver brugt på request objektet.
     * @throws Exception, da HttpServletRequest kan give en Exception (typisk vil denne exception være en ServletException eller IOException)
     */
    @RequestMapping(value = "/declareWinner", method = RequestMethod.POST)
    public String declareWinner(HttpServletRequest request) throws Exception
    {

        matchRepo.declareWinner(playerService.retrieveUsername(request), request);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * declareLoser metoden kalder en anden metode ved navn declareLoser fra MatchRepo klassen.
     * @param request en instans lavet af HttpServletRequest
     * @return returnerer "redirect: "+referer, som redirecter én til den side man i forvejen er på, vha. .getheadet metoden der bliver brugt på request objektet.
     * @throws Exception, da HttpServletRequest kan give en Exception (typisk vil denne exception være en ServletException eller IOException)
     */
    @RequestMapping(value = "/declareLoser", method = RequestMethod.POST)
    public String declareLoser(HttpServletRequest request) throws Exception
    {

        matchRepo.declareLoser(playerService.retrieveUsername(request), request);


        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * Bruges her for at mappe HTTP GET requests på en specifik handler metode
     * (Kan også bruges som @RequestMapping(method = RequestMethod.GET))
     * @return returnerer "home/index", som er vores html fil: index.
     */
    @GetMapping("/")
    public String home()
    {
        return "home/index";
    }

    /**
     * @return returnerer "home/createAccountPage", som er vores html fil, hvor man kan oprette sin profil.
     */
    @GetMapping("/createAccountPage")
    public String createAccountPage()
    {
        return "home/createAccountPage";
    }

    /**
     * @return returnerer "home/helpPage" som er en side der indeholder nogle vigtige informationer for førstegangs-brugere af systemet, samt en "how to" find match.
     */
    @GetMapping("/helpPage")
    public String helpPage()
    {
        return "home/helpPage";
    }

    /**
     * @param model i paramteren instantieres et objekt af Model klassen ->
     *              Dette bruges til at tilføje attributen "player", på baggrund af fetchAll() metoden fra PlayerRepo klassen, som gør det muligt at anvende thymeleaf på dette i html filen.
     * @return returnerer "home/allPlayers, html filen.
     */
    @GetMapping("/allPlayersList")
    public String allPlayers(Model model)
    {
        model.addAttribute("player", playerRepo.fetchAll());
        return "home/allPlayers";
    }

    /**
     * @param model i paramteren instantieres et objekt af Model klassen ->
     *      *              Dette bruges til at tilføje attributen "systemLog", på baggrund af fetchAllActions() metoden fra SystemLogRepo klassen, som gør det muligt at anvende thymeleaf på dette i html filen.
     * @return returnerer "home/systemLog", html filen
     */
    @GetMapping("/systemLogPage")
    public String systemLogPage(Model model)
    {
        model.addAttribute("systemLog", systemLogRepo.fetchAllActions());
        return "home/systemLog";
    }

    /**
     * @param model i paramteren instantieres et objekt af Model klassen ->
     *      *              Dette bruges til at tilføje attributen "allMatches", på baggrund af fetchAllCompletedMatches() metoden fra MatchRepo klassen, som gør det muligt at anvende thymeleaf på dette i html filen.
     * @return returnerer "home/allMatches", html filen
     */
    @GetMapping("/allMatchesList")
    public String allMatches(Model model)
    {
        model.addAttribute("allMatches", matchRepo.fetchAllCompletedMatches());
        return "home/allmatches";
    }

    /**
     * en post mapping metode, som kalder addAction (fra SystemLogRepo klassen), samt deletePlayer() metoden fra PlayerRepo.
     * @param player Modelattribute.. objekt af Player klassen.
     * @param request en instans lavet af HttpServletRequest
     * @return redirecter en til den samme url som man er på.
     */
    @RequestMapping(value = "/deletePlayer", method = RequestMethod.POST)
    public String deletePlayer(@ModelAttribute("player") Player player, HttpServletRequest request)
    {

        systemLogRepo.addAction(systemLog, playerRepo.retrievePlayerNameFromInput(player.getUserName())+" was deleted from the system");

        playerRepo.deletePlayer(player.getUserName());

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * en post mapping metode, der kalder revengeELOcheater fra PlayerRepo og addAction fra SystemLogRepo.
     * @param player instantierer et objekt af Player klassen.
     * @param request en instans lavet af HttpServletRequest
     * @return redirecter en til den samme url som man er på.
     */
    @RequestMapping(value = "/revengePlayer", method = RequestMethod.POST)
    public String revengeCheater(@ModelAttribute("player") Player player, HttpServletRequest request)
    {
        playerRepo.revengeELOcheater(player.getUserName());

        systemLogRepo.addAction(systemLog, playerRepo.retrievePlayerNameFromInput(player.getUserName())+" has been revenged and was subtracted 200 ELO points");

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * ModelAttribute bruges til at binde en metode parameter eller retur værdi til en navngivet model attribute
     * og gøre den klar til et web view.
     * @Valid og Bindingresult bliver brugt til at tjekke annotationerne på Player klassens attributer.
     * .hasErrors() metoden fra BindingResult, tjekker hvorvidt disse er overholdt.
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("player") Player player, @Valid Player player1, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "home/createAccountPage";
        }

        playerRepo.addPlayer(player);

        return "redirect:/";
    }

    /**
     * post mapping metode, der kalder addChat metoden fra ChatRepo.
     * @param request instans af HttpServletRequest
     * @param chat objekt af Chat model klassen.
     * @return redirecter en til den samme url som man er på.
     * @throws Exception, da HttpServletRequest kan give en Exception (typisk vil denne exception være en ServletException eller IOException)
     */
    @RequestMapping(value = "/saveMessage", method = RequestMethod.POST)
    public String addMessage(HttpServletRequest request, @ModelAttribute("chat") Chat chat) throws Exception
    {
        chatRepo.addChat(chat, request);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * successLogin metoden returnerer et ModelAndView objekt.
     * PathVariable bruges til at annotere request handler metode argumenter.
     * Den kan bruges til at håndtere dynamiske ændringer i URI'en, der hvor nogle URI værdier fungerer som
     * parametre.
     * Der kan bruges RegularExpressions til at specificere denne parameter.
     */
    @GetMapping(value = "/login/{userName}")
    public ModelAndView successLogin(@PathVariable String userName, Model model)
    {
        ModelAndView mav = new ModelAndView("home/playerhome");

        Player player = playerRepo.findPlayerById(userName);
        mav.addObject("player", player);

        model.addAttribute("chat", chatRepo.fetchAll());
        model.addAttribute("allCompletedMatches", matchRepo.fetchRecentCompletedMatches());
        model.addAttribute("upcomingMatches", matchRepo.fetchAllUpcoming());
        model.addAttribute("top10Players", playerRepo.fetchTop10());
        matchService.showCurrentMatch(model);


        return mav;
    }

    /**
     * Login metoden kalder en anden metode ved navn login fra PlayerRepo klassen.
     * @param player objekt af Player klassen, der bliver brugt til at kalde 2 getter metoder.
     * @return returnerer getmapping metode "/login/{username}", hvis login metoden fra PlayerRepo ikke kaster en Exception.
     * Hvis den kaster en Exception, bliver man redirectet til index siden.
     * Dette er netop grunden til at der bliver brugt en try catch, da login metoden fra PlayerRepo kan kaste en Exception.
      */
    @RequestMapping(value= "/login", method = RequestMethod.POST)
    public String Login(@ModelAttribute("player") Player player)
    {
        try
        {
            playerRepo.login(player.getUserName(), player.getUserCode());
            return "redirect:/login/"+player.getUserName();

        } catch(Exception e)
        {
            return "redirect:/";
        }
    }
}

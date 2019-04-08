package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.List;

import static uk.ac.bris.cs.scotlandyard.model.Colour.*;

public class ScotMask{

    private Player mrX;
    private List<Player> detectives;
    private Graph<Integer, Transport> graph;
    private List<Colour> players;

    public ScotMask (ScotlandYardView view){
        players = view.getPlayers();
        Cost xTickets = new Cost(view.getPlayerTickets(BLACK, Ticket.TAXI).get(),
                                 view.getPlayerTickets(BLACK, Ticket.BUS).get(),
                                 view.getPlayerTickets(BLACK, Ticket.UNDERGROUND).get(),
                                 view.getPlayerTickets(BLACK, Ticket.SECRET).get(),
                                 view.getPlayerTickets(BLACK, Ticket.DOUBLE).get());
        mrX = new Player(view.getPlayerLocation(BLACK).get(), xTickets, BLACK);
    }
}
package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static uk.ac.bris.cs.scotlandyard.model.Colour.*;

public class ScotMask{

    private Player mrX;
    private List<Player> detectives = new ArrayList<>();
    private Graph<Integer, Transport> graph;
    private List<Colour> players;

    public ScotMask (ScotlandYardView view){
        players = view.getPlayers();
        for(Colour player : players){
            if(player == BLACK)
                mrX = new Player(view.getPlayerLocation(BLACK).get(), makeCost(BLACK, view), BLACK);
            else
                detectives.add(new Player(view.getPlayerLocation(player).get(), makeCost(player, view), player));
        }
        graph = view.getGraph();

    }

    public void makeMove(Move move, Colour player){
        if(player == mrX.getColour()){
            if(move instanceof TicketMove){
                TicketMove tm = (TicketMove) move;
                mrX.setLocation(tm.destination());
            }
        }
    }

    private Cost makeCost(Colour colour, ScotlandYardView view){

        return new Cost(view.getPlayerTickets(colour, Ticket.TAXI).get(),
                        view.getPlayerTickets(colour, Ticket.BUS).get(),
                        view.getPlayerTickets(colour, Ticket.UNDERGROUND).get(),
                        view.getPlayerTickets(colour, Ticket.SECRET).get(),
                        view.getPlayerTickets(colour, Ticket.DOUBLE).get());
    }

    public List<Player> getDetectives(){
        return detectives;
    }

    public Player getMrX(){
        return mrX;
    }

    public Graph<Integer, Transport> getGraph(){
        return graph;
    }
}
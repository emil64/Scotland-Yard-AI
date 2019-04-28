package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static uk.ac.bris.cs.scotlandyard.model.Colour.*;

public class ScotMask{

    private Player mrX;
    private List<Player> detectives = new ArrayList<>();
    private Graph<Integer, Transport> graph;
    private List<Colour> players;
    private int roundsSince, roundsTo;
    private Colour currentPlayer;

    public ScotMask (ScotlandYardView view){
        players = view.getPlayers();
        for(Colour player : players){
            if(player == BLACK)
                mrX = new Player(view.getPlayerLocation(BLACK).get(), makeCost(BLACK, view), BLACK);
            else
                detectives.add(new Player(view.getPlayerLocation(player).get(), makeCost(player, view), player));
        }
        graph = view.getGraph();
        roundsSince = calculateRoundsSince(view.getRounds(), view.getCurrentRound());
        roundsTo = calculateRoundsTo(view.getRounds(), view.getCurrentRound());
        currentPlayer = view.getCurrentPlayer();

    }

    private int calculateRoundsSince(List<Boolean> rounds, int currentRound){

        int res = 0, r = currentRound;
        while(r > 0 && !rounds.get(r))
            r--;
        res = currentRound - r;
        return res;
    }

    private int calculateRoundsTo(List<Boolean> rounds, int currentRound){

        int res = 0, r = currentRound;
        while(r > 0 && !rounds.get(r))
            r++;
        res = r - currentRound;
        return res;
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

    public int getRoundsSince(){
        return roundsSince;
    }
    public int getRoundsTo(){
        return roundsTo;
    }
}
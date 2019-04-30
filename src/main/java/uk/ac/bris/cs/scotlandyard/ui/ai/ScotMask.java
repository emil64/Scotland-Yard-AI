package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.*;

import static uk.ac.bris.cs.scotlandyard.model.Colour.*;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.DOUBLE;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.SECRET;

public class ScotMask{

    private Player mrX;
    private List<Player> detectives = new ArrayList<>();
    private Graph<Integer, Transport> graph;
    private List<Colour> playersList;
    private Map<Colour, Player> players = new HashMap<Colour, Player>();
    private int roundsSince, roundsTo, currentRound;
    private Colour currentPlayer;
    private Set<Move> validMoves = new HashSet<>();
    private List<Boolean> rounds;

    public ScotMask (ScotlandYardView view){
        playersList = view.getPlayers();
        for(Colour player : playersList){
            if(player == BLACK)
                mrX = new Player(view.getPlayerLocation(BLACK).get(), makeCost(BLACK, view), BLACK);
            else
                detectives.add(new Player(view.getPlayerLocation(player).get(), makeCost(player, view), player));
            players.put(player, new Player(view.getPlayerLocation(player).get(), makeCost(player, view), player));
        }
        graph = view.getGraph();
        roundsSince = calculateRoundsSince(view.getRounds(), view.getCurrentRound());
        roundsTo = calculateRoundsTo(view.getRounds(), view.getCurrentRound());
        rounds = view.getRounds();
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

    public Player getDetective(Colour colour){
        return players.get(colour);
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
    public int getCurrentRound() {
        return currentRound;
    }

    private Set<Move> validMove(Colour player) {
        Player p;
        p = players.get(player);
        validMoves.clear();
        Collection<Edge<Integer, Transport>> edges = getGraph().getEdgesFrom(new Node<>(p.getLocation()));
        for (Edge<Integer, Transport> edge : edges){
            Transport t = edge.data();
            int destination = edge.destination().value();
            boolean mere = true;

            for (Player d : detectives){
                if(d.getLocation() == destination)
                    mere = false;
            }

            if(mere && p.hasTickets(SECRET))
                validMoves.add(new TicketMove(p.getColour(), SECRET, destination));

            if(!p.hasTickets(Ticket.fromTransport(t)))
                mere = false;

            if(mere)
                validMoves.add(new TicketMove(p.getColour(), Ticket.fromTransport(t), destination));
        }
        //System.out.println(validMoves.toString());
        if(validMoves.isEmpty() && player != mrX.getColour())
            validMoves.add(new PassMove(player));
        if(player == mrX.getColour()){
            validMoves.addAll(possibleDoubleMoves(validMoves));
        }
        return Collections.unmodifiableSet(validMoves);
    }

    private Set<Move> possibleDoubleMoves(Set<Move> moves){

        //System.out.println(rounds.size());
        if(!mrX.hasTickets(DOUBLE) || getCurrentRound() > rounds.size()-2)
            return Collections.emptySet();
        Set<Move> db = new HashSet<>();
        for(Move move : moves)
            if (move instanceof TicketMove) {
                TicketMove m = (TicketMove) move;
                Collection<Edge<Integer, Transport>> edges = getGraph().getEdgesFrom(new Node<>(m.destination()));
                for (Edge<Integer, Transport> edge : edges) {
                    Transport t = edge.data();
                    boolean mere = true;
                    int destination = edge.destination().value();

                    for (Player d : detectives) {
                        if (d.getLocation() == destination)
                            mere = false;
                    }
                    if (mere) {
                        if(mrX.hasTickets(SECRET) && m.ticket()!=SECRET)
                            db.add(new DoubleMove(mrX.getColour(), m, new TicketMove(mrX.getColour(), SECRET, destination)));
                        if(m.ticket() == SECRET && mrX.hasNumberTickets(SECRET, 2))
                            db.add(new DoubleMove(mrX.getColour(), m, new TicketMove(mrX.getColour(), SECRET, destination)));

                        if (m.ticket() == Ticket.fromTransport(t)) {
                            if (!mrX.hasNumberTickets(Ticket.fromTransport(t), 2))
                                mere = false;
                        } else if (!mrX.hasTickets(Ticket.fromTransport(t)))
                            mere = false;

                    }
                    if (mere)
                        db.add(new DoubleMove(mrX.getColour(), m, new TicketMove(mrX.getColour(), Ticket.fromTransport(t), destination)));
                }
            }
        return db;
    }
}
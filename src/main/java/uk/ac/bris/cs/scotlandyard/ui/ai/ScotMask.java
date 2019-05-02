package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;

import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.*;

import static uk.ac.bris.cs.scotlandyard.model.Colour.*;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.DOUBLE;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.SECRET;

public class ScotMask implements ScotlandYardView{

    private Player mrX;
    private List<Player> detectives = new ArrayList<>();
    private Graph<Integer, Transport> graph;
    private List<Colour> playersList;
    private Map<Colour, Player> players = new HashMap<>();
    private int roundsSince, roundsTo, currentRound;
    private Colour currentPlayer;
    private Set<Move> validMoves = new HashSet<>();
    private List<Boolean> rounds;
    private boolean gameOver;
    private Set<Colour> winningPlayer;

     ScotMask (ScotlandYardView view){
        playersList = view.getPlayers();
        for(Colour player : playersList){
            Player p = mrX;
            if(player == BLACK) {
                if(view.getPlayerLocation(BLACK).isPresent())
                    mrX = new Player(view.getPlayerLocation(BLACK).get(), makeCost(BLACK, view), BLACK);
                p = mrX;
            }
            else {
                if(view.getPlayerLocation(player).isPresent())
                    p = new Player(view.getPlayerLocation(player).get(), makeCost(player, view), player);
                detectives.add(p);
            }
            players.put(player, p);

        }
        graph = view.getGraph();
        roundsSince = calculateRoundsSince(view.getRounds(), view.getCurrentRound());
        roundsTo = calculateRoundsTo(view.getRounds(), view.getCurrentRound());
        rounds = view.getRounds();
        currentPlayer = view.getCurrentPlayer();
        gameOver = false;
        currentRound = view.getCurrentRound();
        winningPlayer = new HashSet<>();

    }

    void setXLocation(int location){
        mrX.setLocation(location);
        players.get(BLACK).setLocation(location);
        //System.out.println("Se fute in Scotmask " + players.get(BLACK).getLocation());
    }

    private int calculateRoundsSince(List<Boolean> rounds, int currentRound){

        int res, r = currentRound;
        while(r > 0 && !rounds.get(r))
            r--;
        res = currentRound - r;
        return res;
    }

    private int calculateRoundsTo(List<Boolean> rounds, int currentRound){

        int res, r = currentRound;
        while(r > 0 && !rounds.get(r))
            r++;
        res = r - currentRound;
        return res;
    }


    void makeMove(Move move, Colour player){
        if(player == mrX.getColour()){
            if(move instanceof TicketMove){
                TicketMove tm = (TicketMove) move;
                mrX.setLocation(tm.destination());
                players.get(BLACK).setLocation(tm.destination());
                mrX.getTickets().removeTicket(tm.ticket());
            }
            if(move instanceof DoubleMove){
                DoubleMove dm = (DoubleMove) move;
                mrX.setLocation(dm.secondMove().destination());
                players.get(BLACK).setLocation(dm.secondMove().destination());
                mrX.getTickets().removeTicket(DOUBLE);
                mrX.getTickets().removeTicket(dm.firstMove().ticket());
                mrX.getTickets().removeTicket(dm.secondMove().ticket());
            }
        }
        else{
            if(move instanceof TicketMove){
                TicketMove tm = (TicketMove) move;
                players.get(player).setLocation(tm.destination());
                players.get(player).getTickets().removeTicket(tm.ticket());
                players.get(BLACK).getTickets().addTicket(tm.ticket());
                mrX = players.get(BLACK);
            }
        }
    }

    private Cost makeCost(Colour colour, ScotlandYardView view){

        if(view.getPlayerTickets(colour, Ticket.TAXI).isPresent() && view.getPlayerTickets(colour, Ticket.BUS).isPresent() && view.getPlayerTickets(colour, Ticket.UNDERGROUND).isPresent() && view.getPlayerTickets(colour, Ticket.SECRET).isPresent() && view.getPlayerTickets(colour, Ticket.DOUBLE).isPresent())
        return new Cost(view.getPlayerTickets(colour, Ticket.TAXI).get(),
                        view.getPlayerTickets(colour, Ticket.BUS).get(),
                        view.getPlayerTickets(colour, Ticket.UNDERGROUND).get(),
                        view.getPlayerTickets(colour, Ticket.SECRET).get(),
                        view.getPlayerTickets(colour, Ticket.DOUBLE).get());
        return new Cost();
    }

    void setCurrentPlayer(boolean MrXTrns){
         if(MrXTrns)
             currentPlayer = BLACK;
         else
             currentPlayer = detectives.get(1).getColour();
    }

    List<Player> getDetectives(){
        return detectives;
    }

    Player getMrX(){
        return mrX;
    }

    Player getDetective(Colour colour){
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

    @Override
    public List<Boolean> getRounds() {
        return rounds;
    }

    private Set<Move> validMove(Colour player) {
        Player p;
        p = players.get(player);
        validMoves.clear();
        //System.out.println(p.getLocation() + " Aici nu se fute nimic");
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

    Set<Move> getValidMoves(Colour player){
        return validMove(player);
    }

    @Override
    public List<Colour> getPlayers() {
        return playersList;
    }

    @Override
    public Set<Colour> getWinningPlayers() {
        return winningPlayer;
    }

    @Override
    public Optional<Integer> getPlayerLocation(Colour colour) {
        return Optional.of(players.get(colour).getLocation());
    }

    @Override
    public Optional<Integer> getPlayerTickets(Colour colour, Ticket ticket) {
        return Optional.of(players.get(colour).getTickets().getTicket(ticket));
    }

    private boolean checkGameOver(){
        gameOver = noMoreRounds() || mrXStuck() || stuckDetectives() || mrXCaptured();
        return gameOver;
    }

    private boolean noMoreRounds(){
        if(getCurrentRound() >= rounds.size()){
            winningPlayer.add(mrX.getColour());
            return true;
        }
        return false;
    }

    private boolean stuckDetectives(){
        boolean stuck = true;
        for(Player detective : detectives){
            if(!validMove(detective.getColour()).contains(new PassMove(detective.getColour())))
                stuck = false;
        }
        if(stuck){
            winningPlayer.add(mrX.getColour());
            return true;
        }
        return false;
    }

    private boolean mrXCaptured(){
        for (Player detective : detectives)
            if(detective.getLocation() == mrX.getLocation()){
                winningPlayer.addAll(playersList.subList(1, playersList.size()));
                return true;
            }
        return false;
    }

    private boolean mrXStuck(){
        if(getCurrentPlayer() == mrX.getColour())
            if(validMove(mrX.getColour()).isEmpty()){
                winningPlayer.addAll(playersList.subList(1, playersList.size()));
                gameOver = true;
                return true;
            }
        return false;
    }

    public boolean isGameOver(){

        return checkGameOver();
    }

    @Override
    public Colour getCurrentPlayer() {
        return currentPlayer;
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
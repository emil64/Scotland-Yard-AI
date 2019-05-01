package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import static uk.ac.bris.cs.scotlandyard.model.Transport.*;

public class Cost {

    private int taxi, bus, underground, secret, x2, ferry;
    private int cost, moves;

    public Cost(){
        taxi = 0;
        bus = 0;
        underground = 0;
        secret = 0;
        x2 = 0;
        ferry = 0;
        cost = 999999999;
        moves = 0;
    }

    public Cost(Cost c){
        taxi = c.taxi;
        bus = c.bus;
        underground = c.underground;
        x2 = c.x2;
        secret = c.secret;
        cost = c.cost;
        ferry = c.ferry;
        moves = c.moves;
    }

    public Cost(int taxi, int bus, int underground, int secret, int x2){
        this.taxi = taxi;
        this.bus = bus;
        this.underground = underground;
        this.secret = secret;
        this.x2 = x2;
        moves = 0;
        ferry = 0;
        cost = 0;
    }

    public void addTransport(Transport transport){
        if(transport == BUS)
            addBus();
        if(transport == TAXI)
            addTaxi();
        if(transport == UNDERGROUND)
            addUnderground();
        if(transport == FERRY)
            addFerry();
    }

    private void addBus(){
        bus++;
        moves++;
    }
    private void addTaxi(){
        taxi++;
        moves++;
    }
    private void addUnderground(){
        underground++;
        moves++;
    }

    private void addFerry(){
        ferry++;
        moves++;
    }
    private void addSecret(){
        secret++;
        moves++;
    }
    private void addX2(){
        x2++;
    }

    public int getCost(){
        return moves*1000 + taxi*2 + bus * 3 + underground * 6;
    }

    public int getMoves(){
        return moves;
    }

    public Boolean hasTaxi(int n){
        return (taxi >= n);
    }
    public Boolean hasBus(int n){
        return (bus >= n);
    }
    public Boolean hasUnderground(int n){
        return (underground >= n);
    }

    public int getTaxi(){
        return taxi;
    }
    public int getBus(){
        return bus;
    }
    public int getUnderground(){
        return underground;
    }
    public int getSecret() { return secret; }
    public int getX2() { return x2; }

    public int getTicket(Ticket ticket){
        if(ticket == Ticket.TAXI) return getTaxi();
        if(ticket == Ticket.BUS) return getBus();
        if(ticket == Ticket.UNDERGROUND) return getUnderground();
        if(ticket == Ticket.SECRET) return getSecret();
        if(ticket == Ticket.DOUBLE) return getX2();
        return 0;
    }

}

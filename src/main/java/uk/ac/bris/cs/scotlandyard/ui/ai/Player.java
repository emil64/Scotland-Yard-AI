package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Ticket;

public class Player {
    private int location;
    private Cost tickets;
    private Colour colour;

    public Player(int location, Cost tickets, Colour colour){
        this.location = location;
        this.colour = colour;
        this.tickets = tickets;
    }

    public int getLocation(){
        return location;
    }
    public Colour getColour(){
        return colour;
    }
    public Cost getTickets(){
        return tickets;
    }
    public void setLocation(int location){
        this.location = location;
    }
    public void setTickets(Cost tickets){
        this.tickets = tickets;
    }

    public boolean hasTickets(Ticket ticket) {
        return tickets.hasTaxi(1) && tickets.hasBus(1) && tickets.hasUnderground(1);
    }

    public boolean hasNumberTickets(Ticket ticket, int quantityInclusive) {
        return tickets.hasTaxi(quantityInclusive) && tickets.hasBus(quantityInclusive) && tickets.hasUnderground(quantityInclusive);
    }
}

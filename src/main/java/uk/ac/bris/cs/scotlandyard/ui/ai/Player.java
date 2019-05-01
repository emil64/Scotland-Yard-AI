package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Ticket;

import static uk.ac.bris.cs.scotlandyard.model.Ticket.*;

class Player {
    private int location;
    private Cost tickets;
    private Colour colour;

    Player(int location, Cost tickets, Colour colour){
        this.location = location;
        this.colour = colour;
        this.tickets = tickets;
    }

    int getLocation(){
        return location;
    }
    Colour getColour(){
        return colour;
    }
    Cost getTickets(){
        return tickets;
    }
    void setLocation(int location){
        this.location = location;
    }
    /*public void setTickets(Cost tickets){
        this.tickets = tickets;
    }*/

    boolean hasTickets(Ticket ticket) {
       return hasNumberTickets(ticket, 1);
    }

    boolean hasNumberTickets(Ticket ticket, int quantityInclusive) {
        if(ticket == TAXI)
            return tickets.hasTaxi(quantityInclusive);
        if(ticket == BUS)
            return tickets.hasBus(quantityInclusive);
        if(ticket == UNDERGROUND)
            return tickets.hasUnderground(quantityInclusive);
        if(ticket == SECRET)
            return tickets.hasSecret(quantityInclusive);
        if(ticket == DOUBLE)
            return tickets.hasDouble(quantityInclusive);
        return false;
    }
}

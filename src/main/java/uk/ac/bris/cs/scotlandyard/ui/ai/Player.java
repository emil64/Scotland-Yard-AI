package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;

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
        return getTickets();
    }
}

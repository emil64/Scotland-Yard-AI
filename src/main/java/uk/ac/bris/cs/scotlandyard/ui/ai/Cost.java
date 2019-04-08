package uk.ac.bris.cs.scotlandyard.ui.ai;

public class Cost {

    private int taxi, bus, underground, secret, x2;
    private int cost, moves;

    public Cost(){
        taxi = 0;
        bus = 0;
        underground = 0;
        secret = 0;
        x2 = 0;
        cost = 0;
        moves = 0;
    }

    public Cost(Cost c){
        taxi = c.taxi;
        bus = c.bus;
        underground = c.underground;
        x2 = c.x2;
        secret = c.secret;
        cost = c.cost;
        moves = c.moves;
    }

    public void addBus(){
        bus++;
        moves++;
    }
    public void addTaxi(){
        taxi++;
        moves++;
    }
    public void addUnderground(){
        underground++;
        moves++;
    }
    public void addSecret(){
        secret++;
        moves++;
    }
    public void addX2(){
        x2++;
    }


}

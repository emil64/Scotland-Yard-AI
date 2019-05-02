package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;

public class Score implements ScoreCalculator {

    @Override
    public int getMrXscore(ScotMask mask) {

        int score = 0, min = 999999999, cost;
        for(Player detective: mask.getDetectives()){
            Distances d = new Distances(mask.getMrX().getLocation(), detective.getLocation(), mask.getGraph(), detective.getTickets());
            cost = d.getCost().getScore();
            if(cost < min)
                min = cost;
            score += cost;
        }
        score = ((score-min)/(mask.getDetectives().size()-1)*2 + min*3)/5;
        return score;
    }

    @Override
    public int getDetectiveScore(ScotMask mask, int Xloc, Colour player) {
        return new Distances(mask.getDetective(player).getLocation(), Xloc, mask.getGraph(), mask.getDetective(player).getTickets()).getCost().getScore();
    }
}

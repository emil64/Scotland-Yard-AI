package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

public interface ScoreCalculator {

    public int getMrXscore(ScotMask mask);

    public int getDetectiveScore(ScotMask mask);

    public ScotMask PlayerMoves(ScotMask mask, Colour colour, Move move);

}

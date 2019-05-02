package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;

public interface ScoreCalculator {

    int getMrXscore(ScotMask mask);

    int getDetectiveScore(ScotMask mask, int Xloc, Colour player);
}

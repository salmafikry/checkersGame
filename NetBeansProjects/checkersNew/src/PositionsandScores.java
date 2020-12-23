
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salmafikry
 */
public class PositionsandScores {
    //class maintains stack of minimax data
    int score;
    List<Point> moveCoords;

    public PositionsandScores(int score, List<Point> moveCoords) {
        this.score = score;
        this.moveCoords = moveCoords;
    }

}

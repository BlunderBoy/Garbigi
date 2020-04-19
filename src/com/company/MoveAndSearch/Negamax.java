package com.company.MoveAndSearch;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Negamax {
    // TODO
    int eval() {
        return 0;
    }
    // alt TODO
    boolean gameOver() {
        return false;
    }
    
    int negamax(int depth) {
        if (gameOver() || depth == 0) {
            return eval();
        }

        int max = Integer.MAX_VALUE;

        MoveGenerator plm = new MoveGenerator();
        ArrayList<Move> moves = plm.mutariGenerate;

        for (Move move : moves) {
            // apply move??

            int score = -negamax(depth - 1);

            if (score > max) {
                max = score;
            }
            // undo move lmao
        }

        return max;
    }
}

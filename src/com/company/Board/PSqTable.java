package com.company.Board;

public class PSqTable {
    public static final int[][] pawnValues = {
            {0,  0,  0,  0,  0,  0,  0,  0, // midgame
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
             5,  5, 10, 25, 25, 10,  5,  5,
             0,  0,  0, 20, 20,  0,  0,  0,
             5, -5,-10,  0,  0,-10, -5,  5,
             5, 10, 10,-20,-20, 10, 10,  5,
             0,  0,  0,  0,  0,  0,  0,  0},

            {0,  0,  0,  0,  0,  0,  0,  0, // endgame
            80, 80, 80, 80, 80, 80, 80, 80,
            60, 60, 60, 60, 60, 60, 60, 60,
            40, 40, 40, 40, 40, 40, 40, 40,
            20, 20, 20, 20, 20, 20, 20, 20,
             0,  0,  0,  0,  0,  0,  0,  0,
           -20,-20,-20,-20,-20,-20,-20,-20,
             0,  0,  0,  0,  0,  0,  0,  0}
    }; //shallow blue

    /*public static final int[][] pawnValues = {
            { 0,  0,  0,  0,  0,  0,  0,  0, // midgame
             -5,  7, 19, 16, 19, 10,  3,  3, // midgame
            -22,  5, 22, 32, 15, 11,-15, -9,
            -12,  4, 17, 40, 20,  6,-23, -8,
              5,-13, -2, 11,  1,-13,  0, 13,
            -18,-15, -5, -8, 22, -7,-12, -5,
             -8, 10,-16,  5,-13, -3,  7, -7,
              0,  0,  0,  0,  0,  0,  0,  0},

            {0,  0,  0,  0,  0,  0,  0,  0, // endgame
            80, 80, 80, 80, 80, 80, 80, 80,
            60, 60, 60, 60, 60, 60, 60, 60,
            40, 40, 40, 40, 40, 40, 40, 40,
            20, 20, 20, 20, 20, 20, 20, 20,
             0,  0,  0,  0,  0,  0,  0,  0,
           -20,-20,-20,-20,-20,-20,-20,-20,
             0,  0,  0,  0,  0,  0,  0,  0}
    };*/ // stockfish

    public static final int[][] knightValues = {
            {-50,-40,-30,-30,-30,-30,-40,-50,
             -40,-20,  0,  0,  0,  0,-20,-40,
             -30,  0, 10, 15, 15, 10,  0,-30,
             -30,  5, 15, 20, 20, 15,  5,-30,
             -30,  0, 15, 20, 20, 15,  0,-30,
             -30,  5, 10, 15, 15, 10,  5,-30,
             -40,-20,  0,  5,  5,  0,-20,-40,
             -50,-40,-30,-30,-30,-30,-40,-50},

            {-50,-40,-30,-30,-30,-30,-40,-50,
             -40,-20,  0,  0,  0,  0,-20,-40,
             -30,  0, 10, 15, 15, 10,  0,-30,
             -30,  5, 15, 20, 20, 15,  5,-30,
             -30,  0, 15, 20, 20, 15,  0,-30,
             -30,  5, 10, 15, 15, 10,  5,-30,
             -40,-20,  0,  5,  5,  0,-20,-40,
             -50,-40,-30,-30,-30,-30,-40,-50}
    };

    public static final int[][] bishopValues = {
            {-20,-10,-10,-10,-10,-10,-10,-20,
             -10,  0,  0,  0,  0,  0,  0,-10,
             -10,  0,  5, 10, 10,  5,  0,-10,
             -10,  5,  5, 10, 10,  5,  5,-10,
             -10,  0, 10, 10, 10, 10,  0,-10,
             -10, 10, 10, 10, 10, 10, 10,-10,
             -10,  5,  0,  0,  0,  0,  5,-10,
             -20,-10,-10,-10,-10,-10,-10,-20},

            {-20,-10,-10,-10,-10,-10,-10,-20,
             -10,  0,  0,  0,  0,  0,  0,-10,
             -10,  0,  5, 10, 10,  5,  0,-10,
             -10,  5,  5, 10, 10,  5,  5,-10,
             -10,  0, 10, 10, 10, 10,  0,-10,
             -10, 10, 10, 10, 10, 10, 10,-10,
             -10,  5,  0,  0,  0,  0,  5,-10,
             -20,-10,-10,-10,-10,-10,-10,-20}
    };

    public static final int[][] rookValues = {
            { 0,  0,  0,  0,  0,  0,  0,  0,
              5,  0,  0,  0,  0,  0,  0,  5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
              0,  0,  0,  5,  5,  0,  0,  0},

            { 0,  0,  0,  0,  0,  0,  0,  0,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
             -5,  0,  0,  0,  0,  0,  0, -5,
              0,  0,  0,  0,  0,  0,  0,  0
            }
    };

    public static final int[][] queenValues = {
            {-20,-10,-10, -5, -5,-10,-10,-20,
             -10,  0,  0,  0,  0,  0,  0,-10,
             -10,  0,  5,  5,  5,  5,  0,-10,
              -5,  0,  5,  5,  5,  5,  0, -5,
               0,  0,  5,  5,  5,  5,  0, -5,
             -10,  5,  5,  5,  5,  5,  0,-10,
             -10,  0,  5,  0,  0,  0,  0,-10,
             -20,-10,-10, -5, -5,-10,-10,-20},

            {-20,-10,-10, -5, -5,-10,-10,-20,
             -10,  0,  0,  0,  0,  0,  0,-10,
             -10,  0,  5,  5,  5,  5,  0,-10,
              -5,  0,  5,  5,  5,  5,  0, -5,
               0,  0,  5,  5,  5,  5,  0, -5,
             -10,  5,  5,  5,  5,  5,  0,-10,
             -10,  0,  5,  0,  0,  0,  0,-10,
             -20,-10,-10, -5, -5,-10,-10,-20}
    };

    public static final int[][] kingValues = {
            {-30,-40,-40,-50,-50,-40,-40,-30,
             -30,-40,-40,-50,-50,-40,-40,-30,
             -30,-40,-40,-50,-50,-40,-40,-30,
             -30,-40,-40,-50,-50,-40,-40,-30,
             -20,-30,-30,-40,-40,-30,-30,-20,
             -10,-20,-20,-20,-20,-20,-20,-10,
              20, 20,  0,  0,  0,  0, 20, 20,
              30, 30, 20, 20, 20, 30, 30, 30},

            {-50,-40,-30,-20,-20,-30,-40,-50,
             -30,-20,-10,  0,  0,-10,-20,-30,
             -30,-10, 20, 30, 30, 20,-10,-30,
             -30,-10, 30, 40, 40, 30,-10,-30,
             -30,-10, 30, 40, 40, 30,-10,-30,
             -30,-10, 20, 30, 30, 20,-10,-30,
             -30,-30,  0,  0,  0,  0,-30,-30,
             -50,-30,-30,-30,-30,-30,-30,-50}
    };

}

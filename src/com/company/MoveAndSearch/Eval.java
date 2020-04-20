package com.company.MoveAndSearch;

import com.company.Board.Bitboard;
import com.company.Board.BoardState;
import com.company.Board.PSqTable;

import static com.company.Board.Bitboard.clearBit;


public class Eval {
    public static int eval(BoardState board, int gamePhase) throws CloneNotSupportedException {
        int score = getScoreWhite(board.whitePawns.reprezentare, board.whitePawns.valoare, PSqTable.pawnValues[gamePhase]);
        //System.out.println("score for white pawns: " + score);
        int temp = getScoreWhite(board.whiteKnights.reprezentare, board.whiteKnights.valoare, PSqTable.knightValues[gamePhase]);
        //System.out.println("score for white knights: " + temp);
        score += temp;
        temp = getScoreWhite(board.whiteBishops.reprezentare, board.whiteBishops.valoare, PSqTable.bishopValues[gamePhase]);
        //System.out.println("score for white bishops: " + temp);
        score += temp;
        temp = getScoreWhite(board.whiteRooks.reprezentare, board.whiteRooks.valoare, PSqTable.rookValues[gamePhase]);
        //System.out.println("score for white rooks: " + temp);
        score += temp;
        temp = getScoreWhite(board.whiteQueens.reprezentare, board.whiteQueens.valoare, PSqTable.queenValues[gamePhase]);
        //System.out.println("score for white queens: " + temp);
        score += temp;
        temp = getScoreWhite(board.whiteKing.reprezentare, board.whiteKing.valoare, PSqTable.kingValues[gamePhase]);
        //System.out.println("score for white king: " + temp);
        score += temp;

        temp = getScoreBlack(board.blackPawns.reprezentare, board.blackPawns.valoare, PSqTable.pawnValues[gamePhase]);
        //System.out.println("score for black pawns: " + temp);
        score -= temp;
        temp = getScoreBlack(board.blackKnights.reprezentare, board.blackKnights.valoare, PSqTable.knightValues[gamePhase]);
        //System.out.println("score for black knights: " + temp);
        score -= temp;
        temp = getScoreBlack(board.blackBishops.reprezentare, board.blackBishops.valoare, PSqTable.bishopValues[gamePhase]);
        //System.out.println("score for black bishops: " + temp);
        score -= temp;
        temp = getScoreBlack(board.blackRooks.reprezentare, board.blackRooks.valoare, PSqTable.rookValues[gamePhase]);
        //System.out.println("score for black rooks: " + temp);
        score -= temp;
        temp = getScoreBlack(board.blackQueens.reprezentare, board.blackQueens.valoare, PSqTable.queenValues[gamePhase]);
        //System.out.println("score for black queens: " + temp);
        score -= temp;
        temp = getScoreBlack(board.blackKing.reprezentare, board.blackKing.valoare, PSqTable.kingValues[gamePhase]);
        //System.out.println("score for black king: " + temp);
        score -= temp;

        return score;
    }

    private static int getScoreWhite (long bitboard, int valoare, int[] value) throws CloneNotSupportedException {
        int score = 0;

        while (bitboard != 0) {
            int index = Bitboard.popLSB(bitboard);
            bitboard = clearBit(index, bitboard);
            score += valoare + value[63-index];
        }
        return score;
    }

    private static int getScoreBlack (long bitboard, int valoare, int[] value) throws CloneNotSupportedException {
        int score = 0;

        while (bitboard != 0) {
            int index = Bitboard.popLSB(bitboard);
            bitboard = clearBit(index, bitboard);
            score += valoare + value[index];
        }
        return score;
    }
}

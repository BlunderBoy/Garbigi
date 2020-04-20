package com.company.MoveAndSearch;

import com.company.Board.Bitboard;
import com.company.Board.BoardState;
import com.company.Board.PSqTable;

import static com.company.Board.Bitboard.clearBit;
import static com.company.Board.Bitboard.popLSB;


public class Eval {
    public static double eval(BoardState board, int gamePhase) throws CloneNotSupportedException {
        double score = 0;
        score += getTableScore(board, gamePhase);
        score += numarPioni(board);
        score += passedPawns(board);
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

    private static int getBlackTableScore(long bitboard, int valoare, int[] value) throws CloneNotSupportedException {
        int score = 0;

        while (bitboard != 0) {
            int index = Bitboard.popLSB(bitboard);
            bitboard = clearBit(index, bitboard);
            score += valoare + value[index];
        }
        return score;
    }
    
    private static double getTableScore(BoardState board, int gamePhase) throws CloneNotSupportedException
    {
    	double score = 0;
    	
    	for (int i = 0; i < 6; i++)
	    {
		    score += getScoreWhite(board.whiteBitboards[i].reprezentare,
				    board.whiteBitboards[i].valoare,
				    PSqTable.pieceSquareTables[i][gamePhase]);
	    }
	    
       for (int i = 0; i < 6; i++)
	    {
		    score -= getBlackTableScore(board.blackBitboards[i].reprezentare,
				    board.blackBitboards[i].valoare,
				    PSqTable.pieceSquareTables[i][gamePhase]);
	    }

        return (score/100);
    }
    
    private static double numarPioni(BoardState board)
    {
    	double score = 0;
    	if(Long.bitCount(board.whitePawns.reprezentare) >
	    Long.bitCount((board.blackPawns.reprezentare)))
	    {
	    	score += 50;
	    }
    	if(Long.bitCount(board.whitePawns.reprezentare) <
	    Long.bitCount((board.blackPawns.reprezentare)))
	    {
	    	score -= 50;
	    }
    	return score;
    }
    
    private static double passedPawns(BoardState board) throws CloneNotSupportedException
    {
    	double score = 0;
    	
    	long bitboardAlb = board.whitePawns.reprezentare;
    	long bitboardNegru = board.blackPawns.reprezentare;
    	
    	for(int i = 0; i < 8; i++)
	    {
	    	if((SlidingPieceGenerator.files[i] & bitboardAlb) != 0)
		    {
		    	if((i - 1) < 0)
			    {
			    	if(SlidingPieceGenerator.files[i] & )
			    }
		    	if((i+1) > 7)
			    {
			    
			    }
		    	if((i - 1) > 0 && (i+1) <= 7)
			    {
			    
			    }
		    }
	    }
    	
    	return score;
    }
}

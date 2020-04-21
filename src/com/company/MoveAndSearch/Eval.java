package com.company.MoveAndSearch;

import com.company.Board.Bitboard;
import com.company.Board.BoardCommands;
import com.company.Board.BoardState;
import com.company.Board.PSqTable;
import com.company.Printer;

import static com.company.Board.Bitboard.clearBit;
import static com.company.Board.Bitboard.popLSB;


public class Eval
{
	public static double eval(BoardState board, int gamePhase, boolean side) throws CloneNotSupportedException
	{
		double score = 0;
		score += getTableScore(board, gamePhase);
		score += numarPioni(board);
		score += passedPawns(board);
		score += rankPioni(board);
		//?score += castleBonus(board);
		//?score += mobilityBonus(board);
		//score += checkScore(board);
		//score += mateScore(board);
		if (!side) { // daca e negru
	        return -(score/100);
        } else {
            return (score/100);
        }

	}
	
	private static double mobilityBonus(BoardState board) throws CloneNotSupportedException
	{
		double score = 0;
		long bishopAlb = board.whiteBishops.reprezentare;
		long rookAlb = board.whiteRooks.reprezentare;
		long reginaAlb = board.whiteQueens.reprezentare;
		long bishopNegru = board.blackBishops.reprezentare;
		long rookNegru = board.blackRooks.reprezentare;
		long reginaNegru = board.blackQueens.reprezentare;
		MoveGenerator mv = new MoveGenerator(board, true);
		
		while(bishopAlb != 0)
		{
			int lsb = Bitboard.popLSB(bishopAlb);
			bishopAlb = Bitboard.clearBit(lsb, bishopAlb);
			score += Long.bitCount(mv.getBishopAttacks(lsb, board.allPieces.reprezentare, true));
		}
		
		while(rookAlb != 0)
		{
			int lsb = Bitboard.popLSB(rookAlb);
			rookAlb = Bitboard.clearBit(lsb, rookAlb);
			score += Long.bitCount(mv.getRookAttacks(lsb, board.allPieces.reprezentare, true));
		}
		
		while(reginaAlb != 0)
		{
			int lsb = Bitboard.popLSB(reginaAlb);
			reginaAlb = Bitboard.clearBit(lsb, reginaAlb);
			score += Long.bitCount(mv.getQueenAtacks(lsb, board.allPieces.reprezentare, true));
		}
		
		while(bishopNegru != 0)
		{
			int lsb = Bitboard.popLSB(bishopNegru);
			bishopNegru = Bitboard.clearBit(lsb, bishopNegru);
			score -= Long.bitCount(mv.getBishopAttacks(lsb, board.allPieces.reprezentare, false));
		}
		
		while(rookNegru != 0)
		{
			int lsb = Bitboard.popLSB(rookNegru);
			rookNegru = Bitboard.clearBit(lsb, rookNegru);
			score -= Long.bitCount(mv.getRookAttacks(lsb, board.allPieces.reprezentare, false));
		}
		
		while(reginaNegru != 0)
		{
			int lsb = Bitboard.popLSB(reginaNegru);
			reginaNegru = Bitboard.clearBit(lsb, reginaNegru);
			score -= Long.bitCount(mv.getQueenAtacks(lsb, board.allPieces.reprezentare, false));
		}
		score *= 5;
		
		return score;
	}

	private static double rankPioni(BoardState board)
	{
		double score = 0;
		long bitboardAlb = board.whitePawns.reprezentare;
		long bitboardNegru = board.blackPawns.reprezentare;

		while(bitboardAlb != 0)
		{
			int lsb = Bitboard.popLSB(bitboardAlb);
			bitboardAlb = Bitboard.clearBit(lsb, bitboardAlb);
			score += MoveGenerator.getrank.get(lsb);
		}

		while(bitboardNegru != 0)
		{
			int lsb = Bitboard.popLSB(bitboardNegru);
			bitboardNegru = Bitboard.clearBit(lsb, bitboardNegru);
			score -= (9 - MoveGenerator.getrank.get(lsb));
		}
		return (score*10);
	}
	
	private static double castleBonus(BoardState board)
	{
		double score = 0;
		
		long bitboardAlb = board.whiteKing.reprezentare;
		long bitboardAlbTura = board.whiteRooks.reprezentare;
		long bitboardNegru = board.blackKing.reprezentare;
		long bitboardNegruTura = board.blackRooks.reprezentare;
		
		int lsb = Bitboard.popLSB(bitboardNegruTura);
		bitboardNegruTura = Bitboard.clearBit(lsb, bitboardNegruTura);
		int lsbrege = Bitboard.popLSB(bitboardNegru);
		if(Math.abs(lsbrege - lsb) == 1)
		{
			score -= 15;
		}
		lsb = Bitboard.popLSB(bitboardNegruTura);
		if(Math.abs(lsbrege - lsb) == 1)
		{
			score -= 15;
		}
		
		lsb = Bitboard.popLSB(bitboardAlbTura);
		bitboardAlbTura = Bitboard.clearBit(lsb, bitboardAlbTura);
		lsbrege = Bitboard.popLSB(bitboardAlb);
		if(Math.abs(lsbrege - lsb) == 1)
		{
			score += 15;
		}
		lsb = Bitboard.popLSB(bitboardAlbTura);
		if(Math.abs(lsbrege - lsb) == 1)
		{
			score += 15;
		}
		return score;
	}

	/*private static double mateScore(BoardState board) throws CloneNotSupportedException
	{
		int score = 0;

		int pozitieRegeAlb = popLSB(board.whiteKing.reprezentare);
		if (BoardCommands.isSquareAttacked(pozitieRegeAlb, false))
		{
			MoveGenerator mv = new MoveGenerator(board);
			mv.generateKingMoves(true);
			if (mv.mutariGenerate.isEmpty())
			{
				score -= 500;
			}
		}

		int pozitieRegeNegru = popLSB(board.blackKing.reprezentare);
		if (BoardCommands.isSquareAttacked(pozitieRegeNegru, true))
		{
			MoveGenerator mv = new MoveGenerator(board);
			mv.generateKingMoves(false);
			if (mv.mutariGenerate.isEmpty())
			{
				score += 500;
			}
		}
		return score;
	}*/

	private static int getWhiteTableScore(long bitboard, int valoare, int[] value) throws CloneNotSupportedException
	{
		int score = 0;

		while (bitboard != 0)
		{
			int index = Bitboard.popLSB(bitboard);
			bitboard = clearBit(index, bitboard);
			score += valoare + value[index];
			//System.out.println("[W] psqt la index " + index + " este " + value[index]);
		}
		return score;
	}

	private static int getBlackTableScore(long bitboard, int valoare, int[] value) throws CloneNotSupportedException
	{
		int score = 0;
		while (bitboard != 0)
		{
			int index = Bitboard.popLSB(bitboard);
			bitboard = clearBit(index, bitboard);
			score += valoare + value[PSqTable.index[index]];
			//System.out.println("[B] psqt la index " + index + " este " + value[PSqTable.index[index]]);
		}
		return score;
	}

	private static double getTableScore(BoardState board, int gamePhase) throws CloneNotSupportedException
	{
		double score = 0;

		for (int i = 0; i < 6; i++)
		{
			score += getWhiteTableScore(board.whiteBitboards[i].reprezentare,
					board.whiteBitboards[i].valoare,
					PSqTable.pieceSquareTables[i]);
		}
		//System.out.println("scor dupa primu for: " + score);

		for (int i = 0; i < 6; i++)
		{
			score -= getBlackTableScore(board.blackBitboards[i].reprezentare,
					board.blackBitboards[i].valoare,
					PSqTable.pieceSquareTables[i]);
		}
		//System.out.println("scor dupa al doilea for: " + score);
		return score;
	}

	//un bonus mic pentru cine are mai multi pioni
	private static double numarPioni(BoardState board)
	{
		double score = 0;
		if (Long.bitCount(board.whitePawns.reprezentare) >
				Long.bitCount((board.blackPawns.reprezentare)))
		{
			score += 25;
		}
		if (Long.bitCount(board.whitePawns.reprezentare) <
				Long.bitCount((board.blackPawns.reprezentare)))
		{
			score -= 25;
		}
		return score;
	}

	private static double passedPawns(BoardState board) throws CloneNotSupportedException
	{
		double score = 0;

		long bitboardAlb = board.whitePawns.reprezentare;
		long bitboardNegru = board.blackPawns.reprezentare;
		for (int i = 0; i < 8; i++)
		{
			if ((SlidingPieceGenerator.files[i] & bitboardNegru) != 0)
			{
				if ((i - 1) < 0)
				{
					if ((SlidingPieceGenerator.files[i] & (bitboardAlb)) == 0)
					{
						if ((SlidingPieceGenerator.files[i + 1] & (bitboardAlb)) == 0)
						{
							score -= 20;
						}
					}
				}
				if ((i + 1) > 7)
				{
					if ((SlidingPieceGenerator.files[i] & (bitboardAlb)) == 0)
					{
						if ((SlidingPieceGenerator.files[i - 1] & (bitboardAlb)) == 0)
						{
							score -= 20;
						}
					}
				}
				if ((i - 1) >= 0 && (i + 1) <= 7)
				{
					if ((SlidingPieceGenerator.files[i] & (bitboardAlb)) == 0)
					{
						if ((SlidingPieceGenerator.files[i + 1] & (bitboardAlb)) == 0)
						{
							if ((SlidingPieceGenerator.files[i - 1] & (bitboardAlb)) == 0)
							{
								score -= 20;
							}
						}
					}
				}
			}

			if ((SlidingPieceGenerator.files[i] & bitboardAlb) != 0)
			{
				if ((i - 1) < 0)
				{
					if ((SlidingPieceGenerator.files[i] & (bitboardNegru)) == 0)
					{
						if ((SlidingPieceGenerator.files[i + 1] & (bitboardNegru)) == 0)
						{
							score += 20;
						}
					}
				}
				if ((i + 1) > 7)
				{
					if ((SlidingPieceGenerator.files[i] & (bitboardNegru)) == 0)
					{
						if ((SlidingPieceGenerator.files[i - 1] & (bitboardNegru)) == 0)
						{
							score += 20;
						}
					}
				}
				if ((i - 1) >= 0 && (i + 1) <= 7)
				{
					if ((SlidingPieceGenerator.files[i] & (bitboardNegru)) == 0)
					{
						if ((SlidingPieceGenerator.files[i + 1] & (bitboardNegru)) == 0)
						{
							if ((SlidingPieceGenerator.files[i - 1] & (bitboardNegru)) == 0)
							{
								score += 20;
							}
						}
					}
				}
			}
		}

		return score;
	}

	private static double checkScore(BoardState board)
	{
		double score = 0;

		int pozitieRegeAlb = popLSB(board.whiteKing.reprezentare);
		if (BoardCommands.isSquareAttacked(pozitieRegeAlb, false))
		{
			score -= 75;
		}

		int pozitieRegeNegru = popLSB(board.blackKing.reprezentare);
		if (BoardCommands.isSquareAttacked(pozitieRegeNegru, true))
		{
			score += 75;
		}

		return score;
	}
}

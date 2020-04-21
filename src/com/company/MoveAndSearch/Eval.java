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
		//score += checkScore(board);
		//score += mateScore(board);
		if (!side) { // daca e negru
	        return -(score/100);
        } else {
            return (score/100);
        }
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
		return (score*5);
	}

	private static double mateScore(BoardState board) throws CloneNotSupportedException
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
	}

	private static int getWhiteTableScore(long bitboard, int valoare, int[] value) throws CloneNotSupportedException
	{
		int score = 0;

		while (bitboard != 0)
		{
			int index = Bitboard.popLSB(bitboard);
			bitboard = clearBit(index, bitboard);
			score += valoare + value[index];
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

		for (int i = 0; i < 6; i++)
		{
			score -= getBlackTableScore(board.blackBitboards[i].reprezentare,
					board.blackBitboards[i].valoare,
					PSqTable.pieceSquareTables[i]);
		}
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

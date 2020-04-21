package com.company.MoveAndSearch;

import com.company.Board.Bitboard;
import com.company.Board.BoardCommands;
import com.company.Board.BoardState;
import com.company.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static com.company.Board.Bitboard.popLSB;

public class MoveGenerator
{
	public PriorityQueue<Move> mutariGenerate;
	public ArrayList<Move> mutariDebugIGuess = new ArrayList<>();
	BoardState board;
	boolean siiiide;
	
	//helpere
	static HashMap<Integer, Integer> getrank;
	
	int captures = 0;
	int ep = 0;
	int promotions = 0;
	int castle = 0;
	
	public void stats()
	{
		System.out.println("capturari " + captures);
		System.out.println("en passant " + ep);
		System.out.println("promotii " + promotions);
		System.out.println("castle " + castle);
	}
	
	public MoveGenerator()
	{
	}
	
	public MoveGenerator(BoardState board, boolean siiiide) throws CloneNotSupportedException
	{
		this.siiiide = siiiide;
		//in hashpmap o sa am [0,1,2,3,4,5,6,7] cu cheia 1
		//[8,9,10,11,12,13,14,15] cu cheia 2 etc
		//ca sa pot lua usor rank-ul dupa pozitie
		mutariGenerate = new PriorityQueue<>();
		this.board = board;
	}
	
	public static void initMoveGenerator()
	{
		getrank = new HashMap<>();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				getrank.put((i) * 8 + j, i + 1);
			}
		}
	}
	
	public void generateAllMoves(boolean side) throws CloneNotSupportedException
	{
		generateBishopMoves(side);
		generateQueenMoves(side);
		generatePawnMoves(side);
		generateKnightMoves(side);
		generateRookMoves(side);
		generateKingMoves(side);
	}
	
	public void generateAllMovesAndStats(boolean side) throws CloneNotSupportedException
	{
		long time = System.nanoTime();
		generateKnightMoves(side);
		generateBishopMoves(side);
		generateQueenMoves(side);
		generatePawnMoves(side);
		generateRookMoves(side);
		generateKingMoves(side);
		time = System.nanoTime() - time;
		System.out.print("mi-a luat " + time + " nano (" + (double) time / 1_000_000_000 + " sec) " + " sa generez ");
		System.out.println(mutariGenerate.size() + " mutari");
		stats();
		while (!mutariGenerate.isEmpty())
		{
			mutariGenerate.poll().printMove();
		}
	}
	
	//helper
	public int getRank(int pozitiePiesaIn64)
	{
		return getrank.get(pozitiePiesaIn64);
	}
	
	//printer
	public void moveListPrint()
	{
		for (Move m : mutariGenerate)
		{
			m.printMove();
		}
	}
	
	//adaugare de mutari
	//quiet move fara capturare
	void addMove(Move mutare)
	{
		//mutare.prioritate = 0;
		Negamax.applyMove(board, mutare, siiiide);
		long bitboard = siiiide ? board.whiteKing.reprezentare : board.blackKing.reprezentare;
		int lsb = Bitboard.popLSB(bitboard);
		if (!BoardCommands.isSquareAttacked(lsb, !siiiide)) {
			mutariGenerate.add(mutare);
		}
		Negamax.undoMove(board, mutare, siiiide);
		mutariDebugIGuess.add(mutare);
	}
	
	void addCaptureMove(Move mutare)
	{
		captures++;
		mutare.prioritate += 1;
		mutare.prioritate += 5 - mutare.piesa; //daca e pion o sa fie 5, daca e regina o sa fie 1, LEAST VALUABLE ATACKER
		mutare.prioritate += mutare.piesaDestinatie; //daca e pion o sa fie 0, daca e regina o sa fie 4, MOST VALUABLE VICTIM
		Negamax.applyMove(board, mutare, siiiide);
		long bitboard = siiiide ? board.whiteKing.reprezentare : board.blackKing.reprezentare;
		int lsb = Bitboard.popLSB(bitboard);
		if (!BoardCommands.isSquareAttacked(lsb, !siiiide)) {
			mutariGenerate.add(mutare);
		}
		Negamax.undoMove(board, mutare, siiiide);
		mutariDebugIGuess.add(mutare);
	}
	
	void addEnPassantMove(Move mutare)
	{
		mutare.prioritate = 0;
		ep++;
		Negamax.applyMove(board, mutare, siiiide);
		long bitboard = siiiide ? board.whiteKing.reprezentare : board.blackKing.reprezentare;
		int lsb = Bitboard.popLSB(bitboard);
		if (!BoardCommands.isSquareAttacked(lsb, !siiiide)) {
			mutariGenerate.add(mutare);
		}
		Negamax.undoMove(board, mutare, siiiide);
		mutariDebugIGuess.add(mutare);
	}
	
	//creator de mutari
	Move createMove(int sursa, int destinatie, int promotie, int flag, int piesa, int piesaDestinatie)
	{
		Move mutare = new Move();
		mutare.sursa = sursa;
		mutare.destinatie = destinatie;
		mutare.promotie = promotie;
		mutare.flag = flag;
		mutare.piesa = piesa;
		mutare.piesaDestinatie = piesaDestinatie;
		return mutare;
	}
	
	//generatoare de mutari
	//primeste white sau black pawns bitboard
	private void generatePawnMoves(boolean side) throws CloneNotSupportedException
	{
		Bitboard bitBoard;
		if (side)
		{
			bitBoard = (Bitboard) board.whitePawns.clone();
		} else
		{
			bitBoard = (Bitboard) board.blackPawns.clone();
		}
		while (bitBoard.reprezentare != 0)
		{
			//extrag locul unde se afla piesa
			int pozitie = popLSB(bitBoard.reprezentare);
			bitBoard.clearBit(pozitie);
			Move move;
			int temp = Database.conversie64la120(pozitie);
			int negruDreapta = temp - 11;
			int negruStanga = temp - 9;
			int albStanga = temp + 11;
			int albDreapta = temp + 9;
			
			//daca suntem pe alb
			if (side)
			{
				if (getRank(pozitie) == 7) //daca suntem pe penultimul rank inseamna ca pot sa si fac promotie
				{
					//setez flag de promotie
					if (!board.allPieces.isBitSet(pozitie + 8))
					{
						//adaug miscare pion alb in fata daca regele nu e in sah
						for (int i = 3; i >= 0; i--)
						{
							move = createMove(pozitie, pozitie + 8, i, 1, 0, BoardCommands.getPieceType(board, pozitie + 8, false));
							addMove(move);
						}
						promotions++;
					}
					
					if (Database.conversie120la64(albDreapta) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albDreapta)))
					{
						//adaug miscare pion alb in dreapta daca regele nu e in sah
						if (board.allBlackPieces.isBitSet(Database.conversie120la64(albDreapta)))
						{
							for (int i = 3; i >= 0; i--)
							{
								move = createMove(pozitie, pozitie + 7, i, 1, 0, BoardCommands.getPieceType(board, pozitie + 7, false));
								addCaptureMove(move);
							}
							promotions++;
						}
					}
					if (Database.conversie120la64(albStanga) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albStanga)))
					{
						//adaug miscare pion alb in dreapta daca regele nu e in sah
						if (board.allBlackPieces.isBitSet(Database.conversie120la64(albStanga)))
						{
							for (int i = 3; i >= 0; i--)
							{
								move = createMove(pozitie, pozitie + 9, i, 1, 0, BoardCommands.getPieceType(board, pozitie + 9, false));
								addCaptureMove(move);
							}
							promotions++;
						}
					}
				} else
				{
					if (getRank(pozitie) == 5)
					{
						if (Database.conversie120la64(albDreapta) != 65 && (board.enPassant == Database.conversie120la64(albDreapta)))
						{
							move = createMove(pozitie, pozitie + 7, 0, 2, 0, BoardCommands.getPieceType(board, pozitie + 7, false));
							addEnPassantMove(move);
						}
						if (Database.conversie120la64(albStanga) != 65 && (board.enPassant == Database.conversie120la64(albStanga)))
						{
							move = createMove(pozitie, pozitie + 9, 0, 2, 0, BoardCommands.getPieceType(board, pozitie + 9, false));
							addEnPassantMove(move);
						}
					}
					if (getRank(pozitie) == 2)
					{
						if (!board.allPieces.isBitSet(pozitie + 16) && !board.allPieces.isBitSet(pozitie + 8))
						{
							move = createMove(pozitie, pozitie + 16, 0, 0, 0, BoardCommands.getPieceType(board, pozitie + 16, false));
							addMove(move);
						}
					}
					if (!board.allPieces.isBitSet(pozitie + 8))
					{
						move = createMove(pozitie, pozitie + 8, 0, 0, 0, BoardCommands.getPieceType(board, pozitie + 8, false));
						addMove(move);
					}
					if (Database.conversie120la64(albDreapta) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albDreapta)))
					{
						if (board.allBlackPieces.isBitSet(Database.conversie120la64(albDreapta)))
						{
							move = createMove(pozitie, pozitie + 7, 0, 0, 0, BoardCommands.getPieceType(board, pozitie + 7, false));
							addCaptureMove(move);
						}
					}
					if (Database.conversie120la64(albStanga) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albStanga)))
					{
						if (board.allBlackPieces.isBitSet(Database.conversie120la64(albStanga)))
						{
							move = createMove(pozitie, pozitie + 9, 0, 0, 0, BoardCommands.getPieceType(board, pozitie + 9, false));
							addCaptureMove(move);
						}
					}
				}
			} else
			{ //negru ghici
				if (getRank(pozitie) == 2)
				{ //daca suntem pe penultimul rank inseamna ca pot sa si fac promotie
					//setez flag de promotie
					if (!board.allPieces.isBitSet(pozitie - 8))
					{
						//adaug miscare pion negru in fata daca regele nu e in sah
						for (int i = 3; i >= 0; i--)
						{
							move = createMove(pozitie, pozitie - 8, i, 1, 0, BoardCommands.getPieceType(board, pozitie - 8, true));
							addMove(move);
						}
						promotions++;
					}
					if (Database.conversie120la64(negruDreapta) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruDreapta)))
					{
						//adaug miscare pion negru in dreapta daca regele nu e in sah
						if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruDreapta)))
						{
							for (int i = 3; i >= 0; i--)
							{
								move = createMove(pozitie, pozitie - 9, i, 1, 0, BoardCommands.getPieceType(board, pozitie - 9, true));
								addCaptureMove(move);
							}
							promotions++;
						}
					}
					if (Database.conversie120la64(negruStanga) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruStanga)))
					{
						//adaug miscare pion negru in dreapta daca regele nu e in sah
						if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruStanga)))
						{
							for (int i = 3; i >= 0; i--)
							{
								move = createMove(pozitie, pozitie - 7, i, 1, 0, BoardCommands.getPieceType(board, pozitie - 7, true));
								addCaptureMove(move);
							}
							promotions++;
						}
					}
				} else
				{
					if (getRank(pozitie) == 4)
					{
						if (Database.conversie120la64(negruDreapta) != 65 && (board.enPassant == Database.conversie120la64(negruDreapta)))
						{
							move = createMove(pozitie, pozitie - 9, 0, 2, 0, BoardCommands.getPieceType(board, pozitie - 9, true));
							addEnPassantMove(move);
						}
						if (Database.conversie120la64(negruStanga) != 65 && (board.enPassant == Database.conversie120la64((negruStanga))))
						{
							move = createMove(pozitie, pozitie - 7, 0, 2, 0, BoardCommands.getPieceType(board, pozitie - 7, true));
							addEnPassantMove(move);
						}
					}
					if (getRank(pozitie) == 7)
					{
						if (!board.allPieces.isBitSet(pozitie - 16) && !board.allPieces.isBitSet(pozitie - 8))
						{
							move = createMove(pozitie, pozitie - 16, 0, 0, 0, BoardCommands.getPieceType(board, pozitie - 16, true));
							addMove(move);
						}
					}
					if (!board.allPieces.isBitSet(pozitie - 8))
					{
						move = createMove(pozitie, pozitie - 8, 0, 0, 0, BoardCommands.getPieceType(board, pozitie - 8, true));
						addMove(move);
					}
					if (Database.conversie120la64(negruDreapta) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruDreapta)))
					{
						if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruDreapta)))
						{
							move = createMove(pozitie, pozitie - 9, 0, 0, 0, BoardCommands.getPieceType(board, pozitie - 9, true));
							addCaptureMove(move);
						}
					}
					if (Database.conversie120la64(negruStanga) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruStanga)))
					{
						if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruStanga)))
						{
							move = createMove(pozitie, pozitie - 7, 0, 0, 0, BoardCommands.getPieceType(board, pozitie - 7, true));
							addCaptureMove(move);
						}
					}
				}
			}
		}
	}
	
	public void generateKnightMoves(boolean side) throws CloneNotSupportedException
	{
		Bitboard bitBoard;
		if (side)
		{
			bitBoard = (Bitboard) board.whiteKnights.clone();
		} else
		{
			bitBoard = (Bitboard) board.blackKnights.clone();
		}
		while (bitBoard.reprezentare != 0)
		{
			int pozitie = popLSB(bitBoard.reprezentare);
			bitBoard.clearBit(pozitie);
			int temp = Database.conversie64la120(pozitie);
			final int[] directiiCal = new int[]{-8, -19, -21, -12, 8, 19, 21, 12};
			for (int i = 0; i < 8; i++)
			{
				int checker = Database.conversie120la64(temp + directiiCal[i]);
				Move move;
				if (checker != 65)
				{
					if (side)
					{
						if (!board.allWhitePieces.isBitSet(checker))
						{
							if (board.allBlackPieces.isBitSet(checker))
							{
								move = createMove(pozitie, checker, 0, 0, 1, BoardCommands.getPieceType(board, checker, !side));
								addCaptureMove(move);
							} else
							{
								move = createMove(pozitie, checker, 0, 0, 1, BoardCommands.getPieceType(board, checker, !side));
								addMove(move);
							}
						}
					} else
					{
						if (!board.allBlackPieces.isBitSet(checker))
						{
							if (board.allWhitePieces.isBitSet(checker))
							{
								move = createMove(pozitie, checker, 0, 0, 1, BoardCommands.getPieceType(board, checker, !side));
								addCaptureMove(move);
							} else
							{
								move = createMove(pozitie, checker, 0, 0, 1, BoardCommands.getPieceType(board, checker, !side));
								addMove(move);
							}
						}
					}
				}
			}
		}
	}
	
	//sa nu generez miscari ilegale
	public void generateKingMoves(boolean side) throws CloneNotSupportedException
	{
		Bitboard bitBoard;
		if (side)
		{
			bitBoard = board.whiteKing.clone();
		} else
		{
			bitBoard = board.blackKing.clone();
		}
		int pozitie = popLSB(bitBoard.reprezentare);
		Move move;
		if (side)
		{
			if (board.castlePermission[0] == 1)
			{
				if (!board.allPieces.isBitSet(pozitie - 1) &&
						!board.allPieces.isBitSet(pozitie - 2) &&
						(Bitboard.popLSB(board.whiteKing.reprezentare) == 3))
				{
					if (!BoardCommands.isSquareAttacked(pozitie - 2, false))
					{
						move = createMove(pozitie, pozitie - 2, 0, 3, 5, BoardCommands.getPieceType(board, pozitie - 2, !side));
						addMove(move);
						castle++;
					}
				}
			}
			if (board.castlePermission[1] == 1)
			{
				if (!board.allPieces.isBitSet(pozitie + 1) &&
						!board.allPieces.isBitSet(pozitie + 2) &&
						!board.allPieces.isBitSet(pozitie + 3) &&
						(Bitboard.popLSB(board.whiteKing.reprezentare) == 3))
				{
					if (!BoardCommands.isSquareAttacked(pozitie + 2, false))
					{
						move = createMove(pozitie, pozitie + 2, 0, 3, 5, BoardCommands.getPieceType(board, pozitie + 2, !side));
						addMove(move);
						castle++;
					}
				}
			}
		} else
		{
			if (board.castlePermission[2] == 1)
			{
				if (!board.allPieces.isBitSet(pozitie - 1) &&
						!board.allPieces.isBitSet(pozitie - 2) &&
						(Bitboard.popLSB(board.blackKing.reprezentare) == 59))
				{
					if (!BoardCommands.isSquareAttacked(pozitie - 2, true))
					{
						move = createMove(pozitie, pozitie - 2, 0, 3, 5, BoardCommands.getPieceType(board, pozitie - 2, !side));
						addMove(move);
						castle++;
					}
				}
			}
			if (board.castlePermission[3] == 1)
			{
				if (!board.allPieces.isBitSet(pozitie + 1) &&
						!board.allPieces.isBitSet(pozitie + 2) &&
						!board.allPieces.isBitSet(pozitie + 3) &&
						(Bitboard.popLSB(board.blackKing.reprezentare) == 59))
				{
					if (!BoardCommands.isSquareAttacked(pozitie + 2, true))
					{
						move = createMove(pozitie, pozitie + 2, 0, 3, 5, BoardCommands.getPieceType(board, pozitie + 2, !side));
						addMove(move);
						castle++;
					}
				}
			}
		}

		while (bitBoard.reprezentare != 0)
		{
			//pozitie = popLSB(bitBoard.reprezentare);
			bitBoard.clearBit(pozitie);
			int temp = Database.conversie64la120(pozitie);
			final int[] directiiRege = new int[]{-1, -10, 1, 10, -9, -11, 11, 9};
			for (int i = 0; i < 8; i++)
			{
				int checker = Database.conversie120la64(temp + directiiRege[i]);
				if (checker != 65)
				{
					if (side)
					{
						if (!board.allWhitePieces.isBitSet(checker))
						{
							if (board.allBlackPieces.isBitSet(checker))
							{
								if (!BoardCommands.isSquareAttacked(checker, false))
								{
									move = createMove(pozitie, checker, 0, 0, 5, BoardCommands.getPieceType(board, checker, !side));
									addCaptureMove(move);
								}
							} else
							{
								if (!BoardCommands.isSquareAttacked(checker, false))
								{
									move = createMove(pozitie, checker, 0, 0, 5, BoardCommands.getPieceType(board, checker, !side));
									addMove(move);
								}
							}
						}
					} else
					{
						if (!board.allBlackPieces.isBitSet(checker))
						{
							if (board.allWhitePieces.isBitSet(checker))
							{
								if (!BoardCommands.isSquareAttacked(checker, true))
								{
									move = createMove(pozitie, checker, 0, 0, 5, BoardCommands.getPieceType(board, checker, !side));
									addCaptureMove(move);
								}
							} else
							{
								if (!BoardCommands.isSquareAttacked(checker, true))
								{
									move = createMove(pozitie, checker, 0, 0, 5, BoardCommands.getPieceType(board, checker, !side));
									addMove(move);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private long getBishopAttacks(int pozitie, long blockers, boolean side)
	{
		blockers &= SlidingPieceGenerator.bishopMasks[pozitie];
		long rezultat = SlidingPieceGenerator.magicBishopTable[pozitie][(int) ((blockers * SlidingPieceGenerator.bishopMagics[pozitie])
				>>> (64 - SlidingPieceGenerator.bishopIndexBits[pozitie]))];
		if (side)
		{
			rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		} else
		{
			rezultat &= ~BoardState.getInstance().allBlackPieces.reprezentare;
		}
		return rezultat;
	}
	
	private long getRookAttacks(int pozitie, long blockers, boolean side)
	{
		blockers &= SlidingPieceGenerator.rookMasks[pozitie];
		int key = (int) ((blockers * SlidingPieceGenerator.rookMagics[pozitie])
				>>> (64 - SlidingPieceGenerator.rookIndexBits[pozitie]));
		long rezultat = SlidingPieceGenerator.magicRookTable[pozitie][key];
		if (side)
		{
			rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		} else
		{
			rezultat &= ~BoardState.getInstance().allBlackPieces.reprezentare;
		}
		return rezultat;
	}
	
	private long getQueenAtacks(int pozitie, long blockers, boolean side)
	{
		long rezultat = getRookAttacks(pozitie, blockers, side) | getBishopAttacks(pozitie, blockers, side);
		rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		if (side)
		{
			rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		} else
		{
			rezultat &= ~BoardState.getInstance().allBlackPieces.reprezentare;
		}
		return rezultat;
	}
	
	private void generateRookMoves(boolean side) throws CloneNotSupportedException
	{
		Bitboard bitBoard;
		if (side)
		{
			bitBoard = (Bitboard) board.whiteRooks.clone();
		} else
		{
			bitBoard = (Bitboard) board.blackRooks.clone();
		}
		while (bitBoard.reprezentare != 0)
		{
			int pozitie = popLSB(bitBoard.reprezentare);
			bitBoard.clearBit(pozitie);
			long atackBoard = getRookAttacks(pozitie, board.allPieces.reprezentare, side);
			while (atackBoard != 0)
			{
				Move move;
				int checker = popLSB(atackBoard);
				atackBoard = Bitboard.clearBit(checker, atackBoard);
				if (side)
				{
					if (board.allBlackPieces.isBitSet(checker))
					{
						move = createMove(pozitie, checker, 0, 0, 3, BoardCommands.getPieceType(board, checker, !side));
						addCaptureMove(move);
					} else
					{
						move = createMove(pozitie, checker, 0, 0, 3, BoardCommands.getPieceType(board, checker, !side));
						addMove(move);
					}
				} else
				{
					if (board.allWhitePieces.isBitSet(checker))
					{
						move = createMove(pozitie, checker, 0, 0, 3, BoardCommands.getPieceType(board, checker, !side));
						addCaptureMove(move);
					} else
					{
						move = createMove(pozitie, checker, 0, 0, 3, BoardCommands.getPieceType(board, checker, !side));
						addMove(move);
					}
				}
			}
		}
	}
	
	private void generateBishopMoves(boolean side) throws CloneNotSupportedException
	{
		Bitboard bitBoard;
		if (side)
		{
			bitBoard = (Bitboard) board.whiteBishops.clone();
		} else
		{
			bitBoard = (Bitboard) board.blackBishops.clone();
		}
		while (bitBoard.reprezentare != 0)
		{
			int pozitie = popLSB(bitBoard.reprezentare);
			bitBoard.clearBit(pozitie);
			long atackBoard = getBishopAttacks(pozitie, board.allPieces.reprezentare, side);
			while (atackBoard != 0)
			{
				Move move;
				int checker = popLSB(atackBoard);
				atackBoard = Bitboard.clearBit(checker, atackBoard);
				if (side)
				{
					if (board.allBlackPieces.isBitSet(checker))
					{
						move = createMove(pozitie, checker, 0, 0, 2, BoardCommands.getPieceType(board, checker, !side));
						addCaptureMove(move);
					} else
					{
						move = createMove(pozitie, checker, 0, 0, 2, BoardCommands.getPieceType(board, checker, !side));
						addMove(move);
					}
				} else
				{
					if (board.allWhitePieces.isBitSet(checker))
					{
						move = createMove(pozitie, checker, 0, 0, 2, BoardCommands.getPieceType(board, checker, !side));
						addCaptureMove(move);
					} else
					{
						move = createMove(pozitie, checker, 0, 0, 2, BoardCommands.getPieceType(board, checker, !side));
						addMove(move);
					}
				}
			}
		}
	}
	
	private void generateQueenMoves(boolean side) throws CloneNotSupportedException
	{
		Bitboard bitBoard;
		if (side)
		{
			bitBoard = (Bitboard) board.whiteQueens.clone();
		} else
		{
			bitBoard = (Bitboard) board.blackQueens.clone();
		}
		while (bitBoard.reprezentare != 0)
		{
			int pozitie = popLSB(bitBoard.reprezentare);
			bitBoard.clearBit(pozitie);
			long atackBoard = getQueenAtacks(pozitie, board.allPieces.reprezentare, side);
			while (atackBoard != 0)
			{
				Move move;
				int checker = popLSB(atackBoard);
				atackBoard = Bitboard.clearBit(checker, atackBoard);
				if (side)
				{
					if (board.allBlackPieces.isBitSet(checker))
					{
						move = createMove(pozitie, checker, 0, 0, 4, BoardCommands.getPieceType(board, checker, !side));
						addCaptureMove(move);
					} else
					{
						move = createMove(pozitie, checker, 0, 0, 4, BoardCommands.getPieceType(board, checker, !side));
						addMove(move);
					}
				} else
				{
					if (board.allWhitePieces.isBitSet(checker))
					{
						move = createMove(pozitie, checker, 0, 0, 4, BoardCommands.getPieceType(board, checker, !side));
						addCaptureMove(move);
					} else
					{
						move = createMove(pozitie, checker, 0, 0, 4, BoardCommands.getPieceType(board, checker, !side));
						addMove(move);
					}
				}
			}
		}
	}
}

package com.company.Board;

/**
 * Clasa asta tine toate variabilele legate de starea unui board (bitboards, castling etc) si comenzi de baza
 * pentru prelucrarea lor (ex: initializare, resetare)
 */

public class BoardState implements Cloneable{
	private static BoardState instance;
	/*
	 piese { EMPTY, wP, wN, wB, wR, wQ, wK, bP, bN, bB, bR, bQ, bK };
	 File { FILE_A, FILE_B, FILE_C, FILE_D, FILE_E, FILE_F, FILE_G, FILE_NONE };
	 Rank { RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8, RANK_NONE };
	*/

	/* tin un array de 120 care acopera tabla in toate
	directiile ca sa verific daca iese ceva de pe tabla.
	am 2 randuri in plus sus si jos din cauza calului.
	cu indexi de la 21 la 98
	 */
	public static int[] pieseFormat120 = new int[120];
	/* tin un array de 64 care tine minte doar tabla din mijloc
	cu indexi de la 0 la 63.
	 */
	public static int[] pieseFormat64 = new int[64];
	
	//numar de la 0 la 63 unde este square-ul en passant
	public int enPassant = 0;
	
	/* Pozitia pieselor albe */
	public Bitboard whitePawns = new Bitboard();
	public Bitboard whiteRooks = new Bitboard();
	public Bitboard whiteKnights = new Bitboard();
	public Bitboard whiteBishops = new Bitboard();
	public Bitboard whiteQueens = new Bitboard();
	public Bitboard whiteKing = new Bitboard();

	/* Pozitia pieselor negre */
	public Bitboard blackPawns = new Bitboard();
	public Bitboard blackRooks = new Bitboard();
	public Bitboard blackKnights = new Bitboard();
	public Bitboard blackBishops = new Bitboard();
	public Bitboard blackQueens = new Bitboard();
	public Bitboard blackKing = new Bitboard();

	/* Helpere */
	public Bitboard allWhitePieces = new Bitboard();
	public Bitboard allBlackPieces = new Bitboard();
	public Bitboard allPieces = new Bitboard();

	public Bitboard[] allBitboards = {whitePawns, whiteKnights, whiteBishops,
			whiteRooks, whiteQueens, whiteKing,
			blackPawns, blackKnights, blackBishops,
			blackRooks, blackQueens, blackKing};
	
	public Bitboard[] whiteBitboards = {whiteQueens, whiteRooks, whiteKing,
			whiteBishops, whiteKnights, whitePawns};
	
	public Bitboard[] blackBitboards = {blackQueens, blackRooks, blackKing,
			blackBishops, blackKnights, blackPawns};

	public int materialAlb = 0;
	public int materalNegru = 0;
	
	/*
	 0 0 0 0
	albRege, albRegina, negruRege, negruRegina
	se vede ca un numar de 4 biti
	daca bitul e setat inseamna ca se poate
	*/
	public int[] castlePermission = {0,0,0,0};
	
	//istoric de miscari, tin sho minte 10 mutari plm
	//public MoveHistory istoric[] = new MoveHistory[10];


	@Override
	public BoardState clone() throws CloneNotSupportedException {
		BoardState clone = (BoardState) super.clone();
		clone.whitePawns = this.whitePawns.clone();
		clone.blackPawns = this.blackPawns.clone();
		clone.whiteRooks = this.whiteRooks.clone();
		clone.blackRooks = this.blackRooks.clone();
		clone.whiteKnights = this.whiteKnights.clone();
		clone.blackKnights = this.blackKnights.clone();
		clone.whiteBishops = this.whiteBishops.clone();
		clone.blackBishops = this.blackBishops.clone();
		clone.whiteQueens = this.whiteQueens.clone();
		clone.blackQueens = this.blackQueens.clone();
		clone.whiteKing = this.whiteKing.clone();
		clone.blackKing = this.blackKing.clone();

		clone.allWhitePieces = this.allWhitePieces.clone();
		clone.allBlackPieces = this.allBlackPieces.clone();
		clone.allPieces = this.allPieces.clone();

		clone.allBitboards = new Bitboard[] {clone.whitePawns, clone.whiteKnights, clone.whiteBishops,
											 clone.whiteRooks, clone.whiteQueens, clone.whiteKing,
											 clone.blackPawns, clone.blackKnights, clone.blackBishops,
											 clone.blackRooks, clone.blackQueens, clone.blackKing};

		clone.whiteBitboards = new Bitboard[] {clone.whitePawns, clone.whiteKnights, clone.whiteBishops,
											   clone.whiteRooks, clone.whiteQueens, clone.whiteKing};

		clone.blackBitboards = new Bitboard[] {clone.blackPawns, clone.blackKnights, clone.blackBishops,
											   clone.blackRooks, clone.blackQueens, clone.blackKing};

		clone.castlePermission = this.castlePermission.clone();
		return clone;
	}

	public static BoardState getInstance() {
		if (instance == null) {
			instance = new BoardState();
			getInstance().initializareValoarePiese();
		}
		return instance;
	}

	public void initializareValoarePiese() {
		instance.whitePawns.valoare = 100;
		instance.whiteKnights.valoare = 320;
		instance.whiteBishops.valoare = 330;
		instance.whiteRooks.valoare = 500;
		instance.whiteQueens.valoare = 900;
		instance.whiteKing.valoare = 20000;

		instance.blackPawns.valoare = 100;
		instance.blackKnights.valoare = 320;
		instance.blackBishops.valoare = 330;
		instance.blackRooks.valoare = 500;
		instance.blackQueens.valoare = 900;
		instance.blackKing.valoare = 20000;
	}

	public void updateBitboards() {
		allBlackPieces.reprezentare = blackPawns.reprezentare | blackBishops.reprezentare | blackRooks.reprezentare |
									  blackKnights.reprezentare | blackKing.reprezentare | blackQueens.reprezentare;

		allWhitePieces.reprezentare = whitePawns.reprezentare | whiteBishops.reprezentare | whiteRooks.reprezentare |
									  whiteKnights.reprezentare | whiteKing.reprezentare | whiteQueens.reprezentare;

		allPieces.reprezentare = allBlackPieces.reprezentare | allWhitePieces.reprezentare;
	}

	public void resetBoard() {
		for (Bitboard b : allBitboards) {
			b.resetBitboard();
		}
	}
}

package com.company.Board;

/**
 * Clasa asta tine toate variabilele legate de starea unui board (bitboards, castling etc) si comenzi de baza
 * pentru prelucrarea lor (ex: initializare, resetare)
 */

public class BoardState {
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
	public int[] pieseFormat120 = new int[120];
	/* tin un array de 64 care tine minte doar tabla din mijloc
	cu indexi de la 0 la 63.
	 */
	public int[] pieseFormat64 = new int[64];
	
	public int enPassant = 0;
	
	/* Pozitia pieselor albe */
	public Bitboard WhitePawns = new Bitboard();
	public Bitboard WhiteRooks = new Bitboard();
	public Bitboard WhiteKnights = new Bitboard();
	public Bitboard WhiteBishops = new Bitboard();
	public Bitboard WhiteQueens = new Bitboard();
	public Bitboard WhiteKing = new Bitboard();

	/* Pozitia pieselor negre */
	public Bitboard BlackPawns = new Bitboard();
	public Bitboard BlackRooks = new Bitboard();
	public Bitboard BlackKnights = new Bitboard();
	public Bitboard BlackBishops = new Bitboard();
	public Bitboard BlackQueens = new Bitboard();
	public Bitboard BlackKing = new Bitboard();

	public Bitboard[] Pawns = {WhitePawns, BlackPawns};
	public Bitboard[] Rooks = {WhiteRooks, BlackRooks};
	public Bitboard[] Knights = {WhiteKnights, BlackKnights};
	public Bitboard[] Bishops = {WhiteBishops, BlackBishops};
	public Bitboard[] Queens = {WhiteQueens, BlackQueens};
	public Bitboard[] Kings = {WhiteKing, BlackKing};

	/* Helpere */
	public Bitboard AllWhitePieces = new Bitboard();
	public Bitboard AllBlackPieces = new Bitboard();
	public Bitboard AllPieces = new Bitboard();

	public Bitboard[] allBitboards = {WhitePawns, WhiteKnights, WhiteBishops,
			                          WhiteRooks, WhiteQueens, WhiteKing,
									  BlackPawns,BlackKnights, BlackBishops,
									  BlackRooks, BlackQueens, BlackKing};
	
	public Bitboard[] WhiteBitboards = {WhiteQueens, WhiteRooks, WhiteKing,
			WhiteBishops, WhiteKnights, WhitePawns};
	
	public Bitboard[] BlackBitboards = {BlackQueens, BlackRooks, BlackKing,
			BlackBishops, BlackKnights, BlackPawns};
	
	public int materialAlb = 0;
	public int materalNegru = 0;
	
	/*
	 0 0 0 0
	albRege, albRegina, negruRege, negruRegina
	se vede ca un numar de 4 biti
	daca bitul e setat inseamna ca se poate
	*/
	public int castlePermision[] = {0,0,0,0};
	
	//istoric de miscari, tin sho minte 10 mutari plm
	public MoveHistory istoric[] = new MoveHistory[10];

	public static BoardState getInstance() {
		if (instance == null) {
			instance = new BoardState();
			getInstance().initializareValoarePiese();
		}
		return instance;
	}

	public void initializareValoarePiese() {
		instance.WhitePawns.valoare = 100;
		instance.WhiteKnights.valoare = 325;
		instance.WhiteBishops.valoare = 325;
		instance.WhiteRooks.valoare = 550;
		instance.WhiteQueens.valoare = 1000;
		instance.WhiteKing.valoare = 50000;

		instance.BlackPawns.valoare = 100;
		instance.BlackKnights.valoare = 325;
		instance.BlackBishops.valoare = 325;
		instance.BlackRooks.valoare = 550;
		instance.BlackQueens.valoare = 1000;
		instance.BlackKing.valoare = 50000;
	}

	public void updateBitboards() {
		AllBlackPieces.reprezentare = BlackPawns.reprezentare | BlackBishops.reprezentare | BlackRooks.reprezentare |
									  BlackKnights.reprezentare | BlackKing.reprezentare | BlackQueens.reprezentare;

		AllWhitePieces.reprezentare = WhitePawns.reprezentare | WhiteBishops.reprezentare | WhiteRooks.reprezentare |
									  WhiteKnights.reprezentare | WhiteKing.reprezentare | WhiteQueens.reprezentare;

		AllPieces.reprezentare = AllBlackPieces.reprezentare | AllWhitePieces.reprezentare;
	}

	public void resetBoard() {
		for (Bitboard b : allBitboards) {
			b.resetBitboard();
		}
	}
}

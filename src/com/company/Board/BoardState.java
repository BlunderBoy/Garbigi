package com.company.Board;

import java.util.ArrayList;

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

	/* Helpere */
	public Bitboard AllWhitePieces = new Bitboard();
	public Bitboard AllBlackPieces = new Bitboard();
	public Bitboard AllPieces = new Bitboard();

	public Bitboard[] allBitboards = {WhitePawns, WhiteKnights, WhiteBishops,
			                          WhiteRooks, WhiteQueens, WhiteKing,
									  BlackPawns,BlackKnights, BlackBishops,
									  BlackRooks, BlackQueens, BlackKing};
	
	public int materialAlb = 0;
	public int materalNegru = 0;
	
	/*
	 0 0 0 0
	albRege, albRegina, negruRege, negruRegina
	se vede ca un numar de 4 biti
	daca bitul e setat inseamna ca se poate
	*/
	public char castlePermision[] = {0,0,0,0};
	
	//istoric de miscari, tin sho minte 10 mutari plm
	public MoveHistory istoric[] = new MoveHistory[10];

	public static BoardState getInstance() {
		if (instance == null) {
			instance = new BoardState();
		}
		return instance;
	}

	public void resetBoard() {
		for (Bitboard b : allBitboards) {
			b.resetBitboard();
		}
	}
}

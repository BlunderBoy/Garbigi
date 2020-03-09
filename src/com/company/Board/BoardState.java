package com.company.Board;

import java.util.ArrayList;

public class BoardState
{
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
	int[] pieseFormat120 = new int[120];
	/* tin un array de 64 care tine minte doar tabla din mijloc
	cu indexi de la 0 la 63.
	 */
	int[] pieseFormat64 = new int[64];
	
	int enPassant = 0;
	
	/* Pozitia pieselor albe */
	Bitboard WhitePawns;
	Bitboard WhiteRooks;
	Bitboard WhiteKnights;
	Bitboard WhiteBishops;
	Bitboard WhiteQueens;
	Bitboard WhiteKing;

	/* Pozitia pieselor negre */
	Bitboard BlackPawns;
	Bitboard BlackRooks;
	Bitboard BlackKnights;
	Bitboard BlackBishops;
	Bitboard BlackQueens;
	Bitboard BlackKing;

	/* Helpere */
	Bitboard AllWhitePieces;
	Bitboard AllBlackPieces;
	Bitboard AllPieces;
	
	int materialAlb;
	int materalNegru;
	
	/*
	 0 0 0 0
	albRege, albRegina, negruRege, negruRegina
	se vede ca un numar de 4 biti
	daca bitul e setat inseamna ca se poate
	*/
	char castlePermision[] = {0,0,0,0};
	
	//istoric de miscari, tin sho minte 10 mutari plm
	MoveHistory istoric[] = new MoveHistory[10];
}

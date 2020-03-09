package com.company.Board;

import com.company.Printer;

public class BoardHelpere {
	//rank = linie, file = coloana
	//intoarce index pentru tabla de 120
	public static int conversieRFla120(int rank, int file)
	{
		return 21 + file + rank * 10;
	}
	//initializeaza cele 2 array-uri ca sa pot sa fac usor
	//conversia intre cele 2 reprezentari.
	public static int conversie120la64(int array120[], int index)
	{
		return array120[index];
	}
	
	public static void initializareArray(int array64[], int array120[]) {
		for (int i = 0; i < 120; i++) {
			array120[i] = 65;
		}
		for (int i = 0; i < 64; i++) {
			array64[i] = 120;
		}
		
		//merge de la 0 la 63 si imi seteaza indecsi
		int index = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int numarSecventa = conversieRFla120(i,j);
				array64[index] = numarSecventa;
				array120[numarSecventa] = index;
				index++;
			}
		}
	}

	public static void createBitboardFromFEN (String fen) {
		char currentChar = 0;
		int index = 0;
		for (int i = 0; i < fen.length(); i++) {
			currentChar = fen.charAt(i);

			switch (currentChar) {
				case 'N':
					setBitAtPos(BoardState.getInstance().WhiteKnights, index);
					setBitAtPos(BoardState.getInstance().AllWhitePieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'R':
					setBitAtPos(BoardState.getInstance().WhiteRooks, index);
					setBitAtPos(BoardState.getInstance().AllWhitePieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'B':
					setBitAtPos(BoardState.getInstance().WhiteBishops, index);
					setBitAtPos(BoardState.getInstance().AllWhitePieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'P':
					setBitAtPos(BoardState.getInstance().WhitePawns, index);
					setBitAtPos(BoardState.getInstance().AllWhitePieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'Q':
					setBitAtPos(BoardState.getInstance().WhiteQueens, index);
					setBitAtPos(BoardState.getInstance().AllWhitePieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'K':
					setBitAtPos(BoardState.getInstance().WhiteKing, index);
					setBitAtPos(BoardState.getInstance().AllWhitePieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;

				case 'n':
					setBitAtPos(BoardState.getInstance().BlackKnights, index);
					setBitAtPos(BoardState.getInstance().AllBlackPieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'r':
					setBitAtPos(BoardState.getInstance().BlackRooks, index);
					setBitAtPos(BoardState.getInstance().AllBlackPieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'b':
					setBitAtPos(BoardState.getInstance().BlackBishops, index);
					setBitAtPos(BoardState.getInstance().AllBlackPieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'p':
					setBitAtPos(BoardState.getInstance().BlackPawns, index);
					setBitAtPos(BoardState.getInstance().AllBlackPieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'q':
					setBitAtPos(BoardState.getInstance().BlackQueens, index);
					setBitAtPos(BoardState.getInstance().AllBlackPieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case 'k':
					setBitAtPos(BoardState.getInstance().BlackKing, index);
					setBitAtPos(BoardState.getInstance().AllBlackPieces, index);
					setBitAtPos(BoardState.getInstance().AllPieces, index);
					index++;
					break;
				case '/':
					//index++;
					break;
				default:
					index += (currentChar - '0');
					break;
			}
		}
	}

	public static void setBitAtPos (Bitboard board, int pos) {
		board.reprezentare |= ((long)1 << pos);
		board.numarPiese++;
	}

}

package com.company;

import com.company.Board.Bitboard;

public class Printer {
    public static void print(String fenNotation) {
        // pracurg stringu
        for (int i = 0; i < fenNotation.length(); i++) {
            char currentChar = fenNotation.charAt(i);
            // daca trb sa newline
            if (currentChar == '/') {
                System.out.println();
                continue;
            }
            // daca e cifra
            if (currentChar <= '9') {
                // printam de atatea ori punct
                for (int j = currentChar - 48; j > 0; j--) {
                    System.out.print(". ");
                }
                continue;
            }
            // daca nu e niciuna, inseamna ca e litera deci nu-i facem nik
            System.out.print(currentChar + " ");
        }
    }

    public static void print() {
    	/*long shifter = 1;
    	shifter <<= 63;
    	
	    for (int i = 0; i < 64; i++) {
		    if ((b.reprezentare & shifter) != 0) {
			    System.out.print("# ");
		    } else {
			    System.out.print(". ");
		    }

		    if ((i+1) % 8 == 0 && i != 0) {
			    System.out.println();
		    }
		    shifter >>>= 1;
	    }
	    System.out.println();*/

    	com.company.Board.BoardState board = com.company.Board.BoardState.getInstance();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                int index = i * 8 + j;
                if (board.WhiteBishops.isOccupied(index)) {
                    System.out.print("B");
                    continue;
                }
                if (board.WhiteKnights.isOccupied(index)) {
                    System.out.print("N");
                    continue;
                }
                if (board.WhitePawns.isOccupied(index)) {
                    System.out.print("P");
                    continue;
                }
                if (board.WhiteKing.isOccupied(index)) {
                    System.out.print("K");
                    continue;
                }
                if (board.WhiteQueens.isOccupied(index)) {
                    System.out.print("Q");
                    continue;
                }
                if (board.WhiteRooks.isOccupied(index)) {
                    System.out.print("R");
                    continue;
                }
                if (board.BlackBishops.isOccupied(index)) {
                    System.out.print("b");
                    continue;
                }
                if (board.BlackKnights.isOccupied(index)) {
                    System.out.print("n");
                    continue;
                }
                if (board.BlackPawns.isOccupied(index)) {
                    System.out.print("p");
                    continue;
                }
                if (board.BlackKing.isOccupied(index)) {
                    System.out.print("k");
                    continue;
                }
                if (board.BlackQueens.isOccupied(index)) {
                    System.out.print("q");
                    continue;
                }
                if (board.BlackRooks.isOccupied(index)) {
                    System.out.print("r");
                    continue;
                }
                System.out.print(".");
            }
            System.out.println();
        }


    	/*com.company.Board.BoardState board = com.company.Board.BoardState.getInstance();

    	for (int i = 0; i < 64; i++) {
    	    if (board.WhiteBishops.isOccupied(i)) {
    	        System.out.print("B");
            }
        }*/
    }
}

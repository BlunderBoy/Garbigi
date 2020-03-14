package com.company;


import com.company.Board.*;

/**
 * Clasa asta practically tine aproape toate constantele si variabilele de stare ale noastre.
 * Pe langa asta, are si functii ajutatoare, de ex chestii legate de indecsii 64-120
 */

public class Database {
	private Database() {} //singleton

	static class SingletonHolder {
		public static final Database INSTANTA_SINGLETON = new Database();
	}

	// #defines practically
	public final boolean DEBUG = true;
	public final boolean BLACK = false;
	public final boolean WHITE = true;

	public int numarDeMiscariFacute = 0;
	public int halfMoves = 0;
	public int fullMoves = 0;

	//TODO : ce plm e force mode fa?
	// e in pzdm force mode al xboardului. citeste documentatia poate??
	// sa nu fim pasivi agresivi se poate? zic doar. punct.
	// sugi pula
	public boolean forceMode = false;
    public boolean engineColor = BLACK;
    public boolean turn = WHITE; // al cui e randul
    public boolean opponentColor = WHITE;


	public static Database getInstance() {
		return SingletonHolder.INSTANTA_SINGLETON;
	}

	public void initGame() {
		BoardHelpere.initializareArray();
		BoardHelpere.initializareValoarePiese();
		BoardHelpere.createBoardstateFromFEN("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
		engineColor = BLACK;
		opponentColor = WHITE;
		turn = WHITE;
		forceMode = false;
	//rank = linie, file = coloana
	//intoarce index pentru tabla de 120
	public static int conversieRFla120(int rank, int file) {
		return 21 + file + rank * 10;
	}
	//initializeaza cele 2 array-uri ca sa pot sa fac usor
	//conversia intre cele 2 reprezentari.
	public static int conversie120la64(int array120[], int index) {
		return array120[index];
	}

	static int array64[];
	static int array120[];

	public static void initializareArray() {
		array64 = new int[64];
		array120 = new int[120];
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
}

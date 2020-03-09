package com.company;


import com.company.Board.BoardHelpere;

public class DatabaseComenziSiConstante {
	// # defines practically
	final boolean DEBUG = true;
	final boolean BLACK = false;
	final boolean WHITE = true;
	final int numarDeMiscariFacute = 0;

	//TODO : ce plm e force mode fa?
	// e in pzdm force mode al xboardului. citeste documentatia poate??
	// sa nu fim pasivi agresivi se poate? zic doar. punct.
	boolean forceMode = false;
    boolean engineColor = BLACK;
    boolean turn = WHITE; // al cui e randul
    boolean opponentColor = WHITE;

	private DatabaseComenziSiConstante() {} //singleton
	
	static class SingletonHolder {
		public static final DatabaseComenziSiConstante INSTANTA_SINGLETON = new DatabaseComenziSiConstante();
	}

	public static DatabaseComenziSiConstante getInstance() {
		return SingletonHolder.INSTANTA_SINGLETON;
	}

	public void initGame() {
		BoardHelpere.createBitboardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		engineColor = BLACK;
		opponentColor = WHITE;
		turn = WHITE;
		forceMode = false;
	}

}

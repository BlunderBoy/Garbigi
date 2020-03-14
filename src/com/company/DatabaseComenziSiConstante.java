package com.company;


import com.company.Board.*;

public class DatabaseComenziSiConstante {
	// # defines practically
	public final boolean DEBUG = false;
	public final boolean BLACK = false;
	public final boolean WHITE = true;
	public int numarDeMiscariFacute = 0;
	public int halfMoves = 0;
	public int fullMoves = 0;
	
	//TODO : ce plm e force mode fa?
	// e in pzdm force mode al xboardului. citeste documentatia poate??
	// sa nu fim pasivi agresivi se poate? zic doar. punct.
	// sugi pula
	// mnu tu sugi
	public boolean forceMode = false;
    public boolean engineColor = BLACK;
    public boolean turn = WHITE; // al cui e randul
    public boolean opponentColor = WHITE;

	private DatabaseComenziSiConstante() {} //singleton
	
	static class SingletonHolder {
		public static final DatabaseComenziSiConstante INSTANTA_SINGLETON = new DatabaseComenziSiConstante();
	}

	public static DatabaseComenziSiConstante getInstance() {
		return SingletonHolder.INSTANTA_SINGLETON;
	}

	public void initGame() {
		BoardHelpere.initializareArray();
		BoardHelpere.initializareValoarePiese();
		BoardHelpere.createBoardstateFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		engineColor = BLACK;
		opponentColor = WHITE;
		turn = WHITE;
		forceMode = false;
	}
}

package com.company;


import com.company.Board.*;

public class Database {
	// # defines practically
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

	private Database() {} //singleton
	
	static class SingletonHolder {
		public static final Database INSTANTA_SINGLETON = new Database();
	}

	public static Database getInstance() {
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

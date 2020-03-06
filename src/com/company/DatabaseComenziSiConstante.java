package com.company;


public class DatabaseComenziSiConstante {
	// # defines practically
	final boolean DEBUG = true;
	final boolean BLACK = false;
	final boolean WHITE = true;
	final int numarDeMiscariFacute = 0;

	//TODO : ce plm e force mode fa?
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
}

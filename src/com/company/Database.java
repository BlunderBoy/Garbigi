package com.company;
import com.company.Board.*;

/**
 * Clasa asta practically tine aproape toate constantele si variabilele de stare ale noastre.
 * Pe langa asta, are si functii ajutatoare, de ex chestii legate de indecsii 64-120
 */

//TODO sa schimb array64 si array120
public class Database {
	private Database() {} //singleton

	static class SingletonHolder {
		public static final Database INSTANTA_SINGLETON = new Database();
	}

	// #defines practically
	public final boolean DEBUG = false;
	public final boolean BLACK = false;
	public final boolean WHITE = true;

	public int numarDeMiscariFacute = 0;
	public int halfMoves = 0;
	public int fullMoves = 0;

	public boolean forceMode = false;
    public boolean engineColor = BLACK;
    public boolean turn = WHITE; // al cui e randul
    public boolean opponentColor = WHITE;


	public static Database getInstance() {
		return SingletonHolder.INSTANTA_SINGLETON;
	}

	//rank = linie, file = coloana
	//intoarce index pentru tabla de 120
	public static int conversieRFla120(int rank, int file) {
		return (21 + file + rank * 10);
	}
	//initializeaza cele 2 array-uri ca sa pot sa fac usor
	//conversia intre cele 2 reprezentari.
	public static int conversie120la64(int index) {
		return array120[index];
	}
	public static int conversie64la120(int index) { return array64[index]; }

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
		int index = 63;
		for (int i = 7; i >= 0; i--) {
			for (int j = 7; j >= 0; j--) {
				int numarSecventa = conversieRFla120(i,j);
				array64[index] = numarSecventa;
				array120[numarSecventa] = index;
				index--;
			}
		}
//		System.out.println("array64:");
//		for (int i = 0; i < 64; i++)
//		{
//			System.out.print(array64[i] + " ");
//			if((i+1) % 8 == 0)
//			{
//				System.out.println();
//			}
//		}
//		System.out.println("array120:");
//		for (int i = 0; i < 120; i++)
//		{
//			System.out.print(array120[i] + " ");
//			if((i+1) % 10 == 0)
//			{
//				System.out.println();
//			}
//		}
	}
}

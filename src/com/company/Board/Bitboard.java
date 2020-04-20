package com.company.Board;

/**
 * Clasa asta tine efectiv obiectul de bitboard si functii care le prelucreaza
 */

public class Bitboard implements Cloneable{
	public long reprezentare;
	public int numarPiese;
	public int valoare;
	public static long setMask[];
	public static long clearMask[];

	//using LITERAL FUCKING MAGIC dam pop la primul bit si ii aflam pozitia in O(FUCKING 1)
    //nu chiar magic, folosim un magic number dupa ideea lui De Brujin de pop lsb
    final static int[] BitTable = {
            63, 30, 3, 32, 25, 41, 22, 33, 15, 50, 42, 13, 11, 53, 19, 34, 61, 29, 2,
            51, 21, 43, 45, 10, 18, 47, 1, 54, 9, 57, 0, 35, 62, 31, 40, 4, 49, 5, 52,
            26, 60, 6, 23, 44, 46, 27, 56, 16, 7, 39, 48, 24, 59, 14, 12, 55, 38, 28,
            58, 20, 37, 17, 36, 8};
	
	public static void initMasti() {
		setMask = new long[64];
		clearMask = new long[64];
		long shifter = 1;

		for (int i = 0; i < 64; i++) {
			setMask[i] = shifter;
			clearMask[i] = ~setMask[i];
			shifter <<= 1;
		}
	}

	public Bitboard() {
		reprezentare = 0;
		numarPiese = 0;
		valoare = 0;
	}

	public Bitboard(long reprezentare, int numarPiese, int valoare) {
		this.reprezentare = reprezentare;
		this.numarPiese = numarPiese;
		this.valoare = valoare;
	}

	@Override
	public Bitboard clone() throws CloneNotSupportedException {
		return (Bitboard) super.clone();
	}

	/**
	 * Checks if the "position" is occupied in the respective bitboard.
	 * @param pos Position to check.
	 * @return True if occupied, false if not.
	 */
	public boolean isBitSet(int pos) { try {return (reprezentare & (setMask[pos])) != 0;}
	catch (Exception e)
	{ return true;} }

	public void setBit(int pos) {
		reprezentare = reprezentare | setMask[pos];
	}

	public void clearBit(int pos) {
		reprezentare = reprezentare & clearMask[pos];
	}
	
	static public long setBit(int pos, long numar) {
		return numar | setMask[pos];
	}

	static public long clearBit(int pos, long numar) {
		return numar & clearMask[pos];
	}
	
	public static boolean isBitSet(int pos, long bitboard) { return (bitboard & (setMask[pos])) != 0; }

	public static int popLSB(long bitboard) {
        long b = bitboard ^ (bitboard - 1);
        int fold = (int) ((b) ^ (b >>> 32));
        return BitTable[(int) ((fold * 0x783a9b23) >>> 26)];
    }

	public void resetBitboard() {
		reprezentare = 0;
		numarPiese = 0;
	}
}

package com.company.Board;

public class Bitboard {
	public long reprezentare;
	public int numarPiese;
	public int valoare;
	public static long setMask[];
	public static long clearMask[];
	
	public static void initMasti()
	{
		setMask = new long[64];
		clearMask = new long[64];
		long shifter = 1;
		for (int i = 0; i < 64; i++)
		{
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

	/**
	 * Checks if the "position" is occupied in the respective bitboard.
	 * @param pos Position to check.
	 * @return True if occupied, false if not.
	 */
	public boolean isBitSet(int pos) {
		return (reprezentare & (setMask[pos])) != 0;
	}

	public void setBit(int pos) {
		reprezentare = reprezentare | setMask[pos];
	}

	public void clearBit(int pos) {
		reprezentare = reprezentare & clearMask[pos];
	}

	public void resetBitboard() {
		reprezentare = 0;
		numarPiese = 0;
	}
}

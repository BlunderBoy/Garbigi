package com.company.Board;

public class Bitboard {
	public long reprezentare;
	public int numarPiese;

	public Bitboard () {
		reprezentare = 0;
		numarPiese = 0;
	}

	/**
	 * Checks if the "position" is occupied in the respective bitboard.
	 * @param pos Position to check.
	 * @return True if occupied, false if not.
	 */
	public boolean isOccupied(int pos) {
		long shifter = 1;
		return (reprezentare & (shifter << pos)) != 0;
	}
}

package com.company.MoveAndSearch;

/*
mutare:
	0000 0000 0000 0000 0111 1111 sursa
	0000 0000 0011 1111 1000 0000 destinatie
	0000 0011 1100 0000 0000 0000 capturare
	0011 1100 0000 0000 0000 0000 promotie
	0100 0000 0000 0000 0000 0000 castle
	1000 0000 0000 0000 0000 0000 en passant
	in total 6 nibbles pentru a codifica o mutare
 */
public class Move
{
	int mutare;
	int scor;
	
	String[] helpere = new String[]{
			"h1", "g1", "f1", "e1", "d1", "c1", "b1", "a1",
			"h2", "g2", "f2", "e2", "d2", "c2", "b2", "a2",
			"h3", "g3", "f3", "e3", "d3", "c3", "b3", "a3",
			"h4", "g4", "f4", "e4", "d4", "c4", "b4", "a4",
			"h5", "g5", "f5", "e5", "d5", "c5", "b5", "a5",
			"h6", "g6", "f6", "e6", "d6", "c6", "b6", "a6",
			"h7", "g7", "f7", "e7", "d7", "c7", "b7", "a7",
			"h8", "g8", "f8", "e8", "d8", "c8", "b8", "a8"};
	
	
	int getSursa()
	{
		return (mutare & 0x7F);
	}
	
	int getDestinatie()
	{
		//magic calculat cu python
		//mutare >> 7 & 7F
		return mutare & 0x3F80;
	}
	
	int getCapturare()
	{
		//magic calculat cu python
		//mutare >> 14 & F
		return mutare & 0x3c000;
	}
	
	int getPromotie()
	{
		//magic calculat cu python
		//mutare >> 18 & F
		return mutare & 0x3c0000;
	}
	
	int getCastle()
	{
		//magic calculat cu python
		//mutare >> 22 & 1
		return mutare & 0x400000;
	}
	
	int getEnPassant()
	{
		//magic calculat cu python
		//mutare >> 23 & 1
		return mutare & 0x800000;
	}
	
	public void printMove()
	{
		System.out.println("valoare = " + mutare + " scor = " + scor);
		System.out.println("sursa = " + getSursa() + " adica " + helpere[getSursa()]);
		System.out.println("destinatie = " + getDestinatie() +  " adica " + helpere[getDestinatie()]);
		System.out.println("capturare = " + getCapturare());
		System.out.println("promotie = " + getPromotie());
		System.out.println("castle = " + getCastle());
		System.out.println("enPassant = " + getEnPassant());
	}
	
	//de la 0 la 64 la RF
	public String getMove()
	{
		return (helpere[getSursa()] + helpere[getDestinatie()]);
	}
}

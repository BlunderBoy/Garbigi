package com.company.MoveAndSearch;

/*
mutare:
	0000 0000 0011 1111 sursa (2^6 64 de valori, de la 0 la 63)
	0000 1111 1100 0000 destinatie (2^6 64 de valori, de la 0 la 63)
	0011 0000 0000 0000 promotie (4 valori - cal 0 (00), nebun 01 (01), tura (10), regina (11))
	1100 0000 0000 0000 flag (4 valori - promotie 1 (01), en passant 2 (10), castling 3 (11))
	in total 4 nibbles pentru a codifica o mutare
	en passant se seteaza cand se poate captura un pion en passant
	Stockfish ty ty
 */
public class Move implements Comparable<Move>{
    //codificare
	int sursa;
	int destinatie;
	int promotie; //(4 valori - cal 0 (00), nebun 01 (01), tura (10), regina (11)) -1 pentru noncap
	int flag; ////(4 valori - promotie 1 (01), en passant 2 (10), castling 3 (11))
	int piesa; //piesa care se muta
	int piesaDestinatie;
	//
    double scor;
    int prioritate;
    // 0 -> pion
    // 1 -> cal
    // 2 -> nebun
    // 3 -> tura
    // 4 -> regina
    // 5 -> rege
    // plz respect these


    @Override
    public String toString () {
        return "{" + "sursa: " + sursa +
                ", destinatie: " + destinatie +
                ", promotie: " + promotie +
                ", flag: " + flag +
                ", piesa: " + piesa +
                ", piesaDestinatie: " + piesaDestinatie +
                ", scor: " + scor +
                '}';
    }

    public Move () { }

    public Move (int sursa, int destinatie, int promotie, int flag, int piesa, int piesaDestinatie, int scor) {
        this.sursa = sursa;
        this.destinatie = destinatie;
        this.promotie = promotie;
        this.flag = flag;
        this.piesa = piesa;
        this.piesaDestinatie = piesaDestinatie;
        this.scor = scor;
    }

    final String[] helpere = new String[]{
            "h1", "g1", "f1", "e1", "d1", "c1", "b1", "a1",
            "h2", "g2", "f2", "e2", "d2", "c2", "b2", "a2",
            "h3", "g3", "f3", "e3", "d3", "c3", "b3", "a3",
            "h4", "g4", "f4", "e4", "d4", "c4", "b4", "a4",
            "h5", "g5", "f5", "e5", "d5", "c5", "b5", "a5",
            "h6", "g6", "f6", "e6", "d6", "c6", "b6", "a6",
            "h7", "g7", "f7", "e7", "d7", "c7", "b7", "a7",
            "h8", "g8", "f8", "e8", "d8", "c8", "b8", "a8"};


    public int getSursa () {
        return sursa;
    }

    public int getDestinatie () {
        return destinatie;
    }

    public int getPromotie () {
        return promotie;
    }

    public int getFlag () {
        return flag;
    }
    
    public int getPiesa(){
    	return piesa;
    }
    
    public int getPiesaDestinatie(){
    	return piesa;
    }

    public void printMove () {
        System.out.println("-----------------");
//        System.out.println(Integer.toBinaryString(mutare));
//        System.out.println("valoare = " + mutare + " scor = " + scor);
//        System.out.println("sursa = " + getSursa() + " adica " + helpere[getSursa()]);
//        System.out.println("destinatie = " + getDestinatie() + " adica " + helpere[getDestinatie()]);
//        System.out.println("flag = " + getFlag());
	
	    System.out.println("scor " + scor  + " prio: " + prioritate + " " + helpere[getSursa()] + " " + helpere[getDestinatie()]);

        if (getFlag() == 1) {
            System.out.print("promotie la : ");
            if (getPromotie() == 1) {
                System.out.println("cal");
            }
            if (getPromotie() == 2) {
                System.out.println("nebun");
            }
            if (getPromotie() == 3) {
                System.out.println("tura");
            }
            if (getPromotie() == 4) {
                System.out.println("regina");
            }
        }

        if (getFlag() == 2) {
            System.out.println("s-a capturat un pion en passant");
        }

        if (getFlag() == 3) {
            System.out.println("castles");
        }

        System.out.println("-----------------");
    }

    //de la 0 la 64 la RF
    public String getMove () {
        return (helpere[getSursa()] + helpere[getDestinatie()]);
    }
	
	
	@Override
	public int compareTo(Move move)
	{
		return move.prioritate - this.prioritate;
	}
}

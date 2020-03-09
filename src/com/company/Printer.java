package com.company;

public class Printer {
    public static void print(String fenNotation) {
        // pracurg stringu
        for (int i = 0; i < fenNotation.length(); i++) {
            char currentChar = fenNotation.charAt(i);
            // daca trb sa newline
            if (currentChar == '/') {
                System.out.println();
                continue;
            }
            // daca e cifra
            if (currentChar <= '9') {
                // printam de atatea ori punct
                for (int j = currentChar - 48; j > 0; j--) {
                    System.out.print(". ");
                }
                continue;
            }
            // daca nu e niciuna, inseamna ca e litera deci nu-i facem nik
            System.out.print(currentChar + " ");
        }
    }
    public static void print(long bitBoard)
    {
    	long shifter = 1;
    	shifter <<= 63;
    	
	    for (int i = 0; i < 64; i++)
	    {
		    if((bitBoard & shifter) != 0)
		    {
			    System.out.print("# ");
		    }
		    else
		    {
			    System.out.print(". ");
		    }
		    if((i+1) % 8 == 0 && i != 0)
		    {
			    System.out.println();
		    }
		    shifter >>>= 1;
	    }
	    System.out.println();
    }
}

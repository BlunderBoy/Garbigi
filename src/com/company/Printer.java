package com.company;

public class Printer {
    public static void print(String fen) {
        // pracurg stringu
        for (int i = 0; i < fen.length(); i++) {
            char ch = fen.charAt(i);
            // daca trb sa newline
            if (ch == '/') {
                System.out.println();
                continue;
            }
            // daca e cifra
            if (ch <= '9') {
                // printam de atatea ori punct
                for (int j = ch - 48; j > 0; j--) {
                    System.out.print(". ");
                }
                continue;
            }
            // daca nu e niciuna, inseamna ca e litera deci nu-i facem nik
            System.out.print(ch + " ");
        }
    }
}

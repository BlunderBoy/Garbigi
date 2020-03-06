package com.company;

import java.util.Scanner;

// TODO: xboard
// TODO: new
// TODO: force - for the moment da
// TODO: go - I guess?
// TODO: white - I guess?
// TODO: black - I guess?
// TODO: quit - atm da
// TODO: resign
// TODO: move

public class Main {
	enum CONST{
		A, B,C
}

    public static void main(String[] args)  {
        // test pt printer
        if (DatabaseComenziSiConstante.getInstance().DEBUG) {
            Printer.print("rnbqk2r/ppp1pp1p/6b1/2n3p1/1P6/8/PP4pP/RN5R");
        }

    	Scanner in = new Scanner(System.in);
        XBoardProtocol xBoardProtocol = new XBoardProtocol();

        while (true) {
            String buffer = in.nextLine();
            if(xBoardProtocol.parseInput(buffer) != 0)
            {
            	break;
            }
        }
    }
}

package com.company;

import java.util.Scanner;

/**
 * new
 * go
 * force
 * INPUT MOVES
 * level
 * time
 * protover
 * setboard
 * quit
 */

public class Main {

    public static void main(String[] args)  {
        
        XBoardProtocol XBoardProtocol = new XBoardProtocol();
        XBoardProtocol.printOptiuniInitiale();

        while (true) {
            Scanner in = new Scanner(System.in);
            String buffer = in.nextLine();
            if(XBoardProtocol.parseInput(buffer) != 0)
            {
            	break;
            }
        }
    }
}

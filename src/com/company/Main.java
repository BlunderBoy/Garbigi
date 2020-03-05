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
     
    	Scanner in = new Scanner(System.in);
        XBoardProtocol XBoardProtocol = new XBoardProtocol();

        while (true) {
            String buffer = in.nextLine();
            if(XBoardProtocol.parseInput(buffer) != 0)
            {
            	break;
            }
        }
    }
}

package com.company;
import java.util.Scanner;

// TODO: force - for the moment da
// TODO: go - I guess?

// TODO: white - I guess? sigur trebuie tratat
// TODO: black - I guess? sigur trebuie tratat
// TODO: sa facem sa meraga din ambele parti miscari hardcodate si sa dea resign dupa ce le face!!!!!
// TODO: ce trebuie sa facem exact la etapa 1.
// TODO this hashing function https://youtu.be/WqVwQBXLwE0
// TODO: setup cu fen, in features!!!!!!!!

public class Main {
    public static void main(String[] args)  {

    	Scanner in = new Scanner(System.in);
        XBoardProtocol xBoardProtocol = new XBoardProtocol();

        while (true) {
            String buffer = in.nextLine();
            if(xBoardProtocol.parseInput(buffer) != 0) {
            	break;
            }
        }
    }
}

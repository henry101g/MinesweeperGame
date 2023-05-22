package cs1302.game;

import java.util.Scanner;
import java.util.*;

/**
 * A driver program for Minesweeper Game.
 */
public class MinesweeperDriver {
    /**
     * Entry point for the application. Exactly one command-line argument is expected.
     * @param args is the command-line argument used as a seed file for the game.
     */

    public static void main(String[] args) { //Begin MAIN
        Scanner stdIn = new Scanner(System.in);
        if (args.length != 1) {
            System.out.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        }
        String seedFile = args[0];
        MinesweeperGame msg = new MinesweeperGame(stdIn, seedFile);
        msg.printWelcome();
        msg.play();
    } //End MAIN
}

package cs1302.game;

import java.util.Scanner;
import java.util.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;

/**
 *class MinesweeperGame. All the methods that need to be ran to execute the game.
 */
public class MinesweeperGame {
  /**
   *rows - The number of rows.
   *columns - The number of columns.
   *numMines - The number of mines in the game.
   *count - The number of rounds in the game.
   *x - The x coordinate on the matrix.
   *y - The y cooridinate on the matrix.
   *mineField - The board/ field that the game is played on.
   *command - The command provided by the user.
   *Scanner stdIn decalring and an instance constant.
   */
    int rows, columns, numMines, count;
    //Naming convension for mineField values:
    //'m' - mine; 'F' - mark on top of mine; 'f' - mark on top of nothing(wrong);
    //'?' - guess on top of mine; 'g' - guess on top of nothing(wrong)
    char [][] mineField;
    private final Scanner stdIn;
    int x, y; //coordinates
    String command; //command provided by user

    /**
     * Constructor function creates object.
     * @param seedPath - A String representing a seed file.
     * @param stdIn - A scanner used throughout the program.
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) {

        ArrayList<Integer> input = new ArrayList<Integer>();
        this.stdIn = stdIn;
        count = 0;
        this.readSeedFile(seedPath, input);
        mineField = new char[rows][columns];
        for (int i = 0; i < numMines * 2; i += 2) {
            mineField[input.get(i)][input.get(i + 1)] = 'm';
        }
    }

    /**
     * This method prints the first welcome screen that the user sees.
     * It uses a try and catch on the file given to insure the file exists.
     * @throws FileNotFoundException.
     */
    public void printWelcome() {
        try {
            File file = new File("resources/welcome.txt");
            Scanner scan = new Scanner(file);
            String line;
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    /**
     * This method reads in the seed file that the user enters, and then puts the information
     * into an arrayList.
     * @param seedPath - represents the seed file for the game.
     * @param input - is an arrayList that is populated with all mines.
     * @throws FileNotFoundException.
     */
    public void readSeedFile(String seedPath, ArrayList<Integer> input) {
        int counter = 0;
        try {
            File seedFile = new File(seedPath);
            Scanner scan = new Scanner(seedFile);
            String line;
            if (scan.hasNextLine()) {
                rows = scan.nextInt();
            }
            if (scan.hasNextLine()) {
                columns = scan.nextInt();
            }
            if (scan.hasNextLine()) {
                numMines = scan.nextInt();
            }
            while (scan.hasNextLine() && counter < numMines * 2) {
                input.add(scan.nextInt());
                counter++;
            }
            System.out.println();
        } catch (FileNotFoundException e) {
            System.err.println("Seed File Not Found Error " + e);
            System.exit(2);
        } catch (NumberFormatException e) {
            System.err.println("Seed File Malformed Error: " + e);
            System.exit(3);
        }
    }

    /**
     * Method that prints the current contents of the minefield to standard output.
     */
    public void printMineField() {
        int i;
        System.out.println();
        System.out.println("  Rounds Completed: " + count);
        System.out.println();
        for (i = 0; i < rows; i++) {
            System.out.print(" " + i + " ");
            for (int j = 0; j < columns; j++) {
                if (mineField[i][j] == 'f' || mineField[i][j] == 'F') {
                    System.out.print("| F ");
                } else if (mineField[i][j] == '?' || mineField[i][j] == 'g') {
                    System.out.print("| ? ");
                } else if (mineField[i][j] >= '0' && mineField[i][j] <= '8') {
                    System.out.print("| " + mineField[i][j] + " ");
                } else {
                    System.out.print("|   ");
                }
                if (j == columns - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
        System.out.print("    ");
        for (i = 0; i < columns; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * This method prompts the user of what they want to do in the game.
     */
    public void promptUser() {
        printMineField();
        System.out.print("minesweeper-alpha: ");
    }

    /**
     * This method evalutaes the user input and returns a boolean depending on the input recieved.
     * @param commandLine - represents full command provided by user.
     * @return returns a boolean based on input validity.
     * @throws NumberFormatException.
     */
    public boolean evaluateInput(String commandLine) {
        String[] tokens = commandLine.split(" ");
        if (tokens.length > 3 || tokens.length == 2) {
            System.out.println("Invalid Command: Command not recognized!");
            return false;
        }
        if (tokens[0].equals("r") || tokens[0].equals("m") || tokens[0].equals("g") ||
            tokens[0].equals("reveal") || tokens[0].equals("mark") || tokens[0].equals("guess")) {
            command = tokens[0];
        } else if (tokens[0].equals("help") || tokens[0].equals("quit") ||
            tokens[0].equals("q") || tokens[0].equals("h") || tokens[0].equals("nofog")) {
            command = tokens[0];
            return true;
        } else {
            System.out.println("Invalid Command: Command not recognized!");
            return false;
        }
        try {
            x = Integer.parseInt(tokens[1]);
            if (x >= rows || x < 0) {
                System.out.println("Invalid Command: Command not recognized!");
                return false;
            }
            y = Integer.parseInt(tokens[2]);
            if (y >= columns || y < 0) {
                System.out.println("Invalid Command: Command not recognized!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println(e);
            return false;
        }
        return true;
    }

    /**
     * This method returns a boolean depeding on whether the game was won.
     * @return returns a boolean whether the game was won or not.
     */
    public boolean isWon() {
        int i, j;
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                if (mineField[i][j] != 'F' && (mineField[i][j] < '0' || mineField[i][j] > '8')) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Prints the winning message to the user when the game is won.
     * @throws FileNotFoundException.
     */
    public void printWin() {
        double score = 100.0 * rows * columns / count;
        try {
            System.out.println();
            File file = new File("resources/gamewon.txt");
            Scanner scan = new Scanner(file);
            String line;
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                if (line.indexOf("SCORE:") >= 0) {
                    System.out.println(line +  " " + score);
                } else {
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        System.out.println();
    }

    /**
     * Prints the losing message to the user when the game is lost.
     * @throws FileNotFoundException.
     */
    public void printLoss() {
        try {
            System.out.println();
            File file = new File("resources/gameover.txt");
            Scanner scan = new Scanner(file);
            String line;
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        System.out.println();
        System.exit(0);
    }
    /**
     * This method is called when the user wants to mark a cell. When called, the method
     * will mark the cell at the coorinates they wanted.
     */

    public void markCell() {
        if (mineField[x][y] == 'm') {
            mineField[x][y] = 'F';
        } else {
            mineField[x][y] = 'f';
        }
    }

    /**
     * This method is called when the user enters for help. When called, the method will print
     * out the help layout for the user.
     */
    public void presentHelp() {
        System.out.println("\nCommands Available...");
        System.out.println(" - Reveal: r/reveal row col");
        System.out.println(" - Mark: m/mark   row col");
        System.out.println(" - Guess: g/guess  row col");
        System.out.println(" - Help: h/help");
        System.out.println(" - Quit: q/quit");

    }

    /**
     * This method is called when the user wants to guess a cell. When called, the method will
     * mark a guess symbol at the coordinates the user entered.
     */

    public void guessCell() {
        if (mineField[x][y] == 'm') {
            mineField[x][y] = '?';
        } else {
            mineField[x][y] = 'g';
        }
    }

    /**
     * This method is used to reveals the surrounding cells at the cooridinates that the user gave.
     */
    public void revealCell() {
        int mineCounter = 0;
        if (mineField[x][y] == 'm' || mineField[x][y] == 'F') {
            printLoss();
        }
        //top left
        if (x > 0 && y > 0 && mineField[x - 1][y - 1] == 'm') {
            mineCounter++;
        }
        //above
        if (x > 0 && mineField[x - 1][y] == 'm') {
            mineCounter++;
        }
        //top right
        if (x > 0 && y < columns - 1 && mineField[x - 1][y + 1] == 'm') {
            mineCounter++;
        }
        //side left
        if (y > 0 && mineField[x][y - 1] == 'm') {
            mineCounter++;
        }
        //side right
        if (y < columns - 1 && mineField[x][y + 1] == 'm') {
            mineCounter++;
        }
        //bottom left
        if (x < rows - 1 && y > 0 && mineField[x + 1][y - 1] == 'm') {
            mineCounter++;
        }
        //under
        if (x < rows - 1 && mineField[x + 1][y] == 'm') {
            mineCounter++;
        }
        //bottom right
        if (x < rows - 1 && y < columns - 1  && mineField[x + 1][y + 1] == 'm') {
            mineCounter++;
        }
        mineField[x][y] = (char)(mineCounter + '0');
    }
    /**
     * nofog method is the "cheat code" for the user and gives them the hints
     * of where the mines are.
     */

    public void noFog() {

        int i;
        System.out.println();
        System.out.println("  Rounds Completed: " + count);
        System.out.println();
        for (i = 0; i < rows; i++) {
            System.out.print(" " + i + " ");
            for (int j = 0; j < columns; j++) {
                if (mineField[i][j] == 'F') {
                    System.out.print("|<F>");
                } else if (mineField[i][j] == 'f') {
                    System.out.print("| F ");
                } else if (mineField[i][j] == '?') {
                    System.out.print("|<?>");
                } else if (mineField[i][j] == 'g') {
                    System.out.print("| ? ");
                } else if (mineField[i][j] >= '0' && mineField[i][j] <= '8') {
                    System.out.print("| " + mineField[i][j] + " ");
                } else if (mineField[i][j] == 'm') {
                    System.out.print("|< >");
                } else {
                    System.out.print("|   ");
                }
                if (j == columns - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
        System.out.print("    ");
        for (i = 0; i < columns; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.println();
        System.out.println();
    }


    /**
     * This method provides the main game loop and call other methods to help.
     */
    public void play() {
        promptUser();
        while (true) {
            String commandLine = stdIn.nextLine();

            if (evaluateInput(commandLine)) {
                count++;
                if (command.equals("m") || command.equals("mark")) {
                    markCell();
                } else if (command.equals("h") || command.equals("help")) {
                    presentHelp();
                } else if (command.equals("g") || command.equals("guess")) {
                    guessCell();
                } else if (command.equals("r") || command.equals("reveal")) {
                    revealCell();
                } else if (command.equals("nofog")) {
                    noFog();
                    System.out.print("minesweeper-alpha: ");
                    continue;
                } else if (command.equals("q") || command.equals("quit")) {
                    System.out.print("\nQuitting the game...\nBye!\n");
                    System.exit(0);
                }
            }
            promptUser();
            if (isWon()) {
                printWin();
                System.exit(0);
            }
        }
    }
}

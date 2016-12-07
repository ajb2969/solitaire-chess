package model;
import backtracking.Backtracker;
import backtracking.Configuration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Observable;

/**
 * Created by alexbrown on 11/11/16.
 */
public class SoltrChessModel extends Observable implements Configuration {
    /*
        * The default size of the board.
        */
    private static final int BOARD_SIZE = 4;
    /*
     * The board.
     */
    private String [][]board;
    /*
     * The file that is being looked at.
     */
    private static String currFile;
    /*
     * Backtracker object.
     */
    private Backtracker obj = new Backtracker();
    /*
     * The previous assortment of the board.
     */
    private String [][] previousBoard = new String[BOARD_SIZE][BOARD_SIZE];
    /*
     * ArrayList that contains the solution.
     */
    private ArrayList<Configuration> solution;
    /*
     * The number of moves that are made.
     */
    private int numOfMoves;
    /*
     * Check to see if there has been a change made.
     */
    private boolean hasChanged = false;
    /*
     * Coordinates for the most recent move made.
     */
    private Coordinates mostRecentMove;
    /*
     * Boolean for solve to check if it was used.
     */
    private Boolean solveCalled = false;
    /*
     * Boolean for hint that checks if it was used.
     */
    private Boolean hintCalled = false;
    /*
     * Boolean that is changed if there is no solution available.
     */
    private Boolean solutionExisting = true;
    /*
     * Boolean that is changed once the first move has been made.
     */
    private Boolean firstMoveMade = false;

    /*
     * Makes the board base on the inputted file.
    */
    public SoltrChessModel(String fileName) {
        board = makeBoard(fileName);
        mostRecentMove = new Coordinates(0,0);
    }
    /*
     * A bunch of helper methods that return the boolean, coordinates, number of moves, the file, or the board.
     */
    public String[][] getBoard() {
        return board;
    }

    public boolean getSolutionExisting(){
        return solutionExisting;
    }

    public boolean getFirstMoveMade(){
        return firstMoveMade;
    }

    public boolean getHasChanged(){return hasChanged;}

    public int getRecentMoveRow(){
        return mostRecentMove.getX();
    }
    public int getRecentMoveCol(){
        return mostRecentMove.getY();
    }

    public int getNumOfMoves() {
        return numOfMoves;
    }

    public boolean getSolveCalled(){return solveCalled;}

    public boolean getHintCalled(){return hintCalled;}

    public void setNumOfMoves(int num) {
        numOfMoves = num;
    }

    public String getCurrFile() {
        return currFile;
    }

    /*
     * A hash map that is based on the coordinates and the type of chess piece.
     * @return - The piece on the board for their corresponding coordinates.
     */
    public HashMap<Coordinates, String> getCoordinates() {
        HashMap<Coordinates, String> piecesOnBoard = new HashMap<Coordinates, String>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!(board[i][j].equals("-"))) {
                    piecesOnBoard.put(new Coordinates(i, j), board[i][j]);//builds hashmap, puts each piece and it's coordinate in hashmap
                }
            }
        }
        return piecesOnBoard;
    }

    /*
    * Helper method that returns the number characters.
    */
    public int getNumChar(String[][] b) {
        int chr = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!(b[i][j].equals("-"))) {
                    chr++;
                }
            }
        }
        return chr;
    }

    /*
    * Creates a copy of the board.
    * @param copy - A copy of the model
    */
    public SoltrChessModel(SoltrChessModel copy) {
        board = new String[BOARD_SIZE][BOARD_SIZE];
        this.currFile = copy.currFile;
        for (int row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(copy.board[row], 0, this.board[row], 0, BOARD_SIZE);
            System.arraycopy(copy.board[row], 0, previousBoard[row], 0, BOARD_SIZE);
        }
    }

    /*
     * Makes the board based on the inputted file.
     * @param fileName - one of 5 games with different configurations for each.
     * @return The completed board with the correct configuration (the one specified in the file)
     */
    public String[][] makeBoard(String fileName) {
        board = new String[BOARD_SIZE][BOARD_SIZE];
        firstMoveMade = false;
        hintCalled = false;
        solveCalled = false;

        numOfMoves = 0;
        try {
            currFile = fileName;
            Scanner input = new Scanner(new File(fileName));
            for (int i = 0; i < BOARD_SIZE; i++) {
                String currLine = input.nextLine();
                String[] lineSplit = currLine.split("\\s+");
                for (int j = 0; j < lineSplit.length; j++) {
                    if (lineSplit[j].equals("-")) {
                        board[i][j] = "-";
                    } else if (lineSplit[j].equals("P")) {
                        board[i][j] = "P";
                    } else if (lineSplit[j].equals("B")) {
                        board[i][j] = "B";
                    } else if (lineSplit[j].equals("K")) {
                        board[i][j] = "K";
                    } else if (lineSplit[j].equals("N")) {
                        board[i][j] = "N";
                    } else if (lineSplit[j].equals("R")) {
                        board[i][j] = "R";
                    } else if (lineSplit[j].equals("Q")) {
                        board[i][j] = "Q";
                    } else {
                        System.out.println("File is Invalid");
                        System.exit(1);
                    }

                }

            }
            announce("Board Made");
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found");
            System.exit(1);
        }
        return board;
    }
    /*
     * Helper method that prints out the board.
     */
    public void printBoard() {
        System.out.println(toString());
    }
    /**
     * Method that evaluates all possible moves a piece can move on the board.
     * @param sR - The source row
     * @param sC - The source column
     * @param dR - The destination row
     * @param dC - The destination column
     */
    public void evaluateMove(int sR, int sC, int dR, int dC) {
        if (board[sR][sC].contains("-")) {
            System.out.println("Invalid source element");
        }
        if (board[dR][dC].contains("-")) {
            System.out.println("Invalid Destination element");
        }
        if (board[sR][sC].contains("B")) { //Bishop - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for (int i = 1; i < BOARD_SIZE; i++) {
                allMovePossibilities.add(new Coordinates(sR + i, sC + i));
                allMovePossibilities.add(new Coordinates(sR + i, sC - i));
                allMovePossibilities.add(new Coordinates(sR - i, sC + i));
                allMovePossibilities.add(new Coordinates(sR - i, sC - i));
            }
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (!((currObject.getX() < 0) || (currObject.getY() < 0) || (currObject.getX() >= BOARD_SIZE) || (currObject.getY() >= BOARD_SIZE))) {
                    validPossibilities.add(currObject);
                }
            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        } else if (board[sR][sC].contains("K")) { //King -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR + 1, sC));
            allMovePossibilities.add(new Coordinates(sR - 1, sC));
            allMovePossibilities.add(new Coordinates(sR, sC + 1));
            allMovePossibilities.add(new Coordinates(sR, sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 1, sC - 1));
            allMovePossibilities.add(new Coordinates(sR - 1, sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 1, sC + 1));
            allMovePossibilities.add(new Coordinates(sR - 1, sC + 1));

            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (!((currObject.getX() < 0) || (currObject.getY() < 0) || (currObject.getX() >= BOARD_SIZE) || (currObject.getY() >= BOARD_SIZE))) {
                    validPossibilities.add(currObject);
                }
            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);
        } else if (board[sR][sC].contains("N")) {//knight - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR + 2, sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 2, sC + 1));
            allMovePossibilities.add(new Coordinates(sR + 1, sC - 2));
            allMovePossibilities.add(new Coordinates(sR + 1, sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 2, sC + 1));
            allMovePossibilities.add(new Coordinates(sR - 2, sC - 1));
            allMovePossibilities.add(new Coordinates(sR - 1, sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 1, sC - 2));
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }

            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        } else if (board[sR][sC].contains("P")) {//Pawn - finished
            ArrayList<Coordinates> movePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            if (sR == 0) {
                movePossibilities.add(new Coordinates(sR + 1, sC + 1));
                movePossibilities.add(new Coordinates(sR + 1, sC - 1));
            } else {
                movePossibilities.add(new Coordinates(sR - 1, sC + 1));
                movePossibilities.add(new Coordinates(sR - 1, sC - 1));
            }

            for (int i = 0; i < movePossibilities.size(); i++) {
                Coordinates currObject = movePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }
            }

            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }

            announce(null);
        } else if (board[sR][sC].contains("R")) {//Rook -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for (int i = 1; i < BOARD_SIZE; i++) {
                allMovePossibilities.add(new Coordinates(sR, sC + i));
                allMovePossibilities.add(new Coordinates(sR, sC - i));
                allMovePossibilities.add(new Coordinates(sR + i, sC));
                allMovePossibilities.add(new Coordinates(sR - i, sC));
            }
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }

            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        } else { //contains Queen - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for (int i = 1; i < BOARD_SIZE; i++) {
                allMovePossibilities.add(new Coordinates(sR, sC + i));
                allMovePossibilities.add(new Coordinates(sR, sC - i));
                allMovePossibilities.add(new Coordinates(sR + i, sC));
                allMovePossibilities.add(new Coordinates(sR - i, sC));
                allMovePossibilities.add(new Coordinates(sR + i, sC + i));
                allMovePossibilities.add(new Coordinates(sR + i, sC - i));
                allMovePossibilities.add(new Coordinates(sR - i, sC + i));
                allMovePossibilities.add(new Coordinates(sR - i, sC - i));
            }
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }
            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }
    }

    /**
     * Evaluates the input of the user.
     * @param choice - The input the user chooses.
     */
    public void evaluate(String choice) {
        Scanner input = new Scanner(System.in);
        if (choice.equals("move")) {
            System.out.print("Enter the source row: ");
            int sourceRow = input.nextInt();
            System.out.print("Enter the source col: ");
            int sourceCol = input.nextInt();
            System.out.print("Enter the dest row: ");
            int destRow = input.nextInt();
            System.out.print("Enter the dest col: ");
            int destCol = input.nextInt();
            evaluateMove(sourceRow, sourceCol, destRow, destCol);
        } else if (choice.equals("new")) {
            System.out.print("Enter file name: ");
            String newFileName = input.nextLine();
            newFileName = "data/" + newFileName;
            makeBoard(newFileName);
            announce(null);
        } else if (choice.equals("restart")) {
            makeBoard(currFile);
            announce(null);
        } else if (choice.equals("hint"))//uses backtracking
        {
            List<Configuration> a = obj.solveWithPath(this);
            if (a != null) {
                a.remove(0);
                System.out.println("Hint:");
                System.out.println(a.get(0).toString());
                String[][] b = ((SoltrChessModel) a.get(0)).board;
                for (int i = 0; i < BOARD_SIZE; i++) {
                    System.arraycopy(b[i], 0, board[i], 0, BOARD_SIZE);
                }
                if (getNumChar(board) == 1) {
                    System.out.println("You've won, Congratulations");
                }
            } else {
                System.out.println("There is no hint");
            }
            announce("Hint");
        } else if (choice.equals("solve"))//uses backtracking
        {
            List<Configuration> a = obj.solveWithPath(this);
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    System.out.println("Step " + (i + 1) + ": ");
                    System.out.println(a.get(i).toString());
                }

                System.out.println("You won, Congratulations!");

            } else {
                System.out.println("There is no solution");
            }
        } else if (choice.equals("quit")) {
            System.out.println("The game has been quit");
            System.exit(0);
        } else {
            System.out.println("Invalid Input value");
            System.exit(1);
        }

    }

    public void announce(String arg) {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * Gets all possible configurations possible on the board.
     * @return The possible configurations of the board.
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        HashMap<Coordinates, String> piecesOnBoard = new HashMap<Coordinates, String>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!(board[i][j].equals("-"))) {
                    piecesOnBoard.put(new Coordinates(i, j), board[i][j]);//builds hashmap, puts each piece and it's coordinate in hashmap
                }
            }
        }
        for (Coordinates c : piecesOnBoard.keySet()) {
            if (piecesOnBoard.get(c).equals("B")) {
                ArrayList<Configuration> possibilities = getPossibleConfigsBishop(c.getX(), c.getY());
                for (int i = 0; i < possibilities.size(); i++) {
                    possibleConfigs.add(possibilities.get(i));
                }

            } else if (piecesOnBoard.get(c).equals("P")) {
                ArrayList<Configuration> possibilities = getPossibleConfigsPawn(c.getX(), c.getY());
                for (int i = 0; i < possibilities.size(); i++) {
                    possibleConfigs.add(possibilities.get(i));
                }
            } else if (piecesOnBoard.get(c).equals("N")) {
                ArrayList<Configuration> possibilities = getPossibleConfigsKnight(c.getX(), c.getY());
                for (int i = 0; i < possibilities.size(); i++) {
                    possibleConfigs.add(possibilities.get(i));
                }
            } else if (piecesOnBoard.get(c).equals("K")) {
                ArrayList<Configuration> possibilities = getPossibleConfigsKing(c.getX(), c.getY());
                for (int i = 0; i < possibilities.size(); i++) {
                    possibleConfigs.add(possibilities.get(i));
                }
            } else if (piecesOnBoard.get(c).equals("Q")) {
                ArrayList<Configuration> possibilities = getPossibleConfigsQueen(c.getX(), c.getY());
                for (int i = 0; i < possibilities.size(); i++) {
                    possibleConfigs.add(possibilities.get(i));
                }
            } else {//Rook
                ArrayList<Configuration> possibilities = getPossibleConfigsRook(c.getX(), c.getY());
                for (int i = 0; i < possibilities.size(); i++) {
                    possibleConfigs.add(possibilities.get(i));
                }
            }


        }

        return possibleConfigs;
    }
    /**
     * Gets the possible configurations of the King piece
     * @param x - The x-coordinate
     * @param y - The y-coordinate
     * @return Returns the possible configurations of the king.
     */

    public ArrayList<Configuration> getPossibleConfigsKing(int x, int y) {
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        allMovePossibilities.add(new Coordinates(x + 1, y));
        allMovePossibilities.add(new Coordinates(x - 1, y));
        allMovePossibilities.add(new Coordinates(x, y + 1));
        allMovePossibilities.add(new Coordinates(x, y - 1));
        allMovePossibilities.add(new Coordinates(x + 1, y - 1));
        allMovePossibilities.add(new Coordinates(x - 1, y - 1));
        allMovePossibilities.add(new Coordinates(x + 1, y + 1));
        allMovePossibilities.add(new Coordinates(x - 1, y + 1));

        for (int i = 0; i < allMovePossibilities.size(); i++) {
            Coordinates currObject = allMovePossibilities.get(i);
            if (!((currObject.getX() < 0) || (currObject.getY() < 0) || (currObject.getX() >= BOARD_SIZE) || (currObject.getY() >= BOARD_SIZE))) {
                validPossibilities.add(currObject);
            }
        }

        for (int i = 0; i < validPossibilities.size(); i++) {//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "K";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }
    /**
     * Gets the possible configurations of the Queen piece
     * @param x - The x-coordinate
     * @param y - The y-coordinate
     * @return Returns the possible configurations of the queen.
     */
    public ArrayList<Configuration> getPossibleConfigsQueen(int x, int y) {
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        for (int i = 1; i < BOARD_SIZE; i++) {
            allMovePossibilities.add(new Coordinates(x, y + i));
            allMovePossibilities.add(new Coordinates(x, y - i));
            allMovePossibilities.add(new Coordinates(x + i, y));
            allMovePossibilities.add(new Coordinates(x - i, y));
            allMovePossibilities.add(new Coordinates(x + i, y + i));
            allMovePossibilities.add(new Coordinates(x + i, y - i));
            allMovePossibilities.add(new Coordinates(x - i, y + i));
            allMovePossibilities.add(new Coordinates(x - i, y - i));
        }
        for (int i = 0; i < allMovePossibilities.size(); i++) {
            Coordinates currObject = allMovePossibilities.get(i);
            if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                validPossibilities.add(currObject);
            }
        }
        for (int i = 0; i < validPossibilities.size(); i++) {//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "Q";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }
    /**
     * Gets the possible configurations of the Rook piece
     * @param x - The x-coordinate
     * @param y - The y-coordinate
     * @return Returns the possible configurations of the rook.
     */
    public ArrayList<Configuration> getPossibleConfigsRook(int x, int y) {
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        for (int i = 1; i < BOARD_SIZE; i++) {
            allMovePossibilities.add(new Coordinates(x, y + i));
            allMovePossibilities.add(new Coordinates(x, y - i));
            allMovePossibilities.add(new Coordinates(x + i, y));
            allMovePossibilities.add(new Coordinates(x - i, y));
        }
        for (int i = 0; i < allMovePossibilities.size(); i++) {
            Coordinates currObject = allMovePossibilities.get(i);
            if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                validPossibilities.add(currObject);
            }
        }
        for (int i = 0; i < validPossibilities.size(); i++) {//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "R";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }

    /**
     * Gets the possible configurations of the Bishop piece
     * @param x - The x-coordinate
     * @param y - The y-coordinate
     * @return Returns the possible configurations of the bishop.
     */
    public ArrayList<Configuration> getPossibleConfigsBishop(int x, int y) {
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        for (int i = 1; i < BOARD_SIZE; i++) {//radiates outwards to find all possible moves at currentPosition
            allMovePossibilities.add(new Coordinates(x + i, y + i));
            allMovePossibilities.add(new Coordinates(x + i, y - i));
            allMovePossibilities.add(new Coordinates(x - i, y + i));
            allMovePossibilities.add(new Coordinates(x - i, y - i));
        }
        for (int i = 0; i < allMovePossibilities.size(); i++) {//prunes to eliminate all invalid moves
            Coordinates currObject = allMovePossibilities.get(i);
            if ((!((currObject.getX() < 0) || (currObject.getY() < 0) || (currObject.getX() >= BOARD_SIZE) || (currObject.getY() >= BOARD_SIZE)))) {
                validPossibilities.add(currObject);
            }
        }
        for (int i = 0; i < validPossibilities.size(); i++) {//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "B";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }
    /**
     * Gets the possible configurations of the Pawn piece
     * @param x - The x-coordinate
     * @param y - The y-coordinate
     * @return Returns the possible configurations of the pawn.
     */

    public ArrayList<Configuration> getPossibleConfigsPawn(int x, int y) {
        ArrayList<Coordinates> movePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        if (x == 0) {
            movePossibilities.add(new Coordinates(x + 1, y + 1));
            movePossibilities.add(new Coordinates(x + 1, y - 1));
        } else {
            movePossibilities.add(new Coordinates(x - 1, y + 1));
            movePossibilities.add(new Coordinates(x - 1, y - 1));
        }

        for (int i = 0; i < movePossibilities.size(); i++) {
            Coordinates currObject = movePossibilities.get(i);
            if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                validPossibilities.add(currObject);
            }
        }

        for (int i = 0; i < validPossibilities.size(); i++) {//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "P";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;

    }

    /**
     * Gets the possible configurations of the Knight piece
     * @param x - The x-coordinate
     * @param y - The y-coordinate
     * @return Returns the possible configurations of the knight.
     */

    public ArrayList<Configuration> getPossibleConfigsKnight(int x, int y) {
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        allMovePossibilities.add(new Coordinates(x + 2, y - 1));
        allMovePossibilities.add(new Coordinates(x + 2, y + 1));
        allMovePossibilities.add(new Coordinates(x + 1, y - 2));
        allMovePossibilities.add(new Coordinates(x + 1, y + 2));
        allMovePossibilities.add(new Coordinates(x - 2, y + 1));
        allMovePossibilities.add(new Coordinates(x - 2, y - 1));
        allMovePossibilities.add(new Coordinates(x - 1, y + 2));
        allMovePossibilities.add(new Coordinates(x - 1, y - 2));
        for (int i = 0; i < allMovePossibilities.size(); i++) {
            Coordinates currObject = allMovePossibilities.get(i);
            if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                validPossibilities.add(currObject);
            }

        }
        for (int i = 0; i < validPossibilities.size(); i++) {//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "N";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }
    /**
     * Checks to see if the board is valid or not.
     * @return - True or false depending on if the board is valid or not.
     */

    @Override
    public boolean isValid() {
        if (getNumChar(previousBoard) - getNumChar(board) == 1) {
            return true;
        }
        return false;
    }
    /**
     * Checks to see if the board has the correct amount of elements (the goal).
     * @return - The boolean response to if it has the correct amount of elements or not.
     */
    @Override
    public boolean isGoal() {
        int numberOfElements = getNumChar(this.board);
        if (numberOfElements == 1) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Converts the board to a String
     * @return The string version of the board.
     */
    public String toString() {
        String output = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                output += board[i][j];
            }
            output += "\n";
        }
        return output;
    }










//---------------GUI Methods-----------------//

    /**
     * Gives the user a a hint on what step they should take to win.
     */
    public void hint() {
        List<Configuration> a = obj.solveWithPath(this);
        if (a != null && a.size() > 1) {
            hintCalled = true;
            a.remove(0);
            System.out.println(a.get(0).toString());
            String[][] b = ((SoltrChessModel) a.get(0)).board;
            for (int i = 0; i < BOARD_SIZE; i++) {
                System.arraycopy(b[i], 0, board[i], 0, BOARD_SIZE);
            }
            numOfMoves += 1;
            announce("Hint Presented");
        } else {
            Stage newStage = new Stage();
            BorderPane mainPane = new BorderPane();
            Button restart = new Button("Restart");
            restart.setOnAction(event -> makeBoard(currFile));
            mainPane.setCenter(new Label("No hint available. This current board is unsolvable. Press restart"));
            mainPane.setBottom(restart);
            Scene scene = new Scene(mainPane);
            newStage.setTitle("Hint Error");
            newStage.setScene(scene);
            newStage.show();
        }
    }

    /**
     * Gets the solutions for the board
     * @param solutionStack - The stack that contains the solution
     */

    public void getSolution(List<Configuration> solutionStack) {
        ArrayList<String> boardLines = new ArrayList<String>();
        for (int i = 0; i < solutionStack.size(); i++) {
            String eachSolution = solutionStack.get(i).toString();
            for (String c : eachSolution.split("\n")) {
                boardLines.add(c);
            }//has 4 elements, one for each line
            for (int r = 0; r < boardLines.size(); r++) {
                String currLine = boardLines.get(r);
                for (int c = 0; c < boardLines.size(); c++) {
                    board[r][c] = String.valueOf(currLine.charAt(c));
                }
            }
            boardLines = new ArrayList<String>();
            mostRecentMove = new Coordinates(0,0);

            announce("Got Solution");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setNumOfMoves(numOfMoves + 1);
        }
    }

    /**
     * Checks to see if the solution is still available with the current configuration of the board.
     * @returns - Whether or not a solution exists with the current configuration.
     */
    public boolean solutionWithCurrBoard(){
        Optional <Configuration> a = obj.solve(this);
        if(a.isPresent()){
            solutionExisting = true;
        }
        else{
            solutionExisting = false;
        }
        return solutionExisting;
    }

    /**
     * Solver method
     */

    public void solve() {
        List<Configuration> a = obj.solveWithPath(this);
        if (a != null) {
            solveCalled = true;
            getSolution(a);
            numOfMoves += a.size() - 1;
            solutionExisting = true;

        } else {
            solutionExisting = false;
        }
    }

    /**
     * Method that evaluates all possible moves a piece can move on the board.
     * @param sR - The source row
     * @param sC - The source column
     * @param dR - The destination row
     * @param dC - The destination column
     */
    public void evalMove(int sR, int sC, int dR, int dC) {
        if (board[sR][sC].contains("B")) { //Bishop - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for (int i = 1; i < BOARD_SIZE; i++) {
                allMovePossibilities.add(new Coordinates(sR + i, sC + i));
                allMovePossibilities.add(new Coordinates(sR + i, sC - i));
                allMovePossibilities.add(new Coordinates(sR - i, sC + i));
                allMovePossibilities.add(new Coordinates(sR - i, sC - i));
            }
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (!((currObject.getX() < 0) || (currObject.getY() < 0) || (currObject.getX() >= BOARD_SIZE) || (currObject.getY() >= BOARD_SIZE))) {
                    validPossibilities.add(currObject);
                }
            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                    hasChanged = true;
                    firstMoveMade = true;
                    mostRecentMove = new Coordinates(dR,dC);
                }
            }
            announce(null);

        } else if (board[sR][sC].contains("K")) { //King -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR + 1, sC));
            allMovePossibilities.add(new Coordinates(sR - 1, sC));
            allMovePossibilities.add(new Coordinates(sR, sC + 1));
            allMovePossibilities.add(new Coordinates(sR, sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 1, sC - 1));
            allMovePossibilities.add(new Coordinates(sR - 1, sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 1, sC + 1));
            allMovePossibilities.add(new Coordinates(sR - 1, sC + 1));

            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (!((currObject.getX() < 0) || (currObject.getY() < 0) || (currObject.getX() >= BOARD_SIZE) || (currObject.getY() >= BOARD_SIZE))) {
                    validPossibilities.add(currObject);
                }
            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                    hasChanged = true;
                    firstMoveMade = true;
                    mostRecentMove = new Coordinates(dR,dC);
                }
            }
            announce(null);
        } else if (board[sR][sC].contains("N")) {//knight - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR + 2, sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 2, sC + 1));
            allMovePossibilities.add(new Coordinates(sR + 1, sC - 2));
            allMovePossibilities.add(new Coordinates(sR + 1, sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 2, sC + 1));
            allMovePossibilities.add(new Coordinates(sR - 2, sC - 1));
            allMovePossibilities.add(new Coordinates(sR - 1, sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 1, sC - 2));
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }

            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                    hasChanged = true;
                    firstMoveMade = true;
                    mostRecentMove = new Coordinates(dR,dC);
                }
            }
            announce(null);

        } else if (board[sR][sC].contains("P")) {//Pawn - finished
            ArrayList<Coordinates> movePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            if (sR == 0) {
                movePossibilities.add(new Coordinates(sR + 1, sC + 1));
                movePossibilities.add(new Coordinates(sR + 1, sC - 1));
            } else {
                movePossibilities.add(new Coordinates(sR - 1, sC + 1));
                movePossibilities.add(new Coordinates(sR - 1, sC - 1));
            }

            for (int i = 0; i < movePossibilities.size(); i++) {
                Coordinates currObject = movePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }
            }

            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                    hasChanged = true;
                    firstMoveMade = true;
                    mostRecentMove = new Coordinates(dR,dC);
                }
            }

            announce(null);
        } else if (board[sR][sC].contains("R")) {//Rook -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for (int i = 1; i < BOARD_SIZE; i++) {
                allMovePossibilities.add(new Coordinates(sR, sC + i));
                allMovePossibilities.add(new Coordinates(sR, sC - i));
                allMovePossibilities.add(new Coordinates(sR + i, sC));
                allMovePossibilities.add(new Coordinates(sR - i, sC));
            }
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }

            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                    hasChanged = true;
                    firstMoveMade = true;
                    mostRecentMove = new Coordinates(dR,dC);
                }
            }
            announce(null);

        } else if (board[sR][sC].contains("Q")){ //contains Queen - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for (int i = 1; i < BOARD_SIZE; i++) {
                allMovePossibilities.add(new Coordinates(sR, sC + i));
                allMovePossibilities.add(new Coordinates(sR, sC - i));
                allMovePossibilities.add(new Coordinates(sR + i, sC));
                allMovePossibilities.add(new Coordinates(sR - i, sC));
                allMovePossibilities.add(new Coordinates(sR + i, sC + i));
                allMovePossibilities.add(new Coordinates(sR + i, sC - i));
                allMovePossibilities.add(new Coordinates(sR - i, sC + i));
                allMovePossibilities.add(new Coordinates(sR - i, sC - i));
            }
            for (int i = 0; i < allMovePossibilities.size(); i++) {
                Coordinates currObject = allMovePossibilities.get(i);
                if (((currObject.getX() >= 0) && currObject.getX() < BOARD_SIZE) && (currObject.getY() >= 0 && currObject.getY() < BOARD_SIZE)) {
                    validPossibilities.add(currObject);
                }
            }
            for (Coordinates c : validPossibilities) {
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))) {
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                    hasChanged = true;
                    firstMoveMade = true;
                    mostRecentMove = new Coordinates(dR,dC);
                }
            }
            announce(null);

        }
        else{hasChanged=false;}
        setNumOfMoves(getNumOfMoves()+1);

    }
}
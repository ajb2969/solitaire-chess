package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Observable;
import backtracking.*;
import jdk.nashorn.internal.runtime.regexp.joni.CodeRangeBuffer;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

/**
 * Created by alexbrown on 11/11/16.
 */
public class SoltrChessModel extends Observable implements Configuration{
    private static final int BOARD_SIZE = 4;
    private int numOfChars;
    private static final int NUM_CARDS = BOARD_SIZE * BOARD_SIZE;
    private static String [][]board;
    private String[][] previousBoard;
    private static String currFile;
    private Backtracker obj;
    private boolean valid;

    public SoltrChessModel(String fileName){
        currFile = fileName;
        board = makeBoard(fileName);
        obj = new Backtracker();
        numOfChars = getNumOfChar(board);
        previousBoard = makeBoard(fileName);
        valid = true;
    }
    public String[][] getBoard(){
        return board;
    }

    public String[][] getPreviousBoard(){
        return previousBoard;
    }


    public SoltrChessModel(SoltrChessModel copy){
        this.numOfChars = copy.numOfChars;
        this.currFile = copy.currFile;
        obj = new Backtracker();
        previousBoard = new String [BOARD_SIZE][BOARD_SIZE];
        String [][] copyBoard = copy.getBoard();
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                this.board[i][j] = copyBoard[i][j];
            }
        }

        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                this.previousBoard[i][j] = copy.previousBoard[i][j];
            }
        }

        valid = true;
    }

    public String[][] makeBoard(String fileName){
        board = new String [BOARD_SIZE][BOARD_SIZE];
        try{
            currFile = fileName;
            Scanner input = new Scanner(new File(fileName));
            for(int i = 0; i< BOARD_SIZE; i++){
                String currLine = input.nextLine();
                String[] lineSplit = currLine.split("\\s+");
                for(int j = 0; j < lineSplit.length; j++){
                    if(lineSplit[j].equals("-")){board[i][j] = "-";}
                    else if(lineSplit[j].equals("P")){board[i][j] = "P";}
                    else if(lineSplit[j].equals("B")){board[i][j] = "B";}
                    else if(lineSplit[j].equals("K")){board[i][j] = "K";}
                    else if(lineSplit[j].equals("N")){board[i][j] = "N";}
                    else if(lineSplit[j].equals("R")){board[i][j] = "R";}
                    else if(lineSplit[j].equals("Q")){board[i][j] = "Q";}
                    else{
                        System.out.println("File is Invalid");
                        System.exit(1);
                    }

                }

            }
        }
        catch(FileNotFoundException e){
            System.out.println("File could not be found");
            System.exit(1);
        }

        return board;
    }



    public void printBoard(){
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void evaluateMove(int sR,int sC,int dR,int dC){
        if(board[sR][sC].contains("-")){
            System.out.println("Invalid source element");
            evaluate("move");
        }
        if(board[dR][dC].contains("-")){
            System.out.println("Invalid Destination element");
            evaluate("move");
        }
        if(board[sR][sC].contains("B")){ //Bishop - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for(int i = 0; i<BOARD_SIZE; i++){
                allMovePossibilities.add(new Coordinates(sR+i,sC+i));
                allMovePossibilities.add(new Coordinates(sR+i,sC-i));
                allMovePossibilities.add(new Coordinates(sR-i,sC+i));
                allMovePossibilities.add(new Coordinates(sR-i,sC-i));
            }
            for(int i = 0; i < allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(!((currObject.getX() < 0)||(currObject.getY() < 0)||(currObject.getX() >= BOARD_SIZE)||(currObject.getY() >= BOARD_SIZE))){
                    validPossibilities.add(currObject);
                }
            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }
        else if(board[sR][sC].contains("K")){ //King -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR+1,sC));
            allMovePossibilities.add(new Coordinates(sR-1,sC));
            allMovePossibilities.add(new Coordinates(sR,sC+1));
            allMovePossibilities.add(new Coordinates(sR,sC-1));
            allMovePossibilities.add(new Coordinates(sR+1,sC-1));
            allMovePossibilities.add(new Coordinates(sR-1,sC-1));
            allMovePossibilities.add(new Coordinates(sR+1,sC+1));
            allMovePossibilities.add(new Coordinates(sR-1,sC+1));

            for(int i = 0; i < allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(!((currObject.getX() < 0)||(currObject.getY() < 0)||(currObject.getX() >= BOARD_SIZE)||(currObject.getY() >= BOARD_SIZE))){
                    validPossibilities.add(currObject);
                }
            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);
        }
        else if(board[sR][sC].contains("N")){//knight - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR + 2,sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 2,sC + 1));
            allMovePossibilities.add(new Coordinates(sR + 1,sC - 2));
            allMovePossibilities.add(new Coordinates(sR + 1,sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 2,sC + 1));
            allMovePossibilities.add(new Coordinates(sR - 2,sC - 1));
            allMovePossibilities.add(new Coordinates(sR - 1,sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 1,sC - 2));
            for(int i=0; i<allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                    validPossibilities.add(currObject);
                }

            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }
        else if(board[sR][sC].contains("P")){//Pawn - finished
            ArrayList<Coordinates> movePossibilities = new ArrayList<Coordinates>();
            if (sR == 0 ){
                movePossibilities.add(new Coordinates(sR - 1,sC + 1));
                movePossibilities.add(new Coordinates(sR - 1,sC - 1));
            }
            else{
                movePossibilities.add(new Coordinates(sR + 1,sC + 1));
                movePossibilities.add(new Coordinates(sR + 1,sC - 1));
            }


            for(Coordinates c: movePossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }

            announce(null);
        }
        else if(board[sR][sC].contains("R")){//Rook -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for(int i = 0; i<BOARD_SIZE; i++){
                allMovePossibilities.add(new Coordinates(sR, sC+i));
                allMovePossibilities.add(new Coordinates(sR, sC-i));
                allMovePossibilities.add(new Coordinates(sR+i, sC));
                allMovePossibilities.add(new Coordinates(sR-i, sC));
            }
            for(int i=0; i<allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                    validPossibilities.add(currObject);
                }

            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);
            
        }
        else{ //contains Queen - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for(int i = 0; i<BOARD_SIZE; i++){
                allMovePossibilities.add(new Coordinates(sR,sC+i));
                allMovePossibilities.add(new Coordinates(sR,sC-i));
                allMovePossibilities.add(new Coordinates(sR+i,sC));
                allMovePossibilities.add(new Coordinates(sR-i,sC));
                allMovePossibilities.add(new Coordinates(sR+i,sC+i));
                allMovePossibilities.add(new Coordinates(sR+i,sC-i));
                allMovePossibilities.add(new Coordinates(sR-i,sC+i));
                allMovePossibilities.add(new Coordinates(sR-i,sC-i));
            }
            for(int i=0; i<allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                    validPossibilities.add(currObject);
                }

            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }

    }
    public void evaluate(String choice){ // still have to complete hint and solve
        Scanner input = new Scanner(System.in);
        if(choice.equals("move")){
            System.out.print("Enter the source row: ");
            int sourceRow = input.nextInt();
            System.out.print("Enter the source col: ");
            int sourceCol = input.nextInt();
            System.out.print("Enter the dest row: ");
            int destRow = input.nextInt();
            System.out.print("Enter the dest col: ");
            int destCol = input.nextInt();
            evaluateMove(sourceRow,sourceCol,destRow,destCol);

        }
        else if(choice.equals("new")){
            System.out.print("Enter file name: ");
            String newFileName = input.nextLine();
            newFileName = "data/"+newFileName;
            System.out.println("New Game " + newFileName);
            makeBoard(newFileName);
            announce(null);
        }
        else if(choice.equals("restart")){
            makeBoard(currFile);
            announce(null);
        }






        else if(choice.equals("hint")){ // need to write
            Stack<Configuration> solution;
            Configuration currConfig = new SoltrChessModel(this);
            solution = obj.solveWithPath(currConfig);
            Configuration nextMove = solution.pop();
            String [][] newBoard = ((SoltrChessModel)nextMove).getBoard();
            String [][] oldBoard = getBoard();
            for(int i = 0; i<BOARD_SIZE; i++){
                for(int j =0; j<BOARD_SIZE; j++){
                    oldBoard[i][j] = newBoard[i][j];
                }
            }
            ((SoltrChessModel)nextMove).printBoard();
            announce(null);

        }
        else if(choice.equals("solve")){//need to write
            Stack<Configuration> solution;
            int i = 1;
            Configuration currConfig = new SoltrChessModel(this);
            solution = obj.solve(currConfig);
            if(solution != null){
                while(!(solution.empty())){
                    Configuration step = solution.pop();
                    System.out.println("Step " + i);
                    ((SoltrChessModel)step).printBoard();
                }
            }
            else{
                System.out.println("There is no solution");
                System.exit(1);
            }
        }
        else if(choice.equals("quit")){
            System.out.println("The game has been quit");
            System.exit(0);
        }
        else{
            System.out.println("Invalid Input value");
            evaluate("move");

        }

    }



    private void announce(String arg) {
        setChanged();
        notifyObservers(arg);
    }

    public ArrayList<Configuration> getValidConfigurations(HashMap<Coordinates,String> charPositions){
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        for(Coordinates c: charPositions.keySet()){
            if(charPositions.get(c).equals("B")){


            }

        }
        return null;
    }

    @Override
    public Collection<Configuration> getSuccessors() {// need to write
        ArrayList<Configuration> allSuccessors = new ArrayList<Configuration>();
        HashMap<Coordinates,String> charPositions = new HashMap<Coordinates, String>();
        String [][] b = getBoard();
        for(int i = 0; i <BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                if(!(b[i][j].equals("-"))){
                    charPositions.put(new Coordinates(i,j), board[i][j]);
                }
            }
        }
        allSuccessors = getValidConfigurations(charPositions);

        return null;
    }

    private int getNumOfChar(String [][] b){
        int numChar = 0;
        for(int i = 0; i<b.length; i++){
            for(int j = 0; j<b.length; j++){
                if(!(b[i][j].equals("-"))){
                    numChar++;
                }
            }
        }
        return numChar;
    }
    @Override
    public boolean isValid() {
        if(!valid)return false;

        int previousBoardChar = getNumOfChar(previousBoard);
        int currBoardChar = getNumOfChar(board);
        if(previousBoardChar - currBoardChar != 1){
            valid = false;
            return false;
        }
        else{
            return true;
        }
    }
    @Override
    public boolean isGoal() {
        if(!isValid())return false;

        int numberOfElements = 0;
        for(int i = 0; i< BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                if(!board[i][j].equals("-")){
                    numberOfElements++;
                }
            }
        }
        if(numberOfElements == 1){return true;}
        else{return false;}
    }

}

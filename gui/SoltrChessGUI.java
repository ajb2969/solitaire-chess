/**
 * Part of SoltrChessLayout project.
 * Created 10 2015
 *
 * @author James Heliotis
 */

package gui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import model.Coordinates;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.*;

/**
 * A miniature chess board
 *
 * @author James Heliotis
 */
public class SoltrChessGUI extends Application implements Observer {
    /**
     * Construct the layout for the game.
     *
     * @param stage container (window) in which to render the UI
     */

    private SoltrChessModel model;
    private final int BOARD_SIZE = 4;
    private Stage primaryStage;
    private String filename;
    private HashMap<Coordinates,String> coordinates;
    private HashMap<String, Image> getFotos;
    private String [][] board;
    private GridPane gridPane;
    private Label title;
    private Label moves;
    private Button b;
    private Stack<Coordinates> moveStack;

    public void updateMethod() {
        board = model.getBoard();
        if(model.isGoal() && model.getHasChanged() && !model.getSolveCalled() && !model.getHintCalled()){
            title.setText("Game File: " + model.getCurrFile() + "    Most Recent Position: "+ board[model.getRecentMoveRow()][model.getRecentMoveCol()] + " to square "+"["+model.getRecentMoveRow()+","+model.getRecentMoveCol() +"]" +" - You Win!");
            moves.setText("Moves: " + model.getNumOfMoves());
        }else if (model.getFirstMoveMade()){
            title.setText("Game File: " + model.getCurrFile() + "      Most Recent Position: "+ board[model.getRecentMoveRow()][model.getRecentMoveCol()] + " to square "+"["+model.getRecentMoveRow()+","+model.getRecentMoveCol() +"]");
            moves.setText("Moves: " + model.getNumOfMoves());
        }
        else{
            moves.setText("Moves: " + model.getNumOfMoves());
            title.setText("Game File: " + model.getCurrFile());
        }


        Image oneFront = new Image (getClass().getResourceAsStream("resources/dark.png"));
        Image twoFront = new Image(getClass().getResourceAsStream("resources/light.png"));
        BackgroundImage backgroundImage1 = new BackgroundImage( new Image( getClass().getResource("resources/light.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background1 = new Background(backgroundImage1);

        BackgroundImage backgroundImage2 = new BackgroundImage( new Image( getClass().getResource("resources/dark.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background2 = new Background(backgroundImage2);


        //gridPane.setMinSize(300,500);
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                b= new Button();
                if((i + j) % 2 == 0){
                    b.setBackground(background1);
                    if(board[i][j].equals("P")){b.setGraphic(new ImageView(getFotos.get("P")));}
                    else if(board[i][j].equals("N")){b.setGraphic(new ImageView(getFotos.get("N")));}
                    else if(board[i][j].equals("R")){b.setGraphic(new ImageView(getFotos.get("R")));}
                    else if(board[i][j].equals("B")){b.setGraphic(new ImageView(getFotos.get("B")));}
                    else if(board[i][j].equals("K")){b.setGraphic(new ImageView(getFotos.get("K")));}
                    else if(board[i][j].equals("Q")){b.setGraphic(new ImageView(getFotos.get("Q")));}
                    else{b.setGraphic(new ImageView(twoFront));}


                }
                else{
                    b.setBackground(background2);
                    if(board[i][j].equals("P")){b.setGraphic(new ImageView(getFotos.get("P")));}
                    else if(board[i][j].equals("N")){b.setGraphic(new ImageView(getFotos.get("N")));}
                    else if(board[i][j].equals("R")){b.setGraphic(new ImageView(getFotos.get("R")));}
                    else if(board[i][j].equals("B")){b.setGraphic(new ImageView(getFotos.get("B")));}
                    else if(board[i][j].equals("K")){b.setGraphic(new ImageView(getFotos.get("K")));}
                    else if(board[i][j].equals("Q")){b.setGraphic(new ImageView(getFotos.get("Q")));}
                    else{b.setGraphic(new ImageView(oneFront));}
                }
                int r = i;
                int c = j;
                b.setOnAction(event -> selectCard(r,c));
                gridPane.add(b,j,i);
            }
        }

        if(!model.solutionWithCurrBoard()){
            Stage newStage = new Stage();
            BorderPane mainPane = new BorderPane();
            Button restart = new Button("Restart");
            restart.setOnAction(event -> model.makeBoard(model.getCurrFile()));
            mainPane.setCenter(new Label("No Solution. Press restart"));
            mainPane.setBottom(restart);
            Scene scene = new Scene(mainPane);
            newStage.setTitle("No Solution Error");
            newStage.setScene(scene);
            newStage.show();
        }


    }


    public void start( Stage primaryStage ) throws Exception {
        board = model.getBoard();
        coordinates = model.getCoordinates();
        getFotos = new HashMap<String,Image>();
        Image bishop = new Image(getClass().getResourceAsStream("resources/bishop.png"));
        Image king = new Image(getClass().getResourceAsStream("resources/king.png"));
        Image knight = new Image(getClass().getResourceAsStream("resources/knight.png"));
        Image queen =  new Image(getClass().getResourceAsStream("resources/queen.png"));
        Image rook = new Image(getClass().getResourceAsStream("resources/rook.png"));
        Image pawn = new Image(getClass().getResourceAsStream("resources/pawn.png"));
        getFotos.put("B",bishop);
        getFotos.put("K",king);
        getFotos.put("Q",queen);
        getFotos.put("R",rook);
        getFotos.put("N",knight);
        getFotos.put("P",pawn);
        Image oneFront = new Image (getClass().getResourceAsStream("resources/dark.png"));
        Image twoFront = new Image(getClass().getResourceAsStream("resources/light.png"));

        BackgroundImage backgroundImage1 = new BackgroundImage( new Image( getClass().getResource("resources/light.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background1 = new Background(backgroundImage1);

        BackgroundImage backgroundImage2 = new BackgroundImage( new Image( getClass().getResource("resources/dark.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background2 = new Background(backgroundImage2);


        BorderPane mainPane = new BorderPane();
        //mainPane.setMinSize(500,300);

        gridPane = new GridPane();//adds the grid

        //gridPane.setMinSize(300,500);
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                b= new Button();
                if((i + j) % 2 == 0){
                    b.setBackground(background1);
                    if(board[i][j].equals("P")){b.setGraphic(new ImageView(getFotos.get("P")));}
                    else if(board[i][j].equals("N")){b.setGraphic(new ImageView(getFotos.get("N")));}
                    else if(board[i][j].equals("R")){b.setGraphic(new ImageView(getFotos.get("R")));}
                    else if(board[i][j].equals("B")){b.setGraphic(new ImageView(getFotos.get("B")));}
                    else if(board[i][j].equals("K")){b.setGraphic(new ImageView(getFotos.get("K")));}
                    else if(board[i][j].equals("Q")){b.setGraphic(new ImageView(getFotos.get("Q")));}
                    else{b.setGraphic(new ImageView(twoFront));}


                }
                else{
                    b.setBackground(background2);
                    if(board[i][j].equals("P")){b.setGraphic(new ImageView(getFotos.get("P")));}
                    else if(board[i][j].equals("N")){b.setGraphic(new ImageView(getFotos.get("N")));}
                    else if(board[i][j].equals("R")){b.setGraphic(new ImageView(getFotos.get("R")));}
                    else if(board[i][j].equals("B")){b.setGraphic(new ImageView(getFotos.get("B")));}
                    else if(board[i][j].equals("K")){b.setGraphic(new ImageView(getFotos.get("K")));}
                    else if(board[i][j].equals("Q")){b.setGraphic(new ImageView(getFotos.get("Q")));}
                    else{b.setGraphic(new ImageView(oneFront));}
                }
                int r = i;
                int c = j;
                b.setOnAction(event -> selectCard(r,c));
                gridPane.add(b,j,i);
            }
        }

        mainPane.setCenter(gridPane);
        HBox titleBox = new HBox();
        title = new Label("Game File: " + model.getCurrFile());
        titleBox.getChildren().add(title);
        mainPane.setTop(titleBox);
        titleBox.setAlignment(Pos.CENTER);
        HBox buttonsAndLabels = new HBox();
        moves = new Label("Moves: " + model.getNumOfMoves());
        Button newGame = new Button("New Game");
        newGame.setOnAction(event -> newGame(primaryStage));
        Button restart = new Button("Restart");
        restart.setOnAction(event -> model.makeBoard(model.getCurrFile()));
        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.hint());
        Button solve = new Button("Solve");
        solve.setOnAction(event-> new Thread(() -> model.solve()).start());
        buttonsAndLabels.getChildren().addAll(newGame,restart,hint,solve,moves);
        mainPane.setBottom(buttonsAndLabels);
        buttonsAndLabels.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainPane);
        primaryStage.setTitle("Solitaire Chess- Alex Brown && Raymond Gonzalez");

        primaryStage.setScene(scene);
        this.primaryStage = primaryStage;
        primaryStage.show();
    }

    @Override
    public void init()throws Exception{
        Parameters parameters = getParameters();
        ArrayList<String> param = new ArrayList<String>(parameters.getRaw());
        model = new SoltrChessModel(param.get(0));
        filename = param.get(0);
        moveStack = new Stack<Coordinates>();
        this.model.addObserver(this);

    }



    public void newGame(Stage mainStage){
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            String fileName = selectedFile.toString();
            ArrayList<String> splitFileName = new ArrayList<String>();
            for (String c : fileName.split("/")) {
                splitFileName.add(c);
            }
            int i = 0;
            while (!(splitFileName.get(i).equals("data"))) {
                splitFileName.remove(i);
            }
            String fileNameCompleted = "";
            for (String e : splitFileName) {
                fileNameCompleted += "/" + e;
            }
            fileNameCompleted = fileNameCompleted.substring(1);
            model.makeBoard(fileNameCompleted);
            model.setNumOfMoves(0);
            model.announce("Board changed");
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Index out of bounds exception");
            System.exit(1);
        }


    }



    public void selectCard(int r, int c){
        if(moveStack.size() == 0){
            moveStack.add(new Coordinates(r,c));//source
        }
        else if(moveStack.size() == 1){
            moveStack.add(new Coordinates(r,c));//destination
            Coordinates dest = moveStack.pop();
            Coordinates src = moveStack.pop();
            model.evalMove(src.getX(),src.getY(),dest.getX(),dest.getY());
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> updateMethod());
    }
}

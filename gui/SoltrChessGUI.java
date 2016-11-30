/**
 * Part of SoltrChessLayout project.
 * Created 10 2015
 *
 * @author James Heliotis
 */

package gui;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.application.Application;
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
import model.SoltrChessModel;
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
    @Override
    public void update(Observable o, Object arg) {
        title.setText("Game File: " + model.getCurrFile());
    }


    public void start( Stage primaryStage ) throws Exception {
        board = model.getBoard();
        model.printBoard();
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

        BorderPane mainPane = new BorderPane();
        mainPane.prefHeight(300);
        mainPane.prefWidth(500);

        gridPane = new GridPane();//adds the
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                Button b = new Button();
                if((i + j) % 2 == 0){
                    b.setGraphic(new ImageView(oneFront));
                }
                else{
                    b.setGraphic(new ImageView(twoFront));
                }
                gridPane.add(b,i,j);
            }
        }


        mainPane.setCenter(gridPane);
        HBox titleBox = new HBox();
        title = new Label("Game File: " + model.getCurrFile());
        titleBox.getChildren().add(title);
        mainPane.setTop(titleBox);
        titleBox.setAlignment(Pos.CENTER);
        HBox buttons = new HBox();
        Button newGame = new Button("New Game");
        newGame.setOnAction(event -> newGame(primaryStage));
        Button restart = new Button("Restart");
        restart.setOnAction(event -> model.makeBoard(filename));
        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.hint());
        Button solve = new Button("Solve");
        solve.setOnAction(event->model.solve());
        buttons.getChildren().addAll(newGame,restart,hint,solve);
        mainPane.setBottom(buttons);
        buttons.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainPane);
        primaryStage.setTitle("Solitaire Chess");

        primaryStage.setScene(scene);
        this.primaryStage = primaryStage;
        primaryStage.show();
    }

    public void setFileName(String file){
        this.filename = file;
    }

    @Override
    public void init()throws Exception{
        System.out.println("inti: Initialize and connect to model!");
        Parameters parameters = getParameters();
        ArrayList<String> param = new ArrayList<String>(parameters.getRaw());
        model = new SoltrChessModel(param.get(0));
        this.model.addObserver(this);
    }



    public void newGame(Stage mainStage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        String fileName = selectedFile.toString();
        ArrayList<String> splitFileName = new ArrayList<String>();
        for(String c:fileName.split("/")){
            splitFileName.add(c);
        }
        int i =0;
        while(!(splitFileName.get(i).equals("data"))){
            splitFileName.remove(i);
        }
        String fileNameCompleted = "";
        for(String e: splitFileName){
            fileNameCompleted += "/" + e;
        }
        fileNameCompleted = fileNameCompleted.substring(1);
        model.makeBoard(fileNameCompleted);
        model.announce("Board changed");




    }





}

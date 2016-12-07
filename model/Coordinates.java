package model;

/**
 * Created by alexbrown on 11/17/16.
 */
public class Coordinates {
    /*
    * Coordinates on the model to be accessed later.
    */
    private int x;
    private int y;
    public Coordinates(int x_coor, int y_coor){
        x = x_coor;
        y = y_coor;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}

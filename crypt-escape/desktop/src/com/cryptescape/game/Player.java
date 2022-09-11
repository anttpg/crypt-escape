package com.cryptescape.game;

public class Player extends Movables{
    public boolean isRunning = false;

    public Player(double x,double y){
        super(x, y, 2.0, 8.0);
    }
    
    public void changeVelocity(){
        velocity += acceleration;
    }

}

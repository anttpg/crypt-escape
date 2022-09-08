package com.cryptescape.game;

import com.badlogic.gdx.ai.btree.decorator.Random;

public class Enemy extends Movables{

    int[][] previousRoom;
    public Enemy(double x,double y){
        super(x, y, 6.0, 24.0);
    }

    public void chooseRoom()
    {
        int randRoom = (int)(Math.random() * 4);
        if(randRoom == 0){
            CurrentRoomX -=1; 
        }
        if(randRoom == 1){
            CurrentRoomX +=1; 
        }
        if(randRoom == 2){
            CurrentRoomY -=1; 
        }
        if(randRoom == 3){
            CurrentRoomY +=1; 
        }
        
    }
}

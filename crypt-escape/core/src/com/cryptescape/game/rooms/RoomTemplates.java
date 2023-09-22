package com.cryptescape.game.rooms;

import java.util.ArrayList;

public class RoomTemplates {
    public static ArrayList<String> templateList = new ArrayList<String>();
    private String roomStyle;
    private String roomName;
    private String roomNickname;
    private String[][] seed;
    
    public RoomTemplates(String name, String style, String nickname) {
        this.roomName = name;
        this.roomNickname = nickname;
        this.roomStyle = style;
    }
    
    public static void loadMap() {
        //DoSomething
    }

    public static String[][] getSkin(String roomType) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

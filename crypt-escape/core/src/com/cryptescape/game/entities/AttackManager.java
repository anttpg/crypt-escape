//package com.cryptescape.game.entities;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class AttackManager {
//    private Movables self;
//    private Map<String, Attack> attackTypes = new HashMap<String, Attack>();   
//    
//    public AttackManager(Movables self) {
//        this.self = self;
//    }
//    
//    public void attachAttack(String attackName) {
//        if(attackName.equals("crowbar_swipe"))
//            attackTypes.put(attackName, new Attack());
//    }
//    
//    public void attack(String attackName, double angle) {
//        attackTypes.get(attackName).attack(angle, self.getX(), self.getY());
//    }
//}

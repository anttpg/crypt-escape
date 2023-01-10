package com.cryptescape.game;

public class Event {
    private float timer;
    private Runnable method;
    
    public Event(float timeTillRun, Runnable runnableMethod) throws IllegalArgumentException {
        if(timeTillRun < 0 || runnableMethod == null)
            throw new IllegalArgumentException("Time cannot be negative, and method to run must be defined.");
        
        timer = timeTillRun;
        method = runnableMethod;
    }
    
    public Boolean trigger() {
        timer -= Constants.FRAME_SPEED;
        
        if(timer > 0)
            return false;
        
        method.run();
        return true;        
    }
}

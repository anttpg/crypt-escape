package com.cryptescape.game.entities;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
 
import java.util.HashMap;
import java.util.Objects;
 
public class AnimationHandler {
 
 
    private float timer = 0;
    private boolean looping = true;
    private String current;
    private final HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();
 
    public void add(String name, Animation<TextureRegion> animation){
        animations.put(name, animation);
    }
 
    public void setCurrent(String name){
        if (Objects.equals(current, name)) return;
        assert (animations.containsKey(name)) : "No such animation " + name;
        current = name;
        timer = 0;
        looping = true;
    }
    
    public void setPlayModes(Animation.PlayMode mode) {
    	animations.forEach((name, anima) -> anima.setPlayMode(mode));
    }
 
    public void setCurrent(String name, boolean looping){
        setCurrent(name);
        this.looping = looping;
    }
    public void setAnimationDuration(float duration){
        animations.get(current).setFrameDuration(duration);
        //System.out.println("durations: " + duration); 
    }
 
    public boolean isCurrent(String name){
        return current.equals(name);
    }
    public boolean isFinished(){
        return animations.get(current).isAnimationFinished(timer);
    }
    public int frameIndex(){
        return animations.get(current).getKeyFrameIndex(timer);
    }
 
    public TextureRegion getFrame(){
        timer += Gdx.graphics.getDeltaTime();
        try {
            return animations.get(current).getKeyFrame(timer, looping);
        } catch(java.lang.ArithmeticException e) {
            System.err.println("Divide by 0 error. likely file name issues, animation has no frames.");
            return null;
        }
    }
    
    public void addTime(float time) {
    	timer += time;
    }
 
    @Override
    public String toString() {
        return "AnimationHandler{" +
                "timer=" + timer +
                ", looping=" + looping +
                ", current='" + current + '\'' +
                ", current frame=" + animations.get(current).getKeyFrameIndex(timer) +
                '}';
    }

	public Animation<TextureRegion> getCurrent() {
		return animations.get(current);
	}
}
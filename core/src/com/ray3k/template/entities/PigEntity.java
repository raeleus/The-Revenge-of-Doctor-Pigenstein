package com.ray3k.template.entities;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.ray3k.template.screens.GameScreen;

public class PigEntity extends Entity {
    private GameScreen gameScreen;
    private SkeletonData skeletonData;
    private AnimationStateData animationStateData;
    private static Animation blinkAnimation;
    private static Animation frownAnimation;
    private static Animation neutralFaceAnimation;
    private static Animation smileAnimation;
    private static Animation standAnimation;
    private static Animation standSideAnimation;
    private static Animation surpriseAnimation;
    private static Animation walkAnimation;
    
    @Override
    public void create() {
        depth = GameScreen.PIG_DEPTH;
        gameScreen = GameScreen.gameScreen;
        
        if (skeletonData == null) {
            skeletonData = core.pigSkeletonData;
            animationStateData = core.pigAnimationStateData;
            
            blinkAnimation = skeletonData.findAnimation("blink");
            frownAnimation = skeletonData.findAnimation("frown");
            neutralFaceAnimation = skeletonData.findAnimation("neutral-face");
            smileAnimation = skeletonData.findAnimation("smile");
            standAnimation = skeletonData.findAnimation("stand");
            standSideAnimation = skeletonData.findAnimation("stand-side");
            surpriseAnimation = skeletonData.findAnimation("surprise");
            walkAnimation = skeletonData.findAnimation("walk");
        }
        
        setSkeletonData(skeletonData, animationStateData);
        animationState.setAnimation(0, walkAnimation, true);
        animationState.setAnimation(1, smileAnimation, true);
        animationState.addAnimation(2, blinkAnimation, true, MathUtils.random(3f));
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
        if (gameScreen.grabbedPig == null) {
            if (GameScreen.isButtonJustPressed(-1) && skeletonBounds.aabbContainsPoint(GameScreen.mouseX, GameScreen.mouseY)) {
                gameScreen.grabbedPig = this;
                gameScreen.grabbedPigOffsetX = x - GameScreen.mouseX;
                gameScreen.grabbedPigOffsetY = y - GameScreen.mouseY;
                gameScreen.pigDeltaX = 0;
                gameScreen.pigDeltaY = 0;
            }
        } else if (gameScreen.grabbedPig == this) {
            if (!GameScreen.isButtonPressed(-1)) {
                gameScreen.grabbedPig = null;
                deltaX = gameScreen.pigDeltaX * 2;
                deltaY = gameScreen.pigDeltaY * 2;
                setGravity(3000, 270);
            }
        }
    }
    
    @Override
    public void draw(float delta) {
    
    }
    
    @Override
    public void destroy() {
    
    }
}

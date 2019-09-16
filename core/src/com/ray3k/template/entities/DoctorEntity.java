package com.ray3k.template.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;
import com.ray3k.template.screens.GameScreen;

public class DoctorEntity extends Entity {
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
    public static Skin monocleSkin;
    public static Skin topHatSkin;
    
    @Override
    public void create() {
        depth = GameScreen.DOCTOR_DEPTH;
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

            monocleSkin = skeletonData.findSkin("glasses/monocle");
            topHatSkin = skeletonData.findSkin("hat/top-hat");
        }
        
        setSkeletonData(skeletonData, animationStateData);
        animationState.setAnimation(0, standAnimation, false);
        animationState.addAnimation(2, blinkAnimation, true, MathUtils.random(3f));
        Skin skin = new Skin("doctor-skin");
        skin.addSkin(monocleSkin);
        skin.addSkin(topHatSkin);
        skeleton.setSkin(skin);
        
        animationState.addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                if (entry.getTrackIndex() == 0 && entry.getAnimation().getName().startsWith("say")) {
                    gameScreen.increaseMode();
                    animationState.setAnimation(0, standAnimation, false);
                }
            }
    
            @Override
            public void event(AnimationState.TrackEntry entry, Event event) {
                switch (event.getData().getName()) {
                    case "01":
                        Sound sound = core.assetManager.get("sfx/01.mp3");
                        sound.play();
                        break;
                    case "02":
                        sound = core.assetManager.get("sfx/02.mp3");
                        sound.play();
                        break;
                    case "03":
                        sound = core.assetManager.get("sfx/03.mp3");
                        sound.play();
                        break;
                    case "04":
                        sound = core.assetManager.get("sfx/04.mp3");
                        sound.play();
                        break;
                    case "05":
                        sound = core.assetManager.get("sfx/05.mp3");
                        sound.play();
                        break;
                    case "06":
                        sound = core.assetManager.get("sfx/06.mp3");
                        sound.play();
                        break;
                }
            }
        });
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
    
    }
    
    @Override
    public void draw(float delta) {
    
    }
    
    @Override
    public void destroy() {
    
    }
}

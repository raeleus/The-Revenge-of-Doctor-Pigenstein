package com.ray3k.template.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.Skin;
import com.ray3k.template.Core;
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
    public static Skin glassesSkin;
    public static Skin monocleSkin;
    public static Skin sunglassesSkin;
    public static Skin techMonocleSkin;
    public static Skin farmerHatSkin;
    public static Skin hatSkin;
    public static Skin headphonesSkin;
    public static Skin headsetSkin;
    public static Skin partyHatSkin;
    public static Skin pigTailsSkin;
    public static Skin sunHatSkin;
    public static Skin topHatSkin;
    public static Skin camoflageSkin;
    public static Skin eyeBlackSkin;
    public static Skin makeupSkin;
    public static Skin noseRingSkin;
    public static Skin beardSkin;
    public static Skin mustacheSkin;
    public static Skin mustacheCurlySkin;
    public static Array<Skin> glassesSkins;
    public static Array<Skin> hatSkins;
    public static Array<Skin> makeupSkins;
    public static Array<Skin> mustacheSkins;
    private boolean crying;
    private float runSpeed;
    private ParticleEntity tossParticleEntity;
    
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
            
            glassesSkins = new Array<>();
            glassesSkin = skeletonData.findSkin("glasses/glasses");
            glassesSkins.add(glassesSkin);
            monocleSkin = skeletonData.findSkin("glasses/monocle");
            glassesSkins.add(monocleSkin);
            sunglassesSkin = skeletonData.findSkin("glasses/sunglasses");
            glassesSkins.add(sunglassesSkin);
            techMonocleSkin = skeletonData.findSkin("glasses/tech-monocle");
            glassesSkins.add(techMonocleSkin);
            
            hatSkins = new Array<>();
            farmerHatSkin = skeletonData.findSkin("hat/farmer-hat");
            hatSkins.add(farmerHatSkin);
            hatSkin = skeletonData.findSkin("hat/hat");
            hatSkins.add(hatSkin);
            headphonesSkin = skeletonData.findSkin("hat/headphones");
            hatSkins.add(headphonesSkin);
            headsetSkin = skeletonData.findSkin("hat/headset");
            hatSkins.add(headsetSkin);
            partyHatSkin = skeletonData.findSkin("hat/party-hat");
            hatSkins.add(partyHatSkin);
            pigTailsSkin = skeletonData.findSkin("hat/pig-tails");
            hatSkins.add(pigTailsSkin);
            sunHatSkin = skeletonData.findSkin("hat/sun-hat");
            hatSkins.add(sunHatSkin);
            topHatSkin = skeletonData.findSkin("hat/top-hat");
            hatSkins.add(topHatSkin);
            
            makeupSkins = new Array<>();
            camoflageSkin = skeletonData.findSkin("makeup/camoflage");
            makeupSkins.add(camoflageSkin);
            eyeBlackSkin = skeletonData.findSkin("makeup/eye-black");
            makeupSkins.add(eyeBlackSkin);
            makeupSkin = skeletonData.findSkin("makeup/makeup");
            makeupSkins.add(makeupSkin);
            noseRingSkin = skeletonData.findSkin("makeup/nose-ring");
            makeupSkins.add(noseRingSkin);
            
            mustacheSkins = new Array<>();
            beardSkin = skeletonData.findSkin("mustache/beard");
            mustacheSkins.add(beardSkin);
            mustacheSkin = skeletonData.findSkin("mustache/mustache");
            mustacheSkins.add(mustacheSkin);
            mustacheCurlySkin = skeletonData.findSkin("mustache/mustache-curly");
            mustacheSkins.add(mustacheCurlySkin);
            
            animationStateData.setMix(standAnimation,  walkAnimation, .25f);
        }
        
        setSkeletonData(skeletonData, animationStateData);
        animationState.setAnimation(0, walkAnimation, true);
        animationState.setAnimation(1, smileAnimation, true);
        animationState.addAnimation(2, blinkAnimation, true, MathUtils.random(3f));
        crying = true;
        runSpeed = MathUtils.random(200, 500);
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
        if (y < 376) {
            float score = getSpeed();
            y = 376;
            gravityY = 0;
            deltaX = 0;
            deltaY = 0;
            if (crying) {
                beginWalking();
            }
    
            if (tossParticleEntity != null) {
                tossParticleEntity.destroy = true;
                tossParticleEntity = null;
            }
            
            if (x > 1475 && x < 2775) {
                crying = false;
                animationState.setAnimation(0, standAnimation, true);
                deltaX = 0;
                gameScreen.addScore((int) (score / 100));
                
                ParticleEntity particleEntity = new ParticleEntity(core.mudParticleEffect);
                particleEntity.setPosition(x, y);
                particleEntity.depth = GameScreen.MUD_DEPTH;
                particleEntity.particleEffect.scaleEffect(score / 2000);
                gameScreen.entityController.add(particleEntity);
            }
        } else if (y > 2290) {
            y = 2290;
            deltaY = -Math.abs(deltaY);
        }
        
        if (gameScreen.grabbedPig == null && crying) {
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
                
                if (tossParticleEntity == null) {
                    tossParticleEntity = new ParticleEntity(core.tossParticleEffect);
                    tossParticleEntity.depth = GameScreen.PARTICLE_DEPTH;
                    gameScreen.entityController.add(tossParticleEntity);
                }
            }
        }
        
        if (gameScreen.grabbedPig != this && (skeletonBounds.getMinX() > 4273 || skeletonBounds.getMaxX() < 0)) {
            destroy = true;
            if (tossParticleEntity != null) {
                tossParticleEntity.destroy = true;
                tossParticleEntity = null;
            }
        }
        
        if (tossParticleEntity != null) {
            tossParticleEntity.setPosition(skeletonBounds.getMinX() + skeletonBounds.getWidth() / 2, skeletonBounds.getMinY() + skeletonBounds.getHeight() / 2.0f);
        }
    }
    
    public void beginWalking() {
        if (x < 2070) {
            deltaX = -runSpeed;
            if (skeleton.getScaleX() != -1) {
                animationState.setAnimation(0, standAnimation, false);
                animationState.addAnimation(0, walkAnimation, true, 0);
            }
            skeleton.setScaleX(-1);
        } else {
            deltaX = runSpeed;
            if (skeleton.getScaleX() != 1) {
                animationState.setAnimation(0, standAnimation, false);
                animationState.addAnimation(0, walkAnimation, true, 0);
            }
            skeleton.setScaleX(1);
        }
    }
    
    @Override
    public void draw(float delta) {
    
    }
    
    @Override
    public void destroy() {
    
    }
}

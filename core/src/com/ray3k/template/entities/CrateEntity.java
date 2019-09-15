package com.ray3k.template.entities;

import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.ray3k.template.screens.GameScreen;

public class CrateEntity extends Entity {
    private Animation animation;
    private static SkeletonData skeletonData;
    private static AnimationStateData animationStateData;
    
    @Override
    public void create() {
        depth = GameScreen.CRATE_DEPTH;
        
        if (skeletonData == null) {
            skeletonData = core.crateSkeletonData;
            animationStateData = core.crateAnimationStateData;
            
            animation = skeletonData.findAnimation("animation");
        }
    
        setSkeletonData(skeletonData, animationStateData);
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

package com.ray3k.template.entities;

import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;

public class PlatformMudEntity extends Entity implements ground {
    private SkeletonData skeletonData;
    private AnimationStateData animationStateData;
    private static Animation animation;
    
    @Override
    public void create() {
        if (skeletonData == null) {
            skeletonData = core.platformMudSkeletonData;
            animationStateData = core.platformMudAnimationStateData;
            
            animation = skeletonData.findAnimation("animation");
        }
        
        setSkeletonData(skeletonData, animationStateData);
        animationState.setAnimation(0, animation, false);
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
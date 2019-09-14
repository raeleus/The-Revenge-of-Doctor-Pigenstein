package com.ray3k.template.entities;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.ray3k.template.Core;

public class CursorActor extends SkeletonActor {
    private Animation pointAnimation;
    private Animation grabAnimation;
    private Animation overAnimation;
    
    public CursorActor() {
        super(Core.core.skeletonRenderer, new Skeleton(Core.core.handSkeletonData), new AnimationState(Core.core.handAnimationStateData));
        pointAnimation = getSkeleton().getData().findAnimation("point");
        grabAnimation = getSkeleton().getData().findAnimation("grab");
        overAnimation = getSkeleton().getData().findAnimation("over");
        getAnimationState().setAnimation(0, pointAnimation, true);
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }
}

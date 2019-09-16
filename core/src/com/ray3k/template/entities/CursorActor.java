package com.ray3k.template.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.ray3k.template.Core;
import com.ray3k.template.screens.GameScreen;

public class CursorActor extends SkeletonActor {
    private Animation pointAnimation;
    private Animation grabAnimation;
    private Animation overAnimation;
    private static final Vector2 temp = new Vector2();
    
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
        temp.set(Gdx.input.getX(), Gdx.input.getY());
        GameScreen.gameScreen.stage.getViewport().unproject(temp);
        setPosition(temp.x, temp.y);
    }
}

package com.ray3k.template.entities;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.ray3k.template.screens.GameScreen;

public class ParticleEntity extends Entity {
    public ParticleEffect particleEffect;
    
    public ParticleEntity(ParticleEffect templateEffect) {
        particleEffect = new ParticleEffect(templateEffect);
    }
    
    @Override
    public void create() {
    
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
        setPosition(GameScreen.mouseX, GameScreen.mouseY);
        
        particleEffect.setPosition(x, y);
        particleEffect.update(delta);
    }
    
    @Override
    public void draw(float delta) {
        particleEffect.setPosition(x + deltaX * delta, y + deltaY * delta);
        particleEffect.draw(core.batch);
    }
    
    @Override
    public void destroy() {
    
    }
}

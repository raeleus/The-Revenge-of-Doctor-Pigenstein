package com.ray3k.template.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.template.Core;
import com.ray3k.template.JamScreen;
import com.ray3k.template.entities.EntityController;
import com.ray3k.template.entities.ParticleEntity;

public class GameScreen extends JamScreen {
    private Action action;
    public EntityController entityController;
    public static FitViewport gameViewport;
    public Stage stage;
    private Skin skin;
    private Core core;
    private Table root;
    private final static Color BG_COLOR = new Color(Color.WHITE);
    
    public GameScreen(Action action) {
        this.action = action;
    }
    
    @Override
    public void show() {
        entityController = new EntityController();
        gameViewport = new FitViewport(1024, 576);
        core = Core.core;
        skin = core.skin;
    
        stage = new Stage(new ScreenViewport(), core.batch);
        Gdx.input.setInputProcessor(stage);
    
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
    
        ParticleEntity entity = new ParticleEntity(core.tossParticleEffect);
        entity.setPosition(200, 200);
        entityController.add(entity);
    }
    
    @Override
    public void act(float delta) {
        stage.act(delta);
        entityController.act(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            core.setScreen(new GameScreen(action));
        }
    }
    
    @Override
    public void draw(float delta) {
        Gdx.gl.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
        gameViewport.apply(true);
        core.batch.setProjectionMatrix(gameViewport.getCamera().combined);
        core.batch.begin();
        core.batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        entityController.draw(delta);
        core.batch.end();
    
        core.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        gameViewport.update(width, height, true);
    }
}

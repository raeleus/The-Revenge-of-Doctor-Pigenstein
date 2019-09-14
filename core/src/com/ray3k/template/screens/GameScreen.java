package com.ray3k.template.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.template.Core;
import com.ray3k.template.JamScreen;
import com.ray3k.template.entities.*;

public class GameScreen extends JamScreen implements InputProcessor {
    private Action action;
    public EntityController entityController;
    public static Viewport gameViewport;
    public static OrthographicCamera gameCamera;
    public Stage stage;
    private Skin skin;
    private Core core;
    private Table root;
    private final static Color BG_COLOR = new Color(Color.WHITE);
    public static GameScreen gameScreen;
    public static float mouseX;
    public static float mouseY;
    private final static Vector3 tempVector3 = new Vector3();
    private static IntArray keysJustPressed = new IntArray();
    private static IntArray buttonsJustPressed = new IntArray();
    
    public GameScreen(Action action) {
        this.action = action;
    }
    
    @Override
    public void show() {
        gameScreen = this;
        
        keysJustPressed.clear();
        buttonsJustPressed.clear();
        
        entityController = new EntityController();
        gameCamera = new OrthographicCamera();
        gameViewport = new ExtendViewport(1024, 576, gameCamera);
        gameCamera.zoom = 3f;
        gameViewport.update(Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        gameCamera.position.set(474 * 3.5f, gameViewport.getWorldHeight() * gameCamera.zoom / 2,  0);
        core = Core.core;
        skin = core.skin;
    
        Gdx.graphics.setCursor(core.invisibleCursor);
        
        stage = new Stage(new ScreenViewport(), core.batch);
        stage.addActor(new CursorActor());
    
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
    
        ParticleEntity entity = new ParticleEntity(core.tossParticleEffect);
        entity.setPosition(200, 200);
        entityController.add(entity);
    
        PigEntity pigEntity = new PigEntity();
        pigEntity.setPosition(500, 200);
        entityController.add(pigEntity);
    
        PlatformEntity platformEntity = new PlatformEntity();
        platformEntity.setPosition(0, 390);
        entityController.add(platformEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474, 390);
        entityController.add(platformEntity);
    
        PlatformMudLeftEntity platformMudLeftEntity = new PlatformMudLeftEntity();
        platformMudLeftEntity.setPosition(474 * 2, 390);
        entityController.add(platformMudLeftEntity);
    
        PlatformMudEntity platformMudEntity = new PlatformMudEntity();
        platformMudEntity.setPosition(474 * 3, 390);
        entityController.add(platformMudEntity);
    
        PlatformMudRightEntity platformMudRightEntity = new PlatformMudRightEntity();
        platformMudRightEntity.setPosition(474 * 4, 390);
        entityController.add(platformMudRightEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474 * 5, 390);
        entityController.add(platformEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474 * 6, 390);
        entityController.add(platformEntity);
    }
    
    @Override
    public void act(float delta) {
        tempVector3.x = Gdx.input.getX();
        tempVector3.y = Gdx.input.getY();
        gameViewport.unproject(tempVector3);
        mouseX = tempVector3.x;
        mouseY = tempVector3.y;
        
        stage.act(delta);
        entityController.act(delta);
        if (isKeyJustPressed(Input.Keys.F5)) {
            core.setScreen(new GameScreen(action));
        }
        
        keysJustPressed.clear();
        buttonsJustPressed.clear();
    }
    
    @Override
    public void draw(float delta) {
        Gdx.gl.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
        gameViewport.apply(false);
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
        gameViewport.update(width, height, false);
    }
    
    @Override
    public boolean keyDown(int keycode) {
        keysJustPressed.add(keycode);
        return false;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        buttonsJustPressed.add(button);
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    public static boolean isKeyJustPressed(int key) {
        return key == Input.Keys.ANY_KEY ? keysJustPressed.size > 0 : keysJustPressed.contains(key);
    }
    
    /**
     * Returns true if the associated mouse button has been pressed since the last step.
     * @param button The button value or -1 for any button
     * @return
     */
    public static boolean isButtonJustPressed(int button) {
        return button == -1 ? buttonsJustPressed.size > 0 : buttonsJustPressed.contains(button);
    }
    
    public static boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }
    
    public static boolean isButtonPressed(int button) {
        if (button == -1) {
            return Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)
                    || Gdx.input.isButtonPressed(Input.Buttons.MIDDLE) || Gdx.input.isButtonPressed(Input.Buttons.BACK)
                    || Gdx.input.isButtonPressed(Input.Buttons.FORWARD);
        } else {
            return Gdx.input.isButtonPressed(button);
        }
    }
}

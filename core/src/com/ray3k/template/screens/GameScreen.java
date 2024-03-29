package com.ray3k.template.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.viewport.*;
import com.ray3k.template.Core;
import com.ray3k.template.JamScreen;
import com.ray3k.template.Utils;
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
    private final static Color BG_COLOR = new Color(134 / 255f, 201 / 255f, 224 / 255f, 1f);
    public static GameScreen gameScreen;
    public static float mouseX;
    public static float mouseY;
    private final static Vector3 tempVector3 = new Vector3();
    private static IntArray keysJustPressed = new IntArray();
    private static IntArray buttonsJustPressed = new IntArray();
    private static IntArray buttonsPressed = new IntArray();
    public static int PIG_DEPTH = 100;
    public static int BACKGROUND_DEPTH = 10;
    public static int PARTICLE_DEPTH = 101;
    public static int MUD_DEPTH = 0;
    public static int CRATE_DEPTH = 1010;
    public static int DOCTOR_DEPTH = 1020;
    public PigEntity grabbedPig;
    public float grabbedPigOffsetX;
    public float grabbedPigOffsetY;
    public float pigDeltaX;
    public float pigDeltaY;
    private GestureDetector gestureDetector;
    private Array<PlatformEntity> platforms;
    private Array<PigEntity> pigEntities;
    public int score;
    public static int mode;
    public DoctorEntity doctorEntity;
    
    public GameScreen(Action action) {
        this.action = action;
    }
    
    @Override
    public void show() {
        mode = 0;
        gameScreen = this;
        
        keysJustPressed.clear();
        buttonsJustPressed.clear();
        
        entityController = new EntityController();
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(1024, 576, gameCamera);
        gameCamera.zoom = 4f;
        gameViewport.update(Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        gameCamera.position.set(474 * 4.5f, gameViewport.getWorldHeight() * gameCamera.zoom / 2,  0);
        core = Core.core;
        skin = core.skin;
        
        platforms = new Array<>();
    
        Gdx.graphics.setCursor(core.invisibleCursor);
        
        stage = new Stage(new ExtendViewport(1024, 576), core.batch);
    
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
    
        Label label = new Label("0", skin, "black");
        label.setName("score");
        root.add(label).expandY().top();
        
        showIntroDialog();
        
        stage.addActor(new CursorActor());
    
        gestureDetector = new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                pigDeltaX = velocityX;
                pigDeltaY = -velocityY;
                return false;
            }
        });
        
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(inputMultiplexer);
    
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
    
        pigEntities = new Array<>();
        
        for (int i = 0; i < 10; i++) {
            PigEntity pigEntity = new PigEntity();
            pigEntity.setPosition(MathUtils.random(1475, 2775), 376);
            entityController.add(pigEntity);
            pigEntities.add(pigEntity);
            pigEntity.skeleton.setSkin(new com.esotericsoftware.spine.Skin("new-skin" + i));
        }
        
        pigEntities.shuffle();
        for (int i = 0; i < 2; i++) {
            com.esotericsoftware.spine.Skin pigSkin = pigEntities.get(i).skeleton.getSkin();
            pigSkin.addSkin(PigEntity.mustacheSkins.random());
            pigEntities.get(i).skeleton.setSkin((com.esotericsoftware.spine.Skin) null);
            pigEntities.get(i).skeleton.setSkin(pigSkin);
            pigEntities.get(i).wearingMustache = true;
        }
    
        pigEntities.shuffle();
        for (int i = 0; i < 4; i++) {
            com.esotericsoftware.spine.Skin pigSkin = pigEntities.get(i).skeleton.getSkin();
            pigSkin.addSkin(PigEntity.glassesSkins.random());
            pigEntities.get(i).skeleton.setSkin((com.esotericsoftware.spine.Skin) null);
            pigEntities.get(i).skeleton.setSkin(pigSkin);
            pigEntities.get(i).wearingGlasses = true;
        }
    
        pigEntities.shuffle();
        for (int i = 0; i < 6; i++) {
            com.esotericsoftware.spine.Skin pigSkin = pigEntities.get(i).skeleton.getSkin();
            pigSkin.addSkin(PigEntity.makeupSkins.random());
            pigEntities.get(i).skeleton.setSkin((com.esotericsoftware.spine.Skin) null);
            pigEntities.get(i).skeleton.setSkin(pigSkin);
            pigEntities.get(i).wearingMakeup = true;
        }
    
        pigEntities.shuffle();
        for (int i = 0; i < 8; i++) {
            com.esotericsoftware.spine.Skin pigSkin = pigEntities.get(i).skeleton.getSkin();
            pigSkin.addSkin(PigEntity.hatSkins.random());
            pigEntities.get(i).skeleton.setSkin((com.esotericsoftware.spine.Skin) null);
            pigEntities.get(i).skeleton.setSkin(pigSkin);
            pigEntities.get(i).wearingHat = true;
        }
    
        PlatformEntity platformEntity = new PlatformEntity();
        platformEntity.setPosition(0, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474 * 2, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformMudLeftEntity();
        platformEntity.setPosition(474 * 3, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformMudEntity();
        platformEntity.setPosition(474 * 4, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformMudRightEntity();
        platformEntity.setPosition(474 * 5, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474 * 6, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474 * 7, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
    
        platformEntity = new PlatformEntity();
        platformEntity.setPosition(474 * 8, 390);
        entityController.add(platformEntity);
        platforms.add(platformEntity);
        
        CrateEntity crateEntity = new CrateEntity();
        crateEntity.setPosition(1493, 380);
        entityController.add(crateEntity);
        
        doctorEntity = new DoctorEntity();
        doctorEntity.setPosition(1493, 620);
        entityController.add(doctorEntity);
    }
    
    @Override
    public void act(float delta) {
        tempVector3.x = Gdx.input.getX();
        tempVector3.y = Gdx.input.getY();
        gameViewport.unproject(tempVector3);
        mouseX = tempVector3.x;
        mouseY = tempVector3.y;
        
        if (grabbedPig != null) {
            grabbedPig.setPosition(mouseX + grabbedPigOffsetX, mouseY + grabbedPigOffsetY);
        }
        
        boolean increase = true;
        if (mode == 2 || mode == 4 || mode == 6 || mode == 8 || mode == 10) {
            for (PigEntity pigEntity : pigEntities) {
                if (!pigEntity.destroy && pigEntity.crying) {
                    increase = false;
                    break;
                }
            }
            if (increase) increaseMode();
        }
        
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
        stage.getViewport().apply();
        core.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        stage.draw();
    }
    
    public void showIntroDialog() {
        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                increaseMode();
            }
        };
        dialog.getContentTable().pad(20);
        Label label = new Label("Doctor Pigenstein is not being nice!\nHe won't let the other piggies have fun in the mud puddle.\nCan you toss the piggies back in?\nThe bigger the splash, the happier the pig!", skin);
        label.setAlignment(Align.center);
        dialog.text(label);
        TextButton textButton = new TextButton("Begin", skin);
        textButton.addListener(core.sndChangeListener);
        dialog.button(textButton, null);
        dialog.show(stage);
    }
    
    public void showEndDialog() {
        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    core.setScreen(core.createGameScreen());
                } else {
                    core.setScreen(core.createMenuScreen());
                }
            }
        };
        dialog.getContentTable().pad(20);
        Label label = new Label("Final Score: " + score + "\n" +
                "Doctor Pigenstein is very sorry about being mean." +
                "\nThanks for helping the piggy friends!", skin);
        label.setAlignment(Align.center);
        dialog.text(label);
    
        TextButton textButton = new TextButton("Play Again", skin);
        textButton.addListener(core.sndChangeListener);
        dialog.button(textButton, true);
    
        textButton = new TextButton("Back to Menu", skin);
        textButton.addListener(core.sndChangeListener);
        dialog.button(textButton, false);
        dialog.show(stage);
    }
    
    public void increaseMode() {
        mode++;
        
        switch (mode) {
            case 1:
                doctorEntity.animationState.setAnimation(0, "say_01", false);
                break;
            case 2:
                Sound sound = core.assetManager.get("sfx/cry.mp3");
                sound.play();
                
                for (PigEntity pigEntity : pigEntities) {
                    if (!pigEntity.wearingHat) {
                        pigEntity.crying = true;
                        pigEntity.beginWalking();
                    }
                }
                break;
            case 3:
                doctorEntity.animationState.setAnimation(0, "say_02", false);
                break;
            case 4:
                sound = core.assetManager.get("sfx/cry.mp3");
                sound.play();
                
                for (PigEntity pigEntity : pigEntities) {
                    if (!pigEntity.wearingMakeup) {
                        pigEntity.crying = true;
                        pigEntity.beginWalking();
                    }
                }
                break;
            case 5:
                doctorEntity.animationState.setAnimation(0, "say_03", false);
                break;
            case 6:
                sound = core.assetManager.get("sfx/cry.mp3");
                sound.play();
                
                for (PigEntity pigEntity : pigEntities) {
                    if (!pigEntity.wearingGlasses) {
                        pigEntity.crying = true;
                        pigEntity.beginWalking();
                    }
                }
                break;
            case 7:
                doctorEntity.animationState.setAnimation(0, "say_04", false);
                break;
            case 8:
                sound = core.assetManager.get("sfx/cry.mp3");
                sound.play();
                
                for (PigEntity pigEntity : pigEntities) {
                    if (!pigEntity.wearingMustache) {
                        pigEntity.crying = true;
                        pigEntity.beginWalking();
                    }
                }
                break;
            case 9:
                doctorEntity.animationState.setAnimation(0, "say_05", false);
                break;
            case 10:
                sound = core.assetManager.get("sfx/cry.mp3");
                sound.play();
                
                for (PigEntity pigEntity : pigEntities) {
                    pigEntity.crying = true;
                    pigEntity.beginWalking();
                }
                break;
            case 11:
                doctorEntity.animationState.setAnimation(0, "say_06", false);
                break;
            case 12:
                showEndDialog();
                break;
        }
    }
    
    public void addScore(int score) {
        this.score += score;
        
        Label label = stage.getRoot().findActor("score");
        label.setText(this.score);
    }
    
    /**todo: add all the key/button logging to base Jam Screen.**/
    
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
        
        buttonsPressed.add(button);
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        buttonsPressed.removeValue(button);
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
            return buttonsPressed.contains(Input.Buttons.LEFT) || buttonsPressed.contains(Input.Buttons.RIGHT)
                    || buttonsPressed.contains(Input.Buttons.MIDDLE) || buttonsPressed.contains(Input.Buttons.BACK)
                    || buttonsPressed.contains(Input.Buttons.FORWARD);
        } else {
            return buttonsPressed.contains(button);
        }
    }
}

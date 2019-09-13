package com.ray3k.template;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.crashinvaders.vfx.VfxManager;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ray3k.template.screens.*;

public class Core extends JamGame {
    private static final int MAX_VERTEX_SIZE = 32767;
    public static Core core;
    public TwoColorPolygonBatch batch;
    public Skin skin;
    public SkeletonRenderer skeletonRenderer;
    public Sound sndClick;
    public ChangeListener sndChangeListener;
    public VfxManager vfxManager;
    public TextureAtlas textureAtlas;
    public SkeletonData handSkeletonData;
    public SkeletonData pigSkeletonData;
    public SkeletonData platformSkeletonData;
    public SkeletonData platformMudSkeletonData;
    public SkeletonData platformMudLeftSkeletonData;
    public SkeletonData platformMudRightSkeletonData;
    public ParticleEffect mudParticleEffect;
    public ParticleEffect tossParticleEffect;
    
    @Override
    public void create() {
        super.create();
        core = this;
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        
        batch = new TwoColorPolygonBatch(MAX_VERTEX_SIZE);
        sndChangeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                core.sndClick.play();
            }
        };
    
        setScreen(createLoadScreen());
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        
        vfxManager.dispose();
        
        super.dispose();
    }
    
    @Override
    public void loadAssets() {
        assetManager.load("ui/ui.json", Skin.class);
    
        assetManager.setLoader(SkeletonData.class, new SkeletonDataLoader(assetManager.getFileHandleResolver()));
        SkeletonDataLoader.SkeletonDataLoaderParameter parameter = new SkeletonDataLoader.SkeletonDataLoaderParameter("spine/The Revenge of Doctor Pigenstein.atlas");
        assetManager.load("spine/hand.json", SkeletonData.class, parameter);
        assetManager.load("spine/pig.json", SkeletonData.class, parameter);
        assetManager.load("spine/platform.json", SkeletonData.class, parameter);
        assetManager.load("spine/platform-mud.json", SkeletonData.class, parameter);
        assetManager.load("spine/platform-mud-left.json", SkeletonData.class, parameter);
        assetManager.load("spine/platform-mud-right.json", SkeletonData.class, parameter);
    
        ParticleEffectLoader.ParticleEffectParameter particleEffectParameter = new ParticleEffectLoader.ParticleEffectParameter();
        particleEffectParameter.atlasFile = "spine/The Revenge of Doctor Pigenstein.atlas";
        assetManager.load("particles/toss.p", ParticleEffect.class, particleEffectParameter);
        assetManager.load("particles/mud.p", ParticleEffect.class, particleEffectParameter);
        
        assetManager.load("sfx/click.mp3", Sound.class);
        
        assetManager.load("sfx/audio-test.mp3", Music.class);
        assetManager.load("bgm/music-test.mp3", Music.class);
    }
    
    private Screen createLoadScreen() {
        return new LoadScreen(Actions.run(() -> {
            skin = assetManager.get("ui/ui.json");
            sndClick = assetManager.get("sfx/click.mp3");
            textureAtlas = assetManager.get("spine/The Revenge of Doctor Pigenstein.atlas");
            
            handSkeletonData = assetManager.get("spine/hand.json");
            pigSkeletonData = assetManager.get("spine/pig.json");
            platformSkeletonData = assetManager.get("spine/platform.json");
            platformMudSkeletonData = assetManager.get("spine/platform-mud.json");
            platformMudLeftSkeletonData = assetManager.get("spine/platform-mud-left.json");
            platformMudRightSkeletonData = assetManager.get("spine/platform-mud-right.json");
            
            mudParticleEffect = assetManager.get("particles/mud.p");
            tossParticleEffect = assetManager.get("particles/toss.p");
            
            setScreen(createSplashScreen());
        }));
    }
    
    private Screen createSplashScreen() {
        return new SplashScreen(Actions.run(() -> setScreen(createMenuScreen())));
    }
    
    private Screen createMenuScreen() {
        return new MenuScreen(Actions.run(() -> setScreen(createGameScreen())),
                Actions.run(() -> setScreen(createOptionsScreen())),
                Actions.run(() -> setScreen(createCreditsScreen())));
    }
    
    private Screen createGameScreen() {
        return new GameScreen(Actions.run(() -> setScreen(createGameOverScreen())));
    }
    
    private Screen createGameOverScreen() {
        return new GameOverScreen(Actions.run(() -> setScreen(createMenuScreen())));
    }
    
    private Screen createOptionsScreen() {
        return new OptionsScreen(Actions.run(() -> setScreen(createMenuScreen())));
    }
    
    private Screen createCreditsScreen() {
        return new CreditsScreen(Actions.run(() -> setScreen(createMenuScreen())));
    }
}

package com.mfp.td.activities;

import java.util.HashMap;

import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;
import android.view.KeyEvent;
import android.widget.Toast;

import com.mfp.td.buttons.ButtonFactory;
import com.mfp.td.buttons.ButtonRegion;
import com.mfp.td.gui.GameGUI;
import com.mfp.td.monsters.Monster.MonsterType;
import com.mfp.td.monsters.MonsterFactory;
import com.mfp.td.scenes.Level;
import com.mfp.td.towers.TowerFactory;

/**
 * 
 * @author M-F.P
 */
public class Game extends SimpleBaseGameActivity implements IOnMenuItemClickListener {

    // ===========================================================
    // Constants
    // ===========================================================
    
    protected static final int MENU_RESET = 0;
    protected static final int MENU_QUIT = MENU_RESET + 1;
    
    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 320;
    
    public static final String TAG = "LevelOne";
    
    // ===========================================================
    // Fields
    // ===========================================================
    
    private Level mLevel;
    private GameGUI mGUI;
    private SmoothCamera mCamera;
    protected MenuScene mMenuScene;
    
    private ButtonFactory mButtonFactory;
    private TowerFactory mTowerFactory;
    private MonsterFactory mMonsterFactory;

    // ===========================================================
    // Constructors
    // ===========================================================
    
    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    public ButtonFactory getButtonFactory() {
    	return this.mButtonFactory;
    }
    
    public Level getLevel() {
    	return this.mLevel;
    }
    
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    
    /*
     * Permet de rajouter des view Android dans la RenderSurfaceView d'andengine
     *
    @Override
    protected void onSetContentView() {
        
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            final FrameLayout.LayoutParams relativeLayoutLayoutParams = 
                    new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

            Button button = new Button(this);
            button.setText("blabla");         

            this.mRenderSurfaceView = new RenderSurfaceView(this);
            this.mRenderSurfaceView.setEGLConfigChooser(false);
            this.mRenderSurfaceView.setRenderer(this.mEngine);

            final LayoutParams surfaceViewLayoutParams = new RelativeLayout.LayoutParams(super.createSurfaceViewLayoutParams());
            surfaceViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            relativeLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
            relativeLayout.addView(button);

            this.setContentView(relativeLayout, relativeLayoutLayoutParams);
    }
    */
    
    @Override
    public EngineOptions onCreateEngineOptions() {

        this.mCamera = new SmoothCamera(0, 0, 
                CAMERA_WIDTH, CAMERA_HEIGHT, 2048, 2048, 2);
        
        this.mLevel = new Level(this.mCamera);
        
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);

        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        
        if(MultiTouch.isSupported(this)) {
                if(MultiTouch.isSupportedDistinct(this)) {
                        Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
                } else {
                        Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
                }
        } else {
                Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
        }

        return engineOptions;
    }
    
    @Override
    public void onCreateResources() {

        SoundFactory.setAssetBasePath("mfx/");

        this.mLevel.createEvents(this, "level1.lvl");
        
        final HashMap<MonsterType, Integer> levelMonstersAmounts = 
        		this.mLevel.getLevelEvents().getLevelMonstersAmounts();
        
        this.mButtonFactory = new ButtonFactory(this.mEngine, this);
        this.mTowerFactory = new TowerFactory(this.mEngine, this, this.mButtonFactory);
        this.mMonsterFactory = new MonsterFactory(this.mEngine, this, levelMonstersAmounts);
        
        this.mGUI = new GameGUI(this.mLevel, this.mEngine, this); 
        this.mGUI.build(this, this.mEngine, this.mButtonFactory);
        
        this.mCamera.setHUD(this.mGUI);
    }

    @Override
    public Scene onCreateScene() {

        this.createMenuScene();
        
        final FPSCounter fpsCounter = new FPSCounter();
        this.mEngine.registerUpdateHandler(fpsCounter);
        this.mGUI.activateFps(fpsCounter);
        
        this.mLevel.createScene(this.mEngine, this, 
        		this.mTowerFactory, 
        		this.mMonsterFactory);
        
        this.mLevel.getLevelEvents().start();
        
        return this.mLevel;
    }
    
    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
        if (pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
            pauseGame();
            return true;
        } else {
            return super.onKeyDown(pKeyCode, pEvent);
        }
    }

    @Override
    public boolean onMenuItemClicked(final MenuScene pMenuScene, 
	    final IMenuItem pMenuItem, 
	    final float pMenuItemLocalX, 
	    final float pMenuItemLocalY) {
	
        switch (pMenuItem.getID()) {
            case MENU_RESET:
                /* Restart the animation. */
                this.mLevel.reset();

                /* Remove the menu and reset it. */
                this.mLevel.clearChildScene();
                this.mMenuScene.reset();
                return true;
            case MENU_QUIT:
                /* End Activity. */
                this.finish();
                return true;
            default:
                return false;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================
    
    public void pauseGame() {
        
        if (this.mLevel.hasChildScene()) {
            /* Remove the menu and reset it. */
            this.mGUI.setLock(false);
            this.mMenuScene.back();
        } else {
            /* Attach the menu. */
            this.mGUI.setLock(true);
            this.mLevel.setChildScene(this.mMenuScene, false, true, true);
        }
    }
    
    protected void createMenuScene() {
        
        this.mMenuScene = new MenuScene(this.mCamera);
        
        final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET, 
                this.mButtonFactory.getRegion(ButtonRegion.MENU_RESET), 
                this.getVertexBufferObjectManager());
        resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, 
                this.mButtonFactory.getRegion(ButtonRegion.MENU_QUIT), 
                this.getVertexBufferObjectManager());
        quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        
        this.mMenuScene.addMenuItem(resetMenuItem);
        this.mMenuScene.addMenuItem(quitMenuItem);
        
        this.mMenuScene.buildAnimations();
        
        this.mMenuScene.setBackgroundEnabled(false);
        this.mMenuScene.setOnMenuItemClickListener(this);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
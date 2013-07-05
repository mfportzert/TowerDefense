/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.gui;

import java.util.ArrayList;
import java.util.Properties;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.mfp.td.activities.Game;
import com.mfp.td.buttons.Button;
import com.mfp.td.buttons.ButtonFactory;
import com.mfp.td.buttons.ButtonRegion;
import com.mfp.td.buttons.ClickableButton;
import com.mfp.td.buttons.DraggableTowerButton;
import com.mfp.td.scenes.Level;
import com.mfp.td.towers.Tower.TowerType;
import com.mfp.td.utils.IndexPropertyConstants;
import com.mfp.td.utils.PropertiesUtils;

/**
 *
 * @author M-F.P
 */
public class GameGUI extends HUD implements IOnSceneTouchListener {
    
    // ===========================================================
    // Constants
    // ===========================================================

    private final static int FONT_SIZE = 20;
    
    // ===========================================================
    // Fields
    // ===========================================================
    
    private Level mLevel;
    private boolean mLocked;
    
    private Font mDefaultFont;
    private Font mNeverwinterNightsFont;
    
    private BitmapTextureAtlas mFontTexture;
    private BitmapTextureAtlas mNeverwinterNightsFontTexture;
    
    private final Text mFpsText;
    private final Text mWaveNumberText;
    private final Text mNextWaveText;
    private final Text mMoneyText;
    private final Text mLifeText;
    
    private final Sprite mMoneyIcon;
    private final Sprite mLifeIcon;
    
    private final ArrayList<ClickableButton> mClickableButtons = new ArrayList<ClickableButton>();
    private final ArrayList<DraggableTowerButton> mDraggableTowerButtons = new ArrayList<DraggableTowerButton>();
    
    // ===========================================================
    // Constructors
    // ===========================================================
    
    /**
     * 
     * @param pLevel
     * @param pEngine
     * @param pContext
     */
    public GameGUI(final Level pLevel, final Engine pEngine, 
	    final Context pContext) {
        
        this.loadFonts(pEngine, pContext);
        
        this.mLevel = pLevel;
        this.mLocked = false;
        
        //  TODO: Utiliser les valeurs en pourcentage
        this.mFpsText = new Text(0, 30, this.mDefaultFont, "FPS:", 
        	"FPS: XXXXXXXXXXX".length(), pEngine.getVertexBufferObjectManager());
        this.mWaveNumberText = new Text(200, 0, this.mNeverwinterNightsFont, "Wave ", 
        	"Wave XX".length(), pEngine.getVertexBufferObjectManager());
        this.mNextWaveText = new Text(0, 0, this.mNeverwinterNightsFont, "Next wave in ", 
        	"Next wave in XX.XX".length(), pEngine.getVertexBufferObjectManager());
        this.mMoneyText = new Text(360, 0, this.mNeverwinterNightsFont, "Money:", 
        	"Money: XXXXXX".length(), pEngine.getVertexBufferObjectManager());
        this.mLifeText = new Text(360, 20, this.mNeverwinterNightsFont, "Life:", 
        	"Life: XXX".length(), pEngine.getVertexBufferObjectManager());
        
        this.mMoneyIcon = new Sprite(460, 5, 
        		((Game) pContext).getButtonFactory().getRegion(ButtonRegion.MONEY_ICON), 
        		pEngine.getVertexBufferObjectManager());
        this.mLifeIcon = new Sprite(330, 25, 
        		((Game) pContext).getButtonFactory().getRegion(ButtonRegion.MONEY_ICON), 
        		pEngine.getVertexBufferObjectManager());
        
        // Make load now all digits from the font
        this.mMoneyText.setText("0123456789");
        this.mMoneyText.setText("Money:");
        
        this.attachChild(this.mWaveNumberText);
        this.attachChild(this.mNextWaveText);
        this.attachChild(this.mMoneyText);
        
        this.attachChild(this.mMoneyIcon);
    	
        this.setOnSceneTouchListener(this);
        this.setTouchAreaBindingOnActionDownEnabled(true);
        this.setTouchAreaBindingOnActionMoveEnabled(true);
    }
    
    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    public void setWaveNumberText(final String pNumber) {
        this.mWaveNumberText.setText("Wave " + pNumber);
    }
    
    public void setMoneyText(final String pAmount) {
        this.mMoneyText.setText("Money: " + pAmount);
    }
    
    public void setLifeText(final String pLife) {
        this.mLifeText.setText("Life: " + pLife);
    }
    
    public void setNextWaveText(final String pText) {
        this.mNextWaveText.setText(pText);
    }
    
    public void setNextWaveVisible(final boolean pBoolean) {
        this.mNextWaveText.setVisible(pBoolean);
    }
    
    public ArrayList<ClickableButton> getClickableButtons() {
        return this.mClickableButtons;
    }
    
    public Level getLevel() {
        return this.mLevel;
    }
    
    public void setLock(final boolean pLocked) {
        this.mLocked = pLocked;
    }
    
    public boolean isLocked() {
        return this.mLocked;
    }
    
    // ===========================================================
 	// Methods
 	// ===========================================================
    
    private void loadFonts(final Engine pEngine, final Context pContext) {
        
        this.mFontTexture = new BitmapTextureAtlas(pEngine.getTextureManager(), 
        		256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mNeverwinterNightsFontTexture = new BitmapTextureAtlas(pEngine.getTextureManager(), 
        		256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        
        FontFactory.setAssetBasePath("font/");
        this.mDefaultFont = new Font(pEngine.getFontManager(), this.mFontTexture, 
        	Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 
        	FONT_SIZE, true, Color.BLACK);
        this.mNeverwinterNightsFont = FontFactory.createFromAsset(pEngine.getFontManager(),
        	this.mNeverwinterNightsFontTexture, pContext.getAssets(), 
        	"NeverwinterNights.ttf", FONT_SIZE, true, Color.BLACK);
        
        this.mFontTexture.load();
        this.mNeverwinterNightsFontTexture.load();
        
        this.mDefaultFont.load();
        this.mNeverwinterNightsFont.load();
    }
    
    /**
     * @param pFPSCounter
     */
    public void activateFps(final FPSCounter pFPSCounter) {
        
        this.attachChild(this.mFpsText);
        this.registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                        GameGUI.this.mFpsText.setText("FPS: " + pFPSCounter.getFPS());
                }
        }));
    }
    
    /**
     * @param button
     */
    public void addButton(final Button button) {
        
    	if (button instanceof ClickableButton) {
    		this.mClickableButtons.add((ClickableButton) button);
    	} else if (button instanceof ClickableButton) {
    		this.mDraggableTowerButtons.add((DraggableTowerButton) button);
    	}
    	
        this.attachChild(button);
        this.registerTouchArea(button);
    }
    
    /**
     * @param pGame
     * @param pEngine
     * @param pButtonFactory
     */
    public void build(final Game pGame, 
    		final Engine pEngine, final ButtonFactory pButtonFactory) {
    	
        final ClickableButton pauseButton = new ClickableButton(
                10, Game.CAMERA_HEIGHT - 40, 
                pButtonFactory.getTiledRegion(ButtonRegion.PLAY_PAUSE),
                pEngine.getVertexBufferObjectManager(),
                null);
        
        final ClickableButton fastForwardButton = new ClickableButton(
                50, Game.CAMERA_HEIGHT - 40, 
                pButtonFactory.getTiledRegion(ButtonRegion.FAST_FORWARD),
                pEngine.getVertexBufferObjectManager(),
                null);
        
        pauseButton.setClickDetector(
        		new ClickDetector(400, 
        				new ClickDetector.IClickDetectorListener() {

			@Override
			public void onClick(ClickDetector pClickDetector, int pPointerID,
					float pSceneX, float pSceneY) {
				
				pGame.pauseGame();
                if (GameGUI.this.isLocked()) {
                    pauseButton.setCurrentTileIndex(2);
                } else {
                    pauseButton.setCurrentTileIndex(0);
                }
			}
        }));
        
        fastForwardButton.setClickDetector(
        		new ClickDetector(400, 
        				new ClickDetector.IClickDetectorListener() {

			@Override
			public void onClick(ClickDetector pClickDetector, int pPointerID,
					float pSceneX, float pSceneY) {

				if (pGame.getLevel().getSpeed() == Level.NORMAL_SPEED) {
					pGame.getLevel().setSpeed(Level.DOUBLE_SPEED);
                    fastForwardButton.setCurrentTileIndex(2);
                } else {
                	pGame.getLevel().setSpeed(Level.NORMAL_SPEED);
                    fastForwardButton.setCurrentTileIndex(0);
                }
			}
        }));
    	
    	this.addButton(pauseButton);
        this.addButton(fastForwardButton);   
        
        ArrayList<TowerType> towerTypes = new ArrayList<TowerType>();
        towerTypes.add(TowerType.ARROW_TOWER);
        towerTypes.add(TowerType.ARROW_TOWER);
        
        
        final DraggableTowerButton arrowTowerButton = new DraggableTowerButton(
        		Game.CAMERA_WIDTH - 50, Game.CAMERA_HEIGHT - 40,
                pButtonFactory.getTiledRegion(ButtonRegion.CREATE_ARROW_TOWER), 
                pEngine.getVertexBufferObjectManager(),
                TowerType.ARROW_TOWER);
        
        this.addButton(arrowTowerButton);
        
        /*
        final PhysicsWorld physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        //physicsWorld.setContinuousPhysics(false);
        pGame.getLevel().registerUpdateHandler(physicsWorld);
        */
        //buildTowersWheel(pEngine, pButtonFactory, physicsWorld, towerTypes);
    }
    
    private void buildTowersWheel(final Engine pEngine, 
    		final ButtonFactory pButtonFactory, 
    		final PhysicsWorld pPhysicsWorld,
    		final ArrayList<TowerType> pTowerTypes) {
    	
    	final TextureRegion wheelRegion = pButtonFactory.getRegion(ButtonRegion.WHEEL);
		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(10, 0.2f, 0.5f);
		
		final Sprite anchorFace = new Sprite(
				Game.CAMERA_WIDTH - (wheelRegion.getWidth() / 2), 
				Game.CAMERA_HEIGHT - (wheelRegion.getHeight() / 2),
				wheelRegion, pEngine.getVertexBufferObjectManager());
		final Body anchorBody = PhysicsFactory.createBoxBody(
				pPhysicsWorld, 
				anchorFace,
				BodyType.StaticBody, 
				objectFixtureDef);
		
		final TowersWheel towersWheel = new TowersWheel(
    			Game.CAMERA_WIDTH - (wheelRegion.getWidth() / 2),
    			Game.CAMERA_HEIGHT - (wheelRegion.getHeight() / 2),
    			wheelRegion, pEngine.getVertexBufferObjectManager(),
    			pPhysicsWorld);
		final Body towersWheelBody = PhysicsFactory.createCircleBody(
	 				pPhysicsWorld, 
	 				towersWheel, 
	     			BodyType.DynamicBody, 
	     			objectFixtureDef);
		
		towersWheel.setBody(towersWheelBody);
		
		anchorFace.setVisible(false);
		
		this.attachChild(anchorFace);
		this.attachChild(towersWheel);
		
		pPhysicsWorld.registerPhysicsConnector(
    			new PhysicsConnector(anchorFace, anchorBody, true, true) {
    				@Override
    				public void onUpdate(final float pSecondsElapsed) {
    					super.onUpdate(pSecondsElapsed);
    					final Vector2 movingBodyWorldCenter = towersWheelBody.getWorldCenter();
    				}
    			});
		
		pPhysicsWorld.registerPhysicsConnector(
				new PhysicsConnector(towersWheel, towersWheelBody, true, true));
		
		final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.initialize(anchorBody, towersWheelBody, anchorBody.getWorldCenter());
		revoluteJointDef.enableMotor = false;
		revoluteJointDef.motorSpeed = 10.0f;
		//revoluteJointDef.enableLimit = true;
		revoluteJointDef.maxMotorTorque = 20.0f;
		
		pPhysicsWorld.createJoint(revoluteJointDef);
    	
    	final DraggableTowerButton arrowTowerButton = new DraggableTowerButton(
                0, 0,
                pButtonFactory.getTiledRegion(ButtonRegion.CREATE_ARROW_TOWER), 
                pEngine.getVertexBufferObjectManager(),
                TowerType.ARROW_TOWER);
    	
    	towersWheel.attachChild(arrowTowerButton);
    	towersWheel.setMotor(revoluteJointDef);
    	
    	this.registerTouchArea(towersWheel);
    }
    
    /**
     * @param pNewAmount
     */
    public void updateAvailablePurchases(final int pNewAmount) {
    	
    	for (final DraggableTowerButton button : this.mDraggableTowerButtons) {
    		
    		final Properties properties = this.mLevel.getTowerFactory().
    				getProperties(button.getTowerType());
    		final int priceTower = PropertiesUtils.parseInt(properties, 
    				properties.getProperty(IndexPropertyConstants.PRICE));
    		
    		if (priceTower < pNewAmount) {
    			button.setActivated(false);
    		} else {
    			button.setActivated(true);
    		}    		
    	}
    }
    
    // ===========================================================
 	// Inner and Anonymous Classes
 	// ===========================================================
    
    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {

        return false;
    }
}

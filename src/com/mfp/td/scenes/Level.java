/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.scenes;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.debug.Debug;

import android.content.Context;

import com.mfp.td.gui.GameGUI;
import com.mfp.td.monsters.Monster;
import com.mfp.td.monsters.Monster.MonsterType;
import com.mfp.td.monsters.MonsterFactory;
import com.mfp.td.scenes.events.LevelEvents;
import com.mfp.td.scenes.events.LevelEventsLoader;
import com.mfp.td.towers.Tower;
import com.mfp.td.towers.Tower.TowerType;
import com.mfp.td.towers.TowerFactory;
import com.mfp.td.towers.TowerSprite;

/**
 *
 * @author M-F.P
 */
public class Level extends CameraManager {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final float NORMAL_SPEED = 1f;
	public static final float DOUBLE_SPEED = 2f;

	// ===========================================================
	// Fields
	// ===========================================================

	private float mSpeed = NORMAL_SPEED;
	private int mMoney;

	private GameGUI mGui;
	private TMXTiledMap mTMXTiledMap;

	private TowerFactory mTowerFactory;
	private MonsterFactory mMonsterFactory;
	private LevelEvents mLevelEvents;

	private Tower mCurrentClickedTower;
	private final ArrayList<Tower> mTowers;
	private final ArrayList<Monster> mMonsters;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Level(final SmoothCamera pCamera) {

		super(pCamera);

		this.mTowers = new ArrayList<Tower>();
		this.mMonsters = new ArrayList<Monster>();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public GameGUI getGUI() {
		return this.mGui;
	}

	public LevelEvents getLevelEvents() {
		return this.mLevelEvents;
	}

	public SmoothCamera getCamera() {
		return this.mCamera;
	}

	public ArrayList<Monster> getMonsters() {
		return this.mMonsters;
	}

	public ArrayList<Tower> getTowers() {
		return this.mTowers;
	}

	public TMXTiledMap getTMXTiledMap() {
		return this.mTMXTiledMap;
	}

	public float getSpeed() {
		return this.mSpeed;
	}

	public void setSpeed(final float pSpeed) {
		this.mSpeed = pSpeed;
	}
	
	public int getMoney() {
		return mMoney;
	}
	
	public void setMoney(int pMoney) {
		this.mMoney = pMoney;
	}
	
	public TowerFactory getTowerFactory() {
		return this.mTowerFactory;
	}
	
	public Tower getCurrentClickedTower() {
		return this.mCurrentClickedTower;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {

		this.hideCurrentClickedTower();
		return super.onSceneTouchEvent(pScene, pSceneTouchEvent);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pContext
	 * @param filename
	 */
	public void createEvents(final Context pContext, final String filename) {

		this.mLevelEvents = new LevelEventsLoader().createLevelEvents(
				this, pContext, filename);
	}

	/**
	 * @param pEngine
	 * @param pContext
	 * @param pTowerFactory
	 * @param pMonsterFactory
	 */
	public void createScene(final Engine pEngine, final Context pContext,
			final TowerFactory pTowerFactory, 
			final MonsterFactory pMonsterFactory) {
		
		this.mGui = (GameGUI) pEngine.getCamera().getHUD();
		this.mMonsterFactory = pMonsterFactory;
		this.mTowerFactory = pTowerFactory;
		
		try {
			final TMXLoader tmxLoader = new TMXLoader(pContext.getAssets(), 
					pEngine.getTextureManager(), 
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, 
					pEngine.getVertexBufferObjectManager(), 
					new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, 
						final TMXLayer pTMXLayer, final TMXTile pTMXTile, 
						final TMXProperties<TMXTileProperty> pTMXTileProperties) {

				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/map_test.tmx");
			
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		
		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
	    for (final TMXLayer tmxlayer : this.mTMXTiledMap.getTMXLayers()) {
	    	this.attachChild(tmxlayer);
	    }
	    
		this.setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		
		this.mCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
		this.mCamera.setBoundsEnabled(true);
	}
	
	public Tower getTowerFromSprite(final TowerSprite pTowerSprite) {
		
		for (final Tower tower : this.mTowers) {
			if (tower.getSprite() == pTowerSprite) {
				return tower;
			}
		}		
		return null;
	}
	
	/**
	 * @param pMonsterType
	 * @param pX
	 * @param pY
	 */
	public Monster createMonster(final MonsterType pMonsterType, 
			final float pX, final float pY) {

		final Monster monster = this.mMonsterFactory.getFromType(
				pMonsterType, pX, pY);

		if (!monster.getSprite().hasParent()) {

			this.attachChild(monster.getSprite());

			monster.getSprite().attachChild(monster.getBackgroundHealthBar());
			monster.getSprite().attachChild(monster.getHealthBar());
			monster.getSprite().attachChild(monster.getBlood().getSprite());
		}
		
		this.mMonsters.add(monster);
		return monster;
	}
	
	/**
	 * @param pMonster
	 */
	public void removeMonster(final Monster pMonster) {

		if (this.mMonsters.contains(pMonster)) {
			this.mMonsters.remove(pMonster);
			this.mMonsterFactory.recycle(pMonster);
		}
	}

	/**
	 * @param pTowerType
	 * @param pX
	 * @param pY
	 */
	public Tower createTower(final TowerType pTowerType, final float pX, final float pY) {

		final Tower tower = this.mTowerFactory.getFromType(pTowerType, pX, pY);

		tower.getGUI().display(false);
		if (!tower.getSprite().hasParent()) {

			this.attachChild(tower.getRangeSprite());
			this.attachChild(tower.getSprite());
			tower.setLevel(this);

			tower.getSprite().attachChild(tower.getGUI().getEvolutionButtonA());
			tower.getSprite().attachChild(tower.getGUI().getEvolutionButtonB());
			tower.getSprite().attachChild(tower.getGUI().getSpecialEvolutionButton());
			tower.getSprite().attachChild(tower.getGUI().getDeleteButton());
			
			this.registerTouchArea(tower.getSprite());
			this.registerTouchArea(tower.getRangeSprite());
			this.registerTouchArea(tower.getGUI().getEvolutionButtonA());
			this.registerTouchArea(tower.getGUI().getEvolutionButtonB());
			this.registerTouchArea(tower.getGUI().getSpecialEvolutionButton());
			this.registerTouchArea(tower.getGUI().getDeleteButton());
		}

		this.mTowers.add(tower);
		return tower;
	}
	
	/**
	 * @param pTower
	 */
	public void removeTower(final Tower pTower) {
		
		if (this.mTowers.contains(pTower)) {
			this.mTowers.remove(pTower);
			this.mTowerFactory.recycle(pTower);
		}
	}

	/**
	 * 
	 */
	public void hideCurrentClickedTower() {

		if (this.mCurrentClickedTower != null) {

			this.mCurrentClickedTower.getGUI().display(false);
			if (this.mCurrentClickedTower.getRangeSprite().isVisible()) {
				this.mCurrentClickedTower.getRangeSprite().setVisible(false);
			}
		}
	}

	/**
	 * 
	 * @param pClickedTower
	 */
	public void setCurrentClickedTower(final Tower pClickedTower) {

		this.hideCurrentClickedTower();

		pClickedTower.getRangeSprite().setZIndex(this.getChildCount() - 1);
		pClickedTower.getSprite().setZIndex(this.getChildCount() - 1);

		pClickedTower.getRangeSprite().setVisible(true);
		pClickedTower.getGUI().display(true);

		this.mCurrentClickedTower = pClickedTower;
	}

	/**
	 * @param pUpgrateTowerType
	 */
	public void upgradeCurrentTower(final TowerType pUpgrateTowerType) {

		this.createTower(pUpgrateTowerType, 
				this.mCurrentClickedTower.getSprite().getX(), 
				this.mCurrentClickedTower.getSprite().getY());

		this.removeTower(this.mCurrentClickedTower);
		this.mCurrentClickedTower = this.getTowers().get(this.getTowers().size() - 1);
		this.mCurrentClickedTower.build();
	}

	/**
	 * @param pTowerSprite
	 * @return
	 */
	public boolean isCollidingWithAnotherTower(final TowerSprite pTowerSprite) {

		for (Tower tower : this.mTowers) {

			if (pTowerSprite != tower.getSprite() &&
					pTowerSprite.collidesWith(tower.getSprite())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param pAmount
	 */
	public void updateMoney(final int pAmount) {
		
		this.mMoney += pAmount;
		this.mGui.setMoneyText(String.valueOf(this.mMoney));
		this.mGui.updateAvailablePurchases(this.mMoney);
	}
}

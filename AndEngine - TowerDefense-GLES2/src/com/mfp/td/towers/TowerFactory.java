/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.pool.MultiPool;
import org.andengine.util.debug.Debug;

import android.content.Context;

import com.mfp.td.buttons.ButtonFactory;
import com.mfp.td.buttons.Button.ButtonType;
import com.mfp.td.towers.Tower.TowerType;
import com.mfp.td.towers.buildingclouds.BuildingCloudPool;
import com.mfp.td.towers.missiles.MissileFactory;
import com.mfp.td.utils.BaseFactory;
import com.mfp.td.utils.PropertiesUtils;
import com.mfp.td.utils.IndexPropertyConstants;

/**
 *
 * @author M-F.P
 */
public class TowerFactory extends BaseFactory {

	// ===========================================================
	// Elements
	// ===========================================================

	// ===========================================================
	// Constants
	// ===========================================================

	private final String PROPERTIES_DIR = "properties/towers/";

	// ===========================================================
	// Fields
	// ===========================================================

	private final ButtonFactory mButtonFactory;
	private final MissileFactory mMissileFactory;

	private final MultiPool<Tower> mTowerPools;
	private final ArrayList<Properties> mTowerProperties;
	private BuildingCloudPool mBuildingCloudPool;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TowerFactory(final Engine pEngine, final Context pContext, 
			final ButtonFactory pButtonFactory) {

		this.mButtonFactory = pButtonFactory;
		this.mMissileFactory = new MissileFactory(pEngine, pContext);
		
		this.mTowerPools = new MultiPool<Tower>();
		this.mTowerProperties = new ArrayList<Properties>();
		
		this.loadResources(pEngine, pContext);
	}
	
	// ===========================================================
    // Getter & Setter
    // ===========================================================
	
	public Properties getProperties(final TowerType pTowerType) {
		return this.mTowerProperties.get(pTowerType.ordinal());
	}
	
	public TextureRegion getRegion(final TowerRegion pRegion) {
		return this.mTextureRegionLibrary.get(pRegion.getSrcName());
	}
	
	public TiledTextureRegion getTiledRegion(final TowerRegion pRegion) {
		return this.getTiled(pRegion.getSrcName(), pRegion.getRows(), pRegion.getColumns());
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pTowerType
	 * @param pX
	 * @param pY
	 * @return
	 */
	public Tower getFromType(final TowerType pTowerType, final float pX, final float pY) {

		final Tower tower = this.mTowerPools.obtainPoolItem(pTowerType.ordinal());
		tower.getSprite().setPosition(pX, pY);

		float posRangeX = tower.getSprite().getX() + 
				(tower.getSprite().getWidth() / 2) - 
				(tower.getRangeSprite().getWidth() / 2);
		float posRangeY = tower.getSprite().getY() + 
				(tower.getSprite().getHeight() / 2) - 
				(tower.getRangeSprite().getHeight() / 2);

		tower.getRangeSprite().setPosition(posRangeX, posRangeY);

		return tower;
	}

	/**
	 * @param pTower
	 */
	public void recycle(final Tower pTower) {
		this.mTowerPools.recyclePoolItem(pTower.getType().ordinal(), pTower);
	}
	
	private void loadResources(final Engine pEngine, final Context pContext) {

		super.loadSpritesheets(pEngine, pContext, "xml/towers.xml");

		this.mBuildingCloudPool = new BuildingCloudPool(
				this.getTiledRegion(TowerRegion.BUILDING_CLOUD),
				pEngine.getVertexBufferObjectManager());

		for (TowerType towerType : TowerType.values()) {
			this.mTowerPools.registerPool(towerType.ordinal(), 
					createTowerPool(towerType, pEngine, pContext));
		}
	}

	private Properties loadProperties(final Context pContext, final TowerType pTowerType) {

		String filename = PROPERTIES_DIR+pTowerType.name().toLowerCase()+".properties";
		return PropertiesUtils.loadProperties(pContext, filename);
	}

	private TowerPool createTowerPool(final TowerType pTowerType, 
			final Engine pEngine, final Context pContext) {

		Sound fireSound = null;

		final Properties properties = loadProperties(pContext, pTowerType);

		final TiledTextureRegion towerTiledRegion = this.getTiledRegion(
				TowerRegion.valueOf(
						properties.getProperty(
								IndexPropertyConstants.TEXTURE_REGION)
						)
				);
		
		try {
			fireSound = SoundFactory.createSoundFromAsset(pEngine.getSoundManager(), 
					pContext, properties.getProperty(IndexPropertyConstants.FIRE_SOUND));
		} catch (final IOException e) {
			Debug.e(e);
		}

		final TowerPool towerPool = new TowerPool(
				pTowerType, 
				properties, 
				fireSound, 
				this.mMissileFactory, 
				this.mButtonFactory, 
				this.mBuildingCloudPool,
				this.getRegion(TowerRegion.RANGE),
				towerTiledRegion,
				pEngine.getVertexBufferObjectManager());

		return towerPool;
	}
}

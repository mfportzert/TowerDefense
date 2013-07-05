/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters;

import java.util.HashMap;
import java.util.Properties;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.pool.MultiPool;

import android.content.Context;

import com.mfp.td.monsters.Monster.MonsterType;
import com.mfp.td.monsters.blood.BloodFactory;
import com.mfp.td.utils.BaseFactory;
import com.mfp.td.utils.IndexPropertyConstants;
import com.mfp.td.utils.PropertiesUtils;

/**
 *
 * @author M-F.P
 */
public class MonsterFactory extends BaseFactory {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PROPERTIES_DIR = "properties/monsters/";

	// ===========================================================
	// Fields
	// ===========================================================

	private BloodFactory mBloodFactory;
	private MultiPool<Monster> mMonsterPools;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MonsterFactory(final Engine pEngine, final Context pContext,
			final HashMap<MonsterType, Integer> pInitialCapacities) {

		super();

		this.mBloodFactory = new BloodFactory(pEngine, pContext);
		this.mMonsterPools = new MultiPool<Monster>();

		this.loadResources(pEngine, pContext, pInitialCapacities);
	}
	
	// ===========================================================
    // Getter & Setter
    // ===========================================================
	
	public TextureRegion getRegion(final MonsterRegion pRegion) {
		return this.mTextureRegionLibrary.get(pRegion.getSrcName());
	}
	
	public TiledTextureRegion getTiledRegion(final MonsterRegion pRegion) {
		return this.getTiled(pRegion.getSrcName(), pRegion.getRows(), pRegion.getColumns());
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pMonsterType
	 * @param pX
	 * @param pY
	 * @return
	 */
	public Monster getFromType(final MonsterType pMonsterType, final float pX, final float pY) {

		final Monster monster = this.mMonsterPools.obtainPoolItem(pMonsterType.ordinal());
		monster.getSprite().setPosition(pX, pY);

		return monster;
	}

	/**
	 * @param pMonster
	 */
	public void recycle(Monster pMonster) {

		this.mMonsterPools.recyclePoolItem(pMonster.getType().ordinal(), pMonster);
	}
	
	private void loadResources(final Engine pEngine, final Context pContext,
			final HashMap<MonsterType, Integer> pInitialCapacities) {

		super.loadSpritesheets(pEngine, pContext, "xml/monsters.xml");
		
		// Load all default basic properties in properties/towers/ directory 
		for (final MonsterType monsterType : pInitialCapacities.keySet()) {

			final Properties properties = loadProperties(pContext, monsterType);

			final TiledTextureRegion tiledTextureRegion = this.getTiledRegion(
					MonsterRegion.valueOf(
							properties.getProperty(
									IndexPropertyConstants.TEXTURE_REGION)
							)
					);

			final MonsterPool monsterPool = new MonsterPool(
					monsterType, 
					properties, 
					this.mBloodFactory,
					tiledTextureRegion,
					pEngine.getVertexBufferObjectManager());
			
			monsterPool.batchAllocatePoolItems(pInitialCapacities.get(monsterType));
			this.mMonsterPools.registerPool(monsterType.ordinal(), monsterPool);
		}
	}

	private Properties loadProperties(final Context pContext, final MonsterType pMonsterType) {

		final String filename = PROPERTIES_DIR+pMonsterType.name().toLowerCase()+".properties";
		return PropertiesUtils.loadProperties(pContext, filename);
	}


}

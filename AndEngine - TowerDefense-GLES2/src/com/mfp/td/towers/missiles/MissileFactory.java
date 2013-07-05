/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers.missiles;

import java.util.Properties;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.pool.MultiPool;

import android.content.Context;

import com.mfp.td.towers.missiles.Missile.MissileType;
import com.mfp.td.utils.BaseFactory;
import com.mfp.td.utils.PropertiesUtils;
import com.mfp.td.utils.IndexPropertyConstants;

/**
 *
 * @author M-F.P
 */
public class MissileFactory extends BaseFactory {

	// ===========================================================
	// Elements
	// ===========================================================

	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PROPERTIES_DIR = "properties/missiles/";

	// ===========================================================
	// Fields
	// ===========================================================

	private final MultiPool<Missile> mMissilePool;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pEngine
	 * @param pContext
	 */
	public MissileFactory(final Engine pEngine, final Context pContext) {

		super();

		this.mMissilePool = new MultiPool<Missile>();

		this.loadResources(pEngine, pContext);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pMissileType
	 * @param pX
	 * @param pY
	 * @return
	 */
	public Missile createFromType(final MissileType pMissileType, final float pX, final float pY) {

		final Missile newMissile = this.mMissilePool.obtainPoolItem(pMissileType.ordinal());
		newMissile.getSprite().setPosition(pX, pY);

		return newMissile;
	}

	/**
	 * @param pMissile
	 */
	public void recycle(final Missile pMissile) {

		this.mMissilePool.recyclePoolItem(pMissile.getType().ordinal(), pMissile);
	}

	/**
	 * @param pRegion
	 * @return
	 */
	public TextureRegion getRegion(final MissileRegion pRegion) {

		return this.mTextureRegionLibrary.get(pRegion.getSrcName());
	}

	/**
	 * @param pRegion
	 * @return
	 */
	public TiledTextureRegion getTiledRegion(final MissileRegion pRegion) {

		return this.getTiled(pRegion.getSrcName(), pRegion.getRows(), pRegion.getColumns());
	}

	private void loadResources(final Engine pEngine, final Context pContext) {

		super.loadSpritesheets(pEngine, pContext, "xml/missiles.xml");

		/* Load all default basic properties in properties/towers/ directory */
		for (MissileType missileType : MissileType.values()) {

			final Properties properties = loadProperties(pContext, missileType);

			final TextureRegion textureRegion = this.getRegion(
					MissileRegion.valueOf(
							properties.getProperty(IndexPropertyConstants.TEXTURE_REGION)
							)
					);

			this.mMissilePool.registerPool(missileType.ordinal(), 
					new MissilePool(
							missileType, 
							properties,
							textureRegion,
							pEngine.getVertexBufferObjectManager())
					);
		}
	}

	private Properties loadProperties(final Context pContext, final MissileType pMissileType) {

		String filename = PROPERTIES_DIR+pMissileType.name().toLowerCase()+".properties";
		return PropertiesUtils.loadProperties(pContext, filename);
	}
}

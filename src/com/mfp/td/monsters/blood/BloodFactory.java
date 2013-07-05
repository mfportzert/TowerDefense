/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters.blood;

import java.util.Properties;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.pool.MultiPool;

import android.content.Context;

import com.mfp.td.monsters.blood.Blood.BloodType;
import com.mfp.td.utils.BaseFactory;
import com.mfp.td.utils.PropertiesUtils;
import com.mfp.td.utils.IndexPropertyConstants;

/**
 *
 * @author M-F.P
 */
public class BloodFactory extends BaseFactory {

	// ===========================================================
	// Elements
	// ===========================================================

	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PROPERTIES_DIR = "properties/blood/";

	// ===========================================================
	// Fields
	// ===========================================================

	private final MultiPool<Blood> mBloodPool;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * 
	 * @param pEngine
	 * @param pContext
	 */
	public BloodFactory(final Engine pEngine, final Context pContext) {

		super();

		this.mBloodPool = new MultiPool<Blood>();

		this.loadResources(pEngine, pContext);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pBloodType
	 * @param pX
	 * @param pY
	 * @return
	 */
	public Blood createFromType(final BloodType pBloodType, final float pX, final float pY) {

		final Blood newBlood = this.mBloodPool.obtainPoolItem(pBloodType.ordinal());
		newBlood.getSprite().setPosition(pX, pY);

		return newBlood;
	}

	/**
	 * @param pBlood
	 */
	public void recycle(final Blood pBlood) {

		this.mBloodPool.recyclePoolItem(pBlood.getType().ordinal(), pBlood);
	}

	/**
	 * @param pRegion
	 * @return
	 */
	public TextureRegion getRegion(final BloodRegion pRegion) {

		return this.mTextureRegionLibrary.get(pRegion.getSrcName());
	}

	/**
	 * @param pRegion
	 * @return
	 */
	public TiledTextureRegion getTiledRegion(final BloodRegion pRegion) {

		return this.getTiled(pRegion.getSrcName(), pRegion.getRows(), pRegion.getColumns());
	}

	private void loadResources(final Engine pEngine, final Context pContext) {

		super.loadSpritesheets(pEngine, pContext, "xml/blood.xml");

		/* Load all default basic properties in properties/blood/ directory */
		for (BloodType bloodType : BloodType.values()) {

			final Properties properties = loadProperties(pContext, bloodType);

			final TiledTextureRegion tiledTextureRegion = this.getTiledRegion(
					BloodRegion.valueOf(
							properties.getProperty(
									IndexPropertyConstants.TEXTURE_REGION)
							)
					);

			this.mBloodPool.registerPool(bloodType.ordinal(), 
					new BloodPool(
							bloodType, 
							properties,
							tiledTextureRegion,
							pEngine.getVertexBufferObjectManager())
					);
		}
	}

	private Properties loadProperties(final Context pContext, final BloodType pBloodType) {

		String filename = PROPERTIES_DIR+pBloodType.name().toLowerCase()+".properties";
		return PropertiesUtils.loadProperties(pContext, filename);
	}
}

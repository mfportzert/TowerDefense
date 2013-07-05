/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters.blood;

import java.util.Properties;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemoryTiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

import com.mfp.td.monsters.blood.Blood.BloodType;

/**
 *
 * @author M-F.P
 */
public class BloodPool extends GenericPool<Blood> {

	// ===========================================================
    // Fields
    // ===========================================================
	
	private final BloodType mType;
	private final Properties mProperties;
	private final TiledTextureRegion mTiledTextureRegion;
	private final ITiledSpriteVertexBufferObject mSharedVertexBuffer;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	
	// ===========================================================
    // Constructors
    // ===========================================================
	
	public BloodPool(final BloodType pType, 
			final Properties pProperties,
			final TiledTextureRegion pTiledTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {

		this.mType = pType;
		this.mProperties = pProperties;
		this.mTiledTextureRegion = pTiledTextureRegion;

		this.mSharedVertexBuffer = new LowMemoryTiledSpriteVertexBufferObject(
				pVertexBufferObjectManager,
				Sprite.SPRITE_SIZE, 
				DrawType.STATIC, 
				true, 
				Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	// ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
	
	@Override
	protected Blood onAllocatePoolItem() {

		final BloodSprite sprite = new BloodSprite(0, 0, 
				this.mTiledTextureRegion, 
				/*this.mSharedVertexBuffer*/
				this.mVertexBufferObjectManager);
		
		return new Blood(this.mType, this.mProperties, sprite);
	}

	@Override
	protected void onHandleRecycleItem(final Blood pBlood) {
		
		pBlood.getSprite().setIgnoreUpdate(true);
		pBlood.getSprite().setVisible(false);
	}

	@Override
	protected void onHandleObtainItem(final Blood pBlood) {
		
		pBlood.getSprite().reset();
	}
}

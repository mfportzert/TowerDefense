/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers.buildingclouds;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemoryTiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

/**
 *
 * @author M-F.P
 */
public class BuildingCloudPool extends GenericPool<AnimatedSprite> {

	// ===========================================================
    // Fields
    // ===========================================================
	
	private final TiledTextureRegion mBuildingCloudTiledTextureRegion;
	private final ITiledSpriteVertexBufferObject mSharedVertexBuffer;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * @param pBuildingCloudTiledTextureRegion
	 * @param pVertexBufferObjectManager
	 */
	public BuildingCloudPool(
			final TiledTextureRegion pBuildingCloudTiledTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {

		this.mBuildingCloudTiledTextureRegion = pBuildingCloudTiledTextureRegion;

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
	protected AnimatedSprite onAllocatePoolItem() {

		final AnimatedSprite buildingSprite = new AnimatedSprite(0, 0, 
				this.mBuildingCloudTiledTextureRegion,
				/*this.mSharedVertexBuffer*/
				this.mVertexBufferObjectManager);

		return buildingSprite;
	}
	
	@Override
	protected void onHandleRecycleItem(final AnimatedSprite pBuildingCloudSprite) {

		pBuildingCloudSprite.setIgnoreUpdate(true);
		pBuildingCloudSprite.setVisible(false);
	}
	
	@Override
	protected void onHandleObtainItem(final AnimatedSprite pBuildingCloudSprite) {

		pBuildingCloudSprite.setIgnoreUpdate(false);
		pBuildingCloudSprite.setVisible(true);
		pBuildingCloudSprite.setAlpha(0.8f);
		pBuildingCloudSprite.setFlippedHorizontal(true);
	}
}

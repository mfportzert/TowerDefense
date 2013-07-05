/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters;

import java.util.Properties;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.primitive.vbo.LowMemoryRectangleVertexBufferObject;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemoryTiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

import com.mfp.td.monsters.Monster.MonsterType;
import com.mfp.td.monsters.blood.BloodFactory;
import com.mfp.td.utils.IndexPropertyConstants;
import com.mfp.td.utils.PropertiesUtils;

/**
 *
 * @author M-F.P
 */
public class MonsterPool extends GenericPool<Monster> {
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private final MonsterType mType;
	private final Properties mProperties;
	private final BloodFactory mBloodFactory;
	private final TiledTextureRegion mTiledTextureRegion;
	private final ITiledSpriteVertexBufferObject mSharedVertexBuffer;
	private final IRectangleVertexBufferObject mHealthBarVertexBufferObject;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * @param pType
	 * @param pProperties
	 * @param pBloodFactory
	 * @param pTiledTextureRegion
	 * @param pVertexBufferObjectManager
	 */
	public MonsterPool(final MonsterType pType, 
			final Properties pProperties, 
			final BloodFactory pBloodFactory, 
			final TiledTextureRegion pTiledTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super();
		
		this.mType = pType;
		this.mProperties = pProperties;
		this.mBloodFactory = pBloodFactory;
		this.mTiledTextureRegion = pTiledTextureRegion;

		this.mSharedVertexBuffer = new LowMemoryTiledSpriteVertexBufferObject(
				pVertexBufferObjectManager,
				Sprite.SPRITE_SIZE, 
				DrawType.STATIC, 
				true, 
				Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		
		this.mHealthBarVertexBufferObject = new LowMemoryRectangleVertexBufferObject(
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
	protected Monster onAllocatePoolItem() {

		final MonsterSprite sprite = new MonsterSprite(0, 0, 
				PropertiesUtils.parseFloat(this.mProperties, IndexPropertyConstants.SIZE_WIDTH),
				PropertiesUtils.parseFloat(this.mProperties, IndexPropertyConstants.SIZE_HEIGHT),
				this.mTiledTextureRegion,
				/*this.mSharedVertexBuffer*/
				this.mVertexBufferObjectManager);

		final Rectangle healthBar = new Rectangle(
				sprite.getWidth() / 4,
				-8, 
				sprite.getWidth() / 2, 
				3, 
				this.mVertexBufferObjectManager);
		
		return new Monster(
				this.mType, 
				this.mProperties, 
				this.mBloodFactory, 
				sprite,
				healthBar);
	}

	@Override
	protected void onHandleRecycleItem(final Monster pMonster) {

		pMonster.getSprite().setIgnoreUpdate(true);
		pMonster.getSprite().setVisible(false);
		pMonster.getBackgroundHealthBar().setIgnoreUpdate(true);
		pMonster.getBackgroundHealthBar().setVisible(false);
		pMonster.getHealthBar().setIgnoreUpdate(true);
		pMonster.getHealthBar().setVisible(false);
	}

	@Override
	protected void onHandleObtainItem(final Monster pMonster) {
		pMonster.reset();
	}
}

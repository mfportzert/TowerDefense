/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.buttons;

import java.util.Properties;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemoryTiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

import com.mfp.td.buttons.Button.ButtonType;
import com.mfp.td.utils.IndexPropertyConstants;

/**
 *
 * @author M-F.P
 */
public class ButtonPool extends GenericPool<Button> {

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final ButtonType mType;
	private final Properties mProperties;
	private final TiledTextureRegion mTextureRegion;
	private final ITiledSpriteVertexBufferObject mSharedVertexBuffer;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * 
	 * @param pType
	 * @param pProperties
	 * @param pFireSound
	 * @param pMissileFactory
	 * @param pTiledTextureRegion
	 */
	public ButtonPool(final ButtonType pType, 
			final Properties pProperties, 
			final TiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {

		this.mType = pType;
		this.mProperties = pProperties;
		this.mTextureRegion = pTextureRegion;

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
	protected Button onAllocatePoolItem() {

		final Button.SuperType superType = Button.SuperType.valueOf(
				this.mProperties.getProperty(
						IndexPropertyConstants.SUPERTYPE)
				);

		switch (superType) {
		case CLICKABLE:
			return new ClickableButton(0, 0, 
					this.mTextureRegion, 
					/*this.mSharedVertexBuffer*/
					this.mVertexBufferObjectManager,
					this.mType);
		default:
			return null;
		}
	}
	
	@Override
	protected void onHandleRecycleItem(final Button pButton) {

		pButton.setIgnoreUpdate(true);
		pButton.setVisible(false);
	}
	
	@Override
	protected void onHandleObtainItem(final Button pButton) {

		pButton.reset();
	}
}

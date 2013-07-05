/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.buttons;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author M-F.P
 */
public class Button extends AnimatedSprite {

	// ===========================================================
    // Fields
    // ===========================================================
	
	private final ButtonType mType;

	// ===========================================================
    // Constructors
    // ===========================================================
	/*
	public Button(final float pX, final float pY, 
			final TiledTextureRegion pControlBaseTextureRegion, 
			final ITiledSpriteVertexBufferObject pSharedVertexBuffer,
			final ButtonType pType) {

		super(pX, pY, pControlBaseTextureRegion, pSharedVertexBuffer);
		this.mType = pType;
	}
	*/
	public Button(final float pX, final float pY, 
			final TiledTextureRegion pControlBaseTextureRegion, 
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ButtonType pType) {

		super(pX, pY, pControlBaseTextureRegion, pVertexBufferObjectManager);
		this.mType = pType;
	}
	
	// ===========================================================
    // Getter & Setter
    // ===========================================================
	
	public ButtonType getType() {
		return this.mType;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum SuperType {

		CLICKABLE,
		DRAGGABLE;
	}
	
	public static enum ButtonType {
	    
	    FIRE_ARROW_TOWER,
	    ICE_ARROW_TOWER,
	    MULTI_ARROW,
	    DELETE;
	}
}

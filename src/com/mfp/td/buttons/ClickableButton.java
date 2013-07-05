/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.buttons;

import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author M-F.P
 */
public class ClickableButton extends Button {

	private boolean mHasClicked;
	private ClickDetector mClickDetector;

	/*
	public ClickableButton(final float pX, final float pY, 
			final TiledTextureRegion pControlBaseTextureRegion, 
			final ITiledSpriteVertexBufferObject pTiledSpriteVertexBuffer,
			final ButtonType pType) {

		super(pX, pY, pControlBaseTextureRegion, pTiledSpriteVertexBuffer, pType);

		this.mHasClicked = false;
	}
	*/
	public ClickableButton(final float pX, final float pY, 
			final TiledTextureRegion pControlBaseTextureRegion, 
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ButtonType pType) {

		super(pX, pY, pControlBaseTextureRegion, pVertexBufferObjectManager, pType);

		this.mHasClicked = false;
	}

	public void setClickDetector(final ClickDetector pClickDetector) {
		this.mClickDetector = pClickDetector;
	}

	public ClickDetector getClickDetector() {
		return this.mClickDetector;
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, 
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

		if (this.mClickDetector != null && this.mVisible) {

			if (pSceneTouchEvent.isActionDown()) {

				this.setCurrentTileIndex(this.getCurrentTileIndex() + 1);
				this.mHasClicked = true;
				this.mClickDetector.onTouchEvent(pSceneTouchEvent);

			} else if (pSceneTouchEvent.isActionUp()) {

				if (this.mHasClicked) {

					this.setCurrentTileIndex(this.getCurrentTileIndex() - 1);
					this.mClickDetector.onTouchEvent(pSceneTouchEvent);
					this.mHasClicked = false;
				}
			}
			return true;
		}
		return false;
	}
}

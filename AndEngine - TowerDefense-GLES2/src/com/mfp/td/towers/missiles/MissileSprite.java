/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers.missiles;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author M-F.P
 */
public class MissileSprite extends Sprite {

	public MissileSprite(final float pX, final float pY, 
			final TextureRegion pTextureRegion,
			final ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		
		super(pX, pY, pTextureRegion, pSpriteVertexBufferObject);
	}
	
	public MissileSprite(final float pX, final float pY, 
			final TextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}
}

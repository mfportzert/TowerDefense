/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters.blood;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author M-F.P
 */
public class BloodSprite extends AnimatedSprite {
    
    public BloodSprite(final float pX, final float pY, 
    		final TiledTextureRegion pTiledTextureRegion,
    		final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
    	
        super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject);
    }
    
    public BloodSprite(final float pX, final float pY, 
    		final TiledTextureRegion pTiledTextureRegion,
    		final VertexBufferObjectManager pVertexBufferObjectManager) {
    	
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
}
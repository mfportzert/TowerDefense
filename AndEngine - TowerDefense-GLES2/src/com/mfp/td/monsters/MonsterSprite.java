/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.mfp.td.scenes.Level;

/**
 *
 * @author M-F.P
 */
public class MonsterSprite extends AnimatedSprite {
    
    public MonsterSprite(final float pX, final float pY, 
            final TiledTextureRegion pTiledTextureRegion,
            final ITiledSpriteVertexBufferObject pSharedVertexBuffer) {
    	
        super(pX, pY, pTiledTextureRegion, pSharedVertexBuffer);
    }
    
    public MonsterSprite(final float pX, final float pY, 
            final TiledTextureRegion pTiledTextureRegion,
            final VertexBufferObjectManager pVertexBufferObjectManager) {
    	
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    
    public MonsterSprite(final float pX, final float pY, 
    		final float pWidth,
    		final float pHeight,
            final TiledTextureRegion pTiledTextureRegion,
            final VertexBufferObjectManager pVertexBufferObjectManager) {
    	
        super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    
    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed * 
                ((Level) this.getParent()).getSpeed());
    }
    
    @Override
    public void setPosition(final float pX, final float pY) {
    	super.setPosition(pX, pY);
    	this.setZIndex((int)pY);
    	if (this.hasParent()) {
    		this.getParent().sortChildren();
    	}
    }
}

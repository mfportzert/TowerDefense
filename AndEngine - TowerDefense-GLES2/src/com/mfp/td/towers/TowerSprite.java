/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.mfp.td.scenes.Level;

/**
 *
 * @author M-F.P
 */
public class TowerSprite extends AnimatedSprite {
    
    private ClickDetector mClickDetector;
    
    private final Sprite mRangeSprite;
    
    public TowerSprite(final float pX, final float pY, 
            final TiledTextureRegion pTiledTextureRegion,
            final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject,
            final Sprite pRangeSprite) {
        
        super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject);
        
        this.mRangeSprite = pRangeSprite;
    }
    
    public TowerSprite(final float pX, final float pY, 
            final TiledTextureRegion pTiledTextureRegion,
            final VertexBufferObjectManager pVertexBufferObjectManager,
            final Sprite pRangeSprite) {
        
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        
        this.mRangeSprite = pRangeSprite;
    }
    
    public TowerSprite(final float pX, final float pY, 
    		final float pWidth,
    		final float pHeight,
            final TiledTextureRegion pTiledTextureRegion,
            final VertexBufferObjectManager pVertexBufferObjectManager,
            final Sprite pRangeSprite) {
        
        super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
        
        this.mRangeSprite = pRangeSprite;
    }
    
    public void setClickDetector(final ClickDetector pClickDetector) {
        this.mClickDetector = pClickDetector;
    }
    
    public Sprite getRangeSprite() {
        return this.mRangeSprite;
    }
    
    @Override
    public boolean onAreaTouched(final TouchEvent pEvent, float pX, float pY){
        
        return this.mClickDetector.onTouchEvent(pEvent);
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

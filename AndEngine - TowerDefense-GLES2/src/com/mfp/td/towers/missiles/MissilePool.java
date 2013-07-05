/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers.missiles;

import java.util.Properties;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemorySpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

import com.mfp.td.towers.missiles.Missile.MissileType;

/**
 *
 * @author M-F.P
 */
public class MissilePool extends GenericPool<Missile> {
	
	// ===========================================================
	// Fields
	// ===========================================================
	
    private final MissileType mType;
    private final Properties mProperties;
    private final TextureRegion mTextureRegion;
    private final ISpriteVertexBufferObject mSharedVertexBuffer;
    private final VertexBufferObjectManager mVertexBufferObjectManager;
    
    // ===========================================================
 	// Constructors
 	// ===========================================================
    
    /**
     * 
     * @param pType
     * @param pProperties
     * @param pTextureRegion
     */
    public MissilePool(final MissileType pType, 
	    final Properties pProperties,
            final TextureRegion pTextureRegion,
            final VertexBufferObjectManager pVertexBufferObjectManager) {
        
        this.mType = pType;
        this.mProperties = pProperties;
        this.mTextureRegion = pTextureRegion;
        
        this.mSharedVertexBuffer = new LowMemorySpriteVertexBufferObject(
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
    protected Missile onAllocatePoolItem() {
	
        final MissileSprite sprite = new MissileSprite(0, 0, 
        	this.mTextureRegion,
        	/*this.mSharedVertexBuffer*/
        	this.mVertexBufferObjectManager);
        
        return new Missile(this.mType, this.mProperties, sprite);
    }
    
    @Override
    protected void onHandleRecycleItem(final Missile pMissile) {
        pMissile.getSprite().setIgnoreUpdate(true);
        pMissile.getSprite().setVisible(false);
    }
    
    @Override
    protected void onHandleObtainItem(final Missile pMissile) {        
        pMissile.getSprite().reset();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers.missiles;

import java.util.Properties;

import com.mfp.td.utils.IndexPropertyConstants;

/**
 *
 * @author M-F.P
 */
public class Missile {
    
    /*
     *
     * Faire 2 types de MissilesSprite :
     * 
     * Animated et Static
     * 
     * Ajouter dans les propriétés un champ sprite_type=animated/static
     * 
     * interpréter ce champ pour créer soit un TowerSprite statique soit animated
     * 
     */
	
	// ===========================================================
    // Fields
    // ===========================================================
	
    private MissileType mType;
    private MissileSprite mSprite;
    private Properties mProperties;
    private final int mDamage;
    
    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * 
     * @param pType
     * @param pProperties
     * @param pSprite
     */
    public Missile(final MissileType pType, final Properties pProperties, 
            final MissileSprite pSprite) {
        
        this.mType = pType;
        this.mProperties = pProperties;
        this.mSprite = pSprite;
        this.mDamage = Integer.valueOf(pProperties.getProperty(
                IndexPropertyConstants.DAMAGE));
    }
    /*
    public Missile(MissileType pMissileType, Properties pMissileProperties, 
            final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion) {
        
        this.mMissileType = pMissileType;
        this.mMissileProperties = pMissileProperties;
        this.mMissileSprite = new MissileSprite(pX, pY, pTextureRegion.deepCopy());
    }*/
    
    // ===========================================================
 	// Getter & Setter
 	// ===========================================================
    
    public MissileType getType() {
        return mType;
    }
    
    public MissileSprite getSprite() {
        return this.mSprite;
    }
    
    public int getDamage() {
        return this.mDamage;
    }
    
    // ===========================================================
 	// Methods
 	// ===========================================================
    
    /**
     * 
     * @param pIndex
     * @return
     */
    public String getProperty(final String pIndex) {
        return this.mProperties.getProperty(pIndex);
    }
    
    // ===========================================================
 	// Inner and Anonymous Classes
 	// ===========================================================
    
    public static enum MissileType {
        
        ARROW_SIMPLE,
        ARROW_FIRE;
    }
}

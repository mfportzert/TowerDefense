/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters.blood;

import java.util.Properties;

/**
 *
 * @author M-F.P
 */
public class Blood {
    
	// ===========================================================
    // Fields
    // ===========================================================
	
    private final BloodType mType;
    private final BloodSprite mSprite;
    private final Properties mProperties;
    
    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * 
     * @param pType
     * @param pProperties
     * @param pSprite
     */
    public Blood(final BloodType pType, final Properties pProperties, 
            final BloodSprite pSprite) {
        
        this.mType = pType;
        this.mProperties = pProperties;
        this.mSprite = pSprite;
    }
    /*
    public Blood(final BloodType pType, final Properties pProperties, 
            final BloodStaticSprite pSprite) {
        
        this.mType = pType;
        this.mProperties = pProperties;
        this.mSprite = pSprite;
    }*/
    
    // ===========================================================
 	// Getter & Setter
 	// ===========================================================
    
    public BloodType getType() {
        return this.mType;
    }
    
    public BloodSprite getSprite() {
        return this.mSprite;
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
        return this.mProperties.getProperty(pIndex, "");
    }
    
    // ===========================================================
  	// Inner and Anonymous Classes
  	// ===========================================================
    
    public enum BloodType {
        
        HIT_SPLASH,
        DEATH_SPLASH;
    }
}

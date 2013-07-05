package com.mfp.td.monsters.blood;

/**
 * TextureRegions qui ne sont pas associées à des fichiers de properties
 * 
 * @author M-F.P
 *
 */
public enum BloodRegion {
    
    DEATH_SPLASH("death_splash.png", 1, 2),
    HIT_SPLASH("hit_splash.png", 2, 4);
    
    private final int mRows;
    private final int mColumns;
    private final String mSrcName;
    
    private BloodRegion(final String pSrcName, final int pRows, final int pColumns) {
        
    	this.mRows = pRows;
		this.mColumns = pColumns;
		this.mSrcName = pSrcName;
    } 
    
    public String getSrcName() {
        return this.mSrcName;
    }
    
    public int getRows() {
        return this.mRows;
    }
    
    public int getColumns() {
        return this.mColumns;
    }
}

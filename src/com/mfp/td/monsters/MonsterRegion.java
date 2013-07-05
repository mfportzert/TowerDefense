package com.mfp.td.monsters;

/**
 * TextureRegions qui ne sont pas associées à des fichiers de properties
 * 
 * @author M-F.P
 *
 */
public enum MonsterRegion {
    
    ZOMBIE("zombie.png", 1, 4);
    
    private final int mRows;
    private final int mColumns;
    private final String mSrcName;
    
    private MonsterRegion(final String pSrcName, final int pRows, final int pColumns) {
        
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

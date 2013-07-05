/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers.missiles;

/**
 * TexturesRegions qui ne sont pas associées à des fichiers de propriétés
 * 
 * @author M-F.P
 */
public enum MissileRegion {
    
    ARROW_FIRE("arrow_fire.png", 1, 1),
    ARROW_SIMPLE("arrow_simple.png", 1, 1);
    
    private final int mRows;
    private final int mColumns;
    private final String mSrcName;
    
    private MissileRegion(final String pSrcName,  final int pRows, final int pColumns) {
        
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

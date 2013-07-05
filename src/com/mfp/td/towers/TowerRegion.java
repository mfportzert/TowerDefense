/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers;

/**
 * TexturesRegions qui ne sont pas associées à des fichiers de propriétés
 * 
 * @author M-F.P
 */
public enum TowerRegion {
    
	ARROW_TOWER("arrow_tower.png", 1, 2),
	FIRE_ARROW_TOWER("fire_arrow_tower.png", 1, 2),
	
    BUILDING_CLOUD("building_cloud.png", 1, 2),
    
    RANGE("range.png", 1, 1);
    
    private final int mRows;
    private final int mColumns;
    private final String mSrcName;
    
    private TowerRegion(final String pSrcName,  final int pRows, final int pColumns) {
        
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

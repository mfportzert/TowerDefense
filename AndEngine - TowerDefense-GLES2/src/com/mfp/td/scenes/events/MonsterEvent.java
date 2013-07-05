/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.scenes.events;

import com.mfp.td.monsters.Monster.MonsterType;

/**
 *
 * @author M-F.P
 */
public class MonsterEvent {
    
	private final int mRoadId;
    private final int mMilliSecUntilNext;
    private final MonsterType mType;
    
    /**
     * 
     * @param pX
     * @param pY
     * @param pMilliSecUntilNext
     * @param pType
     */
    public MonsterEvent(final int pRoadId, 
    		final int pMilliSecUntilNext, final MonsterType pType) {
        
        this.mRoadId = pRoadId;
        this.mMilliSecUntilNext = pMilliSecUntilNext;
        this.mType = pType;
    }
    
    public int getMilliSecUntilNext() {
        return this.mMilliSecUntilNext;
    }
    
    public MonsterType getType() {
        return this.mType;
    }
    
    public int getRoadId() {
    	return this.mRoadId;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.scenes.events;

import java.util.ArrayList;

/**
 *
 * @author M-F.P
 */
public class Wave {
    
    private ArrayList<MonsterEvent> mMonsterEvents;
    
    /**
     * 
     */
    public Wave() {
        
        this.mMonsterEvents = new ArrayList<MonsterEvent>();
    }
    
    /**
     * 
     * @return
     */
    public ArrayList<MonsterEvent> getMonsterEvents() {
        return this.mMonsterEvents;
    }
    
    /**
     * 
     * @param pMonsterEvent
     */
    public void add(MonsterEvent pMonsterEvent) {
        this.mMonsterEvents.add(pMonsterEvent);
    }
}

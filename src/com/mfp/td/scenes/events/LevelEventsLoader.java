/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.scenes.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.entity.IEntity;
import org.andengine.util.SAXUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import android.content.Context;

import com.mfp.td.monsters.Monster.MonsterType;
import com.mfp.td.scenes.Level;

/**
 *
 * @author M-F.P
 */
public class LevelEventsLoader {
    
	private static final String TAG_LEVEL_ATTRIBUTE_MONEY = "money";
	
    private static final String TAG_WAVE = "wave";
    
    private static final String TAG_ROAD = "road";
    private static final String TAG_ROAD_ATTRIBUTE_X = "x";
    private static final String TAG_ROAD_ATTRIBUTE_Y = "y";
    private static final String TAG_ROAD_ATTRIBUTE_ID = "id";
    
    private static final String TAG_TO = "to";
    private static final String TAG_TO_ATTRIBUTE_X = "x";
    private static final String TAG_TO_ATTRIBUTE_Y = "y";
    
    private static final String TAG_MONSTER = "monster";
    private static final String TAG_MONSTER_ATTRIBUTE_ROAD = "road";
    private static final String TAG_MONSTER_ATTRIBUTE_TIME = "time";
    private static final String TAG_MONSTER_ATTRIBUTE_TYPE = "type";
    
    private final LevelLoader mLevelLoader;
    private LevelEvents mLevelEvents;
    
    /**
     * 
     */
    public LevelEventsLoader() {
        
        this.mLevelLoader = new LevelLoader();
        this.mLevelLoader.setAssetBasePath("level/");
        
        this.registerEntityLoaders();
    }
    
    private void registerEntityLoaders() {
        
    	this.mLevelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new IEntityLoader() {
                @Override
                public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
                    
                	final int money = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_LEVEL_ATTRIBUTE_MONEY);
                	LevelEventsLoader.this.mLevelEvents.getLevel().setMoney(money);
                	return null;
                }
        });
        
    	this.mLevelLoader.registerEntityLoader(TAG_WAVE, new IEntityLoader() {
                @Override
                public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
                    
                    final Wave wave = new Wave();
                    LevelEventsLoader.this.mLevelEvents.getWaves().add(wave);
                    return null;
                }
        });
    	
    	this.mLevelLoader.registerEntityLoader(TAG_ROAD, new IEntityLoader() {
	            @Override
	            public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
	                
	                final float x = SAXUtils.getFloatAttributeOrThrow(pAttributes, TAG_ROAD_ATTRIBUTE_X);
                    final float y = SAXUtils.getFloatAttributeOrThrow(pAttributes, TAG_ROAD_ATTRIBUTE_Y);
	                
                    final Road road = new Road();
                    road.add(x, y);
                    
	                LevelEventsLoader.this.mLevelEvents.getRoads().add(road);
	                return null;
	            }
	    });
    	
    	this.mLevelLoader.registerEntityLoader(TAG_TO, new IEntityLoader() {
	            @Override
	            public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
	                
	            	final float x = SAXUtils.getFloatAttributeOrThrow(pAttributes, TAG_TO_ATTRIBUTE_X);
                    final float y = SAXUtils.getFloatAttributeOrThrow(pAttributes, TAG_TO_ATTRIBUTE_Y);
                    
                    final ArrayList<Road> listRoads = LevelEventsLoader.this.mLevelEvents.getRoads();
                    if (!listRoads.isEmpty()) {
                        final Road currentRoad = listRoads.get(listRoads.size() - 1);
                        currentRoad.add(x, y);
                    }
                    return null;
	            }
	    });
        
    	this.mLevelLoader.registerEntityLoader(TAG_MONSTER, new IEntityLoader() {
                @Override
                public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
                	
                	final int roadId = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_MONSTER_ATTRIBUTE_ROAD);
                    final int time = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_MONSTER_ATTRIBUTE_TIME);
                    final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_MONSTER_ATTRIBUTE_TYPE);
                    final MonsterType monsterType = MonsterType.valueOf(type);
                    
                    final MonsterEvent monsterEvent = new MonsterEvent(roadId, time, monsterType);
                    final ArrayList<Wave> listWaves = LevelEventsLoader.this.mLevelEvents.getWaves();
                    if (!listWaves.isEmpty()) {
                        final Wave currentWave = listWaves.get(listWaves.size() - 1);
                        currentWave.add(monsterEvent);
                    }
                    
                    final HashMap<MonsterType, Integer> levelMonstersAmounts = 
                    		LevelEventsLoader.this.mLevelEvents.getLevelMonstersAmounts();
                    
                    if (levelMonstersAmounts.containsKey(monsterType)) {
                    	levelMonstersAmounts.put(monsterType, 
                    			levelMonstersAmounts.get(monsterType) + 1);
                    } else {
                    	levelMonstersAmounts.put(monsterType, 1);
                    }
                    return null;
                }
        });
    }
    
    /**
     * 
     * @param pContext
     * @param filename
     * @return
     */
    public LevelEvents createLevelEvents(final Level pLevel, final Context pContext, 
            final String filename) {
        
        try {
            this.mLevelEvents = new LevelEvents(pLevel);
            this.mLevelLoader.loadLevelFromAsset(pContext.getAssets(), filename);
        } catch (final IOException e) {
            Debug.e(e);
        }
        
        return this.mLevelEvents;
    }
}

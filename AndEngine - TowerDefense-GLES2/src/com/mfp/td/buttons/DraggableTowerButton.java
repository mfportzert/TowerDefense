/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.buttons;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.math.MathUtils;

import com.mfp.td.gui.GameGUI;
import com.mfp.td.scenes.Level;
import com.mfp.td.towers.Tower;
import com.mfp.td.towers.Tower.TowerType;

/**
 *
 * @author M-F.P
 */
public class DraggableTowerButton extends Button {
    
    // ===========================================================
    // Constants
    // ===========================================================
    
    // ===========================================================
    // Fields
    // ===========================================================
    
    private boolean mGrabbed;
    private boolean mActivated;
    
    private float mOffsetX;
    private float mOffsetY;
    
    private float mExtraOffsetY;
    
    private float mNonScrollableRadius;
    
    private final TowerType mTowerType;
    private Tower mCurrentCreatedTower;
    
    /*
    private HoldDetector mHold = new HoldDetector(new IHoldDetectorListener() {
                   
        @Override
        public void onHoldFinished(HoldDetector pHoldDetector,
                long pHoldTimeMilliseconds, float pHoldX, float pHoldY) {
            
            Log.d(TAG, "\n\n\nON HOLD FINISHED PASS\n\n\n");
        }

        @Override
        public void onHold(HoldDetector pHoldDetector, long pHoldTimeMilliseconds,
                float pHoldX, float pHoldY) {
            
            Log.d(TAG, "\n\n\nON HOLD PASS\n\n\n");
        }
    });
    */
    
    // ===========================================================
    // Constructors
    // ===========================================================
    
    /*
    public DraggableTowerButton(final float pX, final float pY, 
            final TiledTextureRegion pControlBaseTextureRegion, 
            final ITiledSpriteVertexBufferObject pTiledSpriteVertexBuffer,
            final TowerType pTowerType) {
        
        super(pX, pY, pControlBaseTextureRegion, pTiledSpriteVertexBuffer, null);
        
        this.mGrabbed = false;
        this.mTowerType = pTowerType;
        this.mNonScrollableRadius = 60;
        //this.mHold.setTriggerHoldMinimumMilliseconds(200);
    }
    */
    public DraggableTowerButton(final float pX, final float pY, 
            final TiledTextureRegion pControlBaseTextureRegion, 
            final VertexBufferObjectManager pVertexBufferObjectManager,
            final TowerType pTowerType) {
        
        super(pX, pY, pControlBaseTextureRegion, pVertexBufferObjectManager, null);
        
        this.mGrabbed = false;
        this.mActivated = true;
        this.mTowerType = pTowerType;
        this.mNonScrollableRadius = 60;
        //this.mHold.setTriggerHoldMinimumMilliseconds(200);
    }
    
    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    public TowerType getTowerType() {
    	return this.mTowerType;
    }
    
    public boolean getActivated() {
    	return this.mActivated;
    }
    
    public void setActivated(final boolean pBoolean) {
    	this.mActivated = pBoolean;
    }
    
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    
    @Override
    public boolean onAreaTouched(final TouchEvent pTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
    	
        GameGUI gui = ((GameGUI) getParent());
        if (this.mActivated && !gui.isLocked()) {
            
            switch (pTouchEvent.getAction()) {
                case TouchEvent.ACTION_DOWN:
                    if (!this.mGrabbed) {
                        this.mGrabbed = true;
                        this.setCurrentTileIndex(this.getCurrentTileIndex() + 1);
                        this.createTower(pTouchEvent, mTowerType);
                    }
                    break;
                case TouchEvent.ACTION_MOVE:
                    if (this.mGrabbed) {
                        this.moveTower(pTouchEvent, 
                                this.getWidth(), this.getHeight());
                    }
                    break;
                case TouchEvent.ACTION_UP:
                    if (this.mGrabbed ) {
                        this.mGrabbed = false;
                        this.setCurrentTileIndex(this.getCurrentTileIndex() - 1);
                        this.dropTower(pTouchEvent);
                    }
                    break;
            }
            return true;
        }
        return false;
    }
    
    // ===========================================================
    // Methods
    // ===========================================================

    public void createTower(final TouchEvent pSceneTouchEvent, final TowerType pTowerType) {
        
        final Level level = ((GameGUI) getParent()).getLevel();
        final SmoothCamera camera = level.getCamera();

        final float touchX = pSceneTouchEvent.getX() / camera.getZoomFactor();
        final float touchY = pSceneTouchEvent.getY() / camera.getZoomFactor();

        this.mOffsetX = this.getTextureRegion().getWidth() / camera.getZoomFactor();
        this.mOffsetY = this.getTextureRegion().getHeight() / camera.getZoomFactor();
        this.mExtraOffsetY = 60 / camera.getZoomFactor();
        
        final float towerPosX = (touchX - this.mOffsetX) +
                (camera.getCenterX() - (camera.getWidth() / 2));
        final float towerPosY = (touchY - this.mOffsetY - this.mExtraOffsetY) + 
                (camera.getCenterY() - (camera.getHeight() / 2));
        
        level.hideCurrentClickedTower();
        this.mCurrentCreatedTower = level.createTower(pTowerType, towerPosX, towerPosY);
    }
    
    
    /**
     * 
     * @param pSceneTouchEvent
     * @param towerWidth
     * @param towerHeight
     */
    public void moveTower(final TouchEvent pSceneTouchEvent, final float towerWidth, final float towerHeight) {
        
        final Level level = ((GameGUI) getParent()).getLevel();
        final SmoothCamera camera = level.getCamera();
        
        final float touchX = pSceneTouchEvent.getX() / camera.getZoomFactor();
        final float touchY = pSceneTouchEvent.getY() / camera.getZoomFactor();

        this.mOffsetX = this.getTextureRegion().getWidth() / camera.getZoomFactor();
        this.mOffsetY = this.getTextureRegion().getHeight() / camera.getZoomFactor();
        this.mExtraOffsetY = 60 / camera.getZoomFactor();
        if (touchY <= (this.mOffsetY + this.mExtraOffsetY)) {
            this.mOffsetY = touchY;
            this.mExtraOffsetY = 0;
        }
        
        final float towerPosX = (touchX - this.mOffsetX) +
                (camera.getCenterX() - (camera.getWidth() / 2));
        final float towerPosY = (touchY - this.mOffsetY - this.mExtraOffsetY) + 
                (camera.getCenterY() - (camera.getHeight() / 2));
        
        if (towerPosY >= 0) {
            this.mCurrentCreatedTower.getSprite().setPosition(towerPosX, towerPosY);
        }
        
        // 12, 31 means the pixels on the sprite corresponding to the player's feet.
        final float[] lightTowerCordinates = this.mCurrentCreatedTower.getSprite().convertLocalToSceneCoordinates(12, 31);

        final TMXTiledMap tmxTiledMap = level.getTMXTiledMap();
        final TMXLayer tmxLayer = tmxTiledMap.getTMXLayers().get(0);
        final TMXTile tmxTile = tmxLayer.getTMXTileAt(
        	lightTowerCordinates[Constants.VERTEX_INDEX_X], 
        	lightTowerCordinates[Constants.VERTEX_INDEX_Y]);
        
        if (tmxTile != null) {
            
            this.tileAdjustingTower(level, tmxTiledMap, tmxTile);
            
            final float distance = Math.abs(MathUtils.distance(
                    pSceneTouchEvent.getX(), 
                    pSceneTouchEvent.getY(), 
                    this.getX() - (this.getWidth() / 2), 
                    this.getY() - (this.getHeight() / 2)
                    ));
            
            if (distance > this.mNonScrollableRadius) {
                this.checkMapScrolling(camera, touchX, touchY, 8 / camera.getZoomFactor());
            }
        }
    }
    
    public void tileAdjustingTower(final Level pLevel, 
            final TMXTiledMap pTmxTiledMap, final TMXTile pTmxTile) {
        
        // tmxTile.setTextureRegion(null); <-- Rubber-style removing of tiles =D
        this.mCurrentCreatedTower.getSprite().setPosition(pTmxTile.getTileX(), 
                pTmxTile.getTileY());
        
        float rangePosX = (this.mCurrentCreatedTower.getSprite().getX() + 
                (this.mCurrentCreatedTower.getSprite().getWidth() / 2)) - 
                (this.mCurrentCreatedTower.getRangeSprite().getWidth() / 2);
        float rangePosY = (this.mCurrentCreatedTower.getSprite().getY() + 
                (this.mCurrentCreatedTower.getSprite().getHeight() / 2)) - 
                (this.mCurrentCreatedTower.getRangeSprite().getHeight() / 2);
        
        this.mCurrentCreatedTower.getRangeSprite().setPosition(rangePosX, rangePosY);
        
        try {
            final TMXProperties<TMXTileProperty> pTMXTileProperties = 
        	    pTmxTile.getTMXTileProperties(pTmxTiledMap);
            
            if (pTMXTileProperties != null 
                    && !pTMXTileProperties.isEmpty() 
                    && pTMXTileProperties.containsTMXProperty("free", "true") 
                    && !pLevel.isCollidingWithAnotherTower(
                	    this.mCurrentCreatedTower.getSprite())) {
                this.mCurrentCreatedTower.setDroppable(true);
            } else {
                this.mCurrentCreatedTower.setDroppable(false);
            }
        } catch (IllegalArgumentException ex) {
            this.mCurrentCreatedTower.setDroppable(false);
        }
    }
    
    public void checkMapScrolling(final SmoothCamera pCamera, final float pTouchX,
            final float pTouchY, final float pScrollPixelValue) {
        
        final float centerX = pCamera.getCenterX();
        final float centerY = pCamera.getCenterY();
        
        final float offsetScroll = 80 / pCamera.getZoomFactor();
        final float offsetScrollLeft = 160 / pCamera.getZoomFactor();
        final float offsetScrollBottom = 40 / pCamera.getZoomFactor();
        
        if (pTouchX > pCamera.getWidth() - offsetScroll) {
            pCamera.setCenter(centerX + pScrollPixelValue, centerY);
        } else if (pTouchX < offsetScrollLeft) {
            pCamera.setCenter(centerX - pScrollPixelValue, centerY);
        } else if (pTouchY > pCamera.getHeight() - offsetScrollBottom) {
            pCamera.setCenter(centerX, centerY + pScrollPixelValue);
        } else if (pTouchY < offsetScroll) {
            pCamera.setCenter(centerX, centerY - pScrollPixelValue);
        }
    }
    
    /**
     * 
     * @param pSceneTouchEvent
     */
    public void dropTower(final TouchEvent pSceneTouchEvent) {
        
        final Level level = ((GameGUI) getParent()).getLevel();

        if (this.mCurrentCreatedTower.isDroppable()) {
        	
        	level.updateMoney(-this.mCurrentCreatedTower.getPrice());
            this.mCurrentCreatedTower.getRangeSprite().setVisible(false);
            this.mCurrentCreatedTower.build();
        } else {
            level.removeTower(this.mCurrentCreatedTower);
        }
    }
}

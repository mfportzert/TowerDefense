/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers;

import java.util.Properties;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.time.TimeConstants;

import com.mfp.td.monsters.Monster;
import com.mfp.td.scenes.Level;
import com.mfp.td.towers.buildingclouds.BuildingCloudPool;
import com.mfp.td.towers.missiles.Missile;
import com.mfp.td.towers.missiles.Missile.MissileType;
import com.mfp.td.towers.missiles.MissileFactory;
import com.mfp.td.utils.IndexPropertyConstants;
import com.mfp.td.utils.PropertiesUtils;

/**
 *
 * @author M-F.P
 */
public class Tower implements TimeConstants, IClickDetectorListener {

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mActive;
	private boolean mReady;
	private boolean mReloading;
	private boolean mDroppable;

	private float mReloadSpeed;
	
	private Level mLevel;
	
	private final int mPrice;
	
	private final Sound mFireSound;
	private final TowerSprite mSprite;
	private final TowerType mType;
	private final Properties mProperties;
	private final MissileType mMissileType;
	private final MissileFactory mMissileFactory;
	private final BuildingCloudPool mBuildingCloudPool;

	private AnimatedSprite mTmpBuildingCloudSprite;
	
	private TimerHandler mReloadTimerHandler;
	private final ClickDetector mClickDetector;

	private final TowerGUI mGUI;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Tower(final TowerType pType, 
			final Properties pProperties, 
			final Sound pFireSound, 
			final MissileFactory pMissileFactory, 
			final BuildingCloudPool pBuildingCloudPool,
			final TowerSprite pSprite,
			final TowerGUI pGUI) {

		this.mReady = false;
		this.mActive = false;
		this.mReloading = false;
		this.mDroppable = true;

		this.mGUI = pGUI;
		this.mType = pType;
		this.mSprite = pSprite;
		this.mFireSound = pFireSound;
		this.mProperties = pProperties;
		this.mMissileFactory = pMissileFactory;
		this.mBuildingCloudPool = pBuildingCloudPool;

		if (PropertiesUtils.parseInt(this.mProperties, 
				IndexPropertyConstants.TEXTURE_ANIMATED) == 1) {
			this.mSprite.animate(200, true);
		}

		this.mMissileType = MissileType.valueOf(
				pProperties.getProperty(IndexPropertyConstants.MISSILE_TYPE));
		this.mReloadSpeed = PropertiesUtils.parseFloat(
				pProperties, IndexPropertyConstants.RATE) / 1000;
		this.mPrice = PropertiesUtils.parseInt(
				pProperties, IndexPropertyConstants.PRICE);
		
		this.mClickDetector = new ClickDetector(this);
		this.mSprite.setClickDetector(this.mClickDetector);

		this.mLevel = (Level) this.mSprite.getParent();
		this.mSprite.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {
			}

			@Override
			public void onUpdate(final float pSecondsElapsed) {

				if (Tower.this.mActive) {

					for (Monster monster : Tower.this.mLevel.getMonsters()) {

						if (!monster.isDead() && Tower.this.getRangeSprite().collidesWith(monster.getSprite())) {

							if (Tower.this.mReady) {
								Tower.this.shoot(monster);
							} else {
								if (!Tower.this.mReloading) {
									Tower.this.reload();
								}
							}
							break;
						}
					}
				}
			}
		});
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isReloading() {
		return this.mReloading;
	}

	public boolean isReady() {
		return this.mReady;
	}

	public boolean isActive() {
		return this.mActive;
	}

	public int getPrice() {
		return this.mPrice;
	}
	
	public void setActive(boolean pActive) {
		this.mActive = pActive;
	}
	
	public void setLevel(final Level pLevel) {
		this.mLevel = pLevel;
	}
	
	public Properties getProperties() {
		return this.mProperties;
	}
	
	public String getProperty(final String pIndex) {
		return this.mProperties.getProperty(pIndex);
	}

	public TowerType getType() {
		return this.mType;
	}

	public String getName() {
		return this.getProperty(IndexPropertyConstants.NAME);
	}

	public TowerSprite getSprite() {
		return this.mSprite;
	}

	public TowerGUI getGUI() {
		return this.mGUI;
	}

	public Sprite getRangeSprite() {
		return this.mSprite.getRangeSprite();
	}

	public void setDroppable(final boolean pDroppable) {

		if (pDroppable != this.mDroppable) {

			if (this.mDroppable = pDroppable) {
				this.mSprite.getRangeSprite().setColor(0, 1, 0);
			} else {
				this.mSprite.getRangeSprite().setColor(1, 0, 0);
			}
		}
	}

	public boolean isDroppable() {
		return this.mDroppable;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {

		if (this.mActive) {

			if (getRangeSprite().isVisible()) {
				getRangeSprite().setVisible(false);
				this.mGUI.display(false);
			} else {
				this.mLevel.setCurrentClickedTower(this);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void reload() {

		if (!this.mReloading) {

			this.mReloading = true;
			if (this.mReloadTimerHandler == null) {
				this.mReloadTimerHandler = new TimerHandler(this.mReloadSpeed, false,
						new ITimerCallback() {

					@Override
					public void onTimePassed(final TimerHandler reloadTimerHandler) {
						//pEngine.unregisterUpdateHandler(Tower.this.mReloadTimerHandler);
						Tower.this.mReady = true;
						Tower.this.mReloading = false;
					}
				});
				this.mLevel.registerUpdateHandler(this.mReloadTimerHandler);
			} else {
				this.mReloadTimerHandler.reset();
			}
		}
	}

	public void shoot(final Monster pTarget) {

		if (this.mReady) {

			this.mFireSound.play();
			final Missile missile = this.mMissileFactory.createFromType(this.mMissileType,
					this.mSprite.getX(), this.mSprite.getY());
			if (!missile.getSprite().hasParent()) {
				this.mLevel.attachChild(missile.getSprite());
			}

			float angle = (float) (Math.atan2(this.mSprite.getY() - pTarget.getSprite().getY(),
					this.mSprite.getX() - pTarget.getSprite().getX()) * 180 / Math.PI);

			missile.getSprite().setRotation(angle);

			float speed = Float.valueOf(missile.getProperty(IndexPropertyConstants.SPEED)) / 1000;
			float distance = MathUtils.distance(missile.getSprite().getX(),
					missile.getSprite().getY(), pTarget.getSprite().getX(), pTarget.getSprite().getY());
			float timeToTarget = (distance * speed) / this.mLevel.getSpeed();

			final float impactPosX = pTarget.getSprite().getX();
			final float impactPosY = pTarget.getSprite().getY();

			final float fromX = missile.getSprite().getX();
			final float fromY = missile.getSprite().getY();
			final float toX = impactPosX + pTarget.getSprite().getWidth() / 2;
			final float toY = impactPosY + pTarget.getSprite().getHeight() / 2;
			MoveModifier fireModifier = new MoveModifier(timeToTarget,
					fromX, toX, 
					fromY, toY,
					new IEntityModifier.IEntityModifierListener() {

				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
					//setPosition(missile.getSprite().getX(), missile.getSprite().getY());
				}

				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

					Tower.this.mMissileFactory.recycle(missile);
					if (missile.getSprite().collidesWith(pTarget.getSprite())) {

						/*
						 * Pour les positions, comme le sang est attaché au sprite,
						 * on est obligé de soustraire la différence de position 
						 * du monstre entre le moment du lancement du missile 
						 * et le moment de l impact
						 */
						pTarget.receiveDamage(missile.getDamage(), 
								pTarget.getSprite().getWidth() / 2 -
								(pTarget.getSprite().getX() - impactPosX), 
								pTarget.getSprite().getHeight() / 2 -
								(pTarget.getSprite().getY() - impactPosY));
					}
				}
			});

			missile.getSprite().registerEntityModifier(fireModifier);
			this.mReady = false;
		}
	}

	public void build() {

		if (!this.mActive) {
			
			this.mGUI.getDeleteButton().setVisible(true);
			this.mGUI.getDeleteButton().getClickDetector().setEnabled(true);
			
			this.mTmpBuildingCloudSprite = this.mBuildingCloudPool.obtainPoolItem();
			this.mTmpBuildingCloudSprite.setZIndex(this.mSprite.getZIndex() + 1);
			this.mTmpBuildingCloudSprite.animate((long) (300 / this.mLevel.getSpeed()), 
					PropertiesUtils.parseInt(this.mProperties, IndexPropertyConstants.BUILD_TIME),
					new AnimatedSprite.IAnimationListener() {
				
				@Override
				public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
						int pInitialLoopCount) {
					
				}

				@Override
				public void onAnimationFrameChanged(
						AnimatedSprite pAnimatedSprite, int pOldFrameIndex,
						int pNewFrameIndex) {
					
				}

				@Override
				public void onAnimationLoopFinished(
						AnimatedSprite pAnimatedSprite,
						int pRemainingLoopCount, int pInitialLoopCount) {
					
				}

				@Override
				public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
					
					Tower.this.mBuildingCloudPool.recyclePoolItem(pAnimatedSprite);
					
					Tower.this.mGUI.getDeleteButton().setVisible(false);
					Tower.this.mGUI.getDeleteButton().getClickDetector().setEnabled(false);
					
					Tower.this.mActive = true;
					Tower.this.mTmpBuildingCloudSprite = null;
				}
			});

			if (!this.mTmpBuildingCloudSprite.hasParent()) {                
				this.mLevel.attachChild(this.mTmpBuildingCloudSprite);
			}

			//this.mTmpBuildingCloudSprite.setZIndex(this.mLevel.getChildCount() - 1);
			this.mTmpBuildingCloudSprite.setPosition(this.mSprite.getX(), this.mSprite.getY());
		}
	}
	
	public void destroy() {
		
		if (!this.mActive) {
			
			this.mTmpBuildingCloudSprite.stopAnimation();
			this.mBuildingCloudPool.recyclePoolItem(this.mTmpBuildingCloudSprite);
			this.mTmpBuildingCloudSprite = null;
			
			this.mLevel.updateMoney(this.mPrice);
		} else {
			this.mLevel.updateMoney(3 * (this.mPrice / 4));
		}
		
		this.mLevel.removeTower(this);
	}
	
	// ===========================================================
 	// Inner and Anonymous Classes
 	// ===========================================================
	
	public static enum TowerType {
		   
	    ARROW_TOWER,
	    FIRE_ARROW_TOWER;
	}
}

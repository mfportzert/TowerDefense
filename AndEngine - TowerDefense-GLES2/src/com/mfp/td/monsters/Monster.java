/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.monsters;

import java.util.Properties;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;

import com.mfp.td.monsters.blood.Blood;
import com.mfp.td.monsters.blood.Blood.BloodType;
import com.mfp.td.monsters.blood.BloodFactory;
import com.mfp.td.scenes.Level;
import com.mfp.td.utils.IndexPropertyConstants;
import com.mfp.td.utils.PropertiesUtils;

/**
 *
 * @author M-F.P
 */
public class Monster {

	// ===========================================================
    // Fields
    // ===========================================================
	
	private boolean mDead;

	private int mHitPoints;
	private int mTotalHitPoints;
	
	private final int mSpeed;

	private final MonsterType mType;
	private final MonsterSprite mSprite;
	private final Blood mBlood;
	private final BloodFactory mBloodFactory;
	private final Properties mProperties;
	private final Rectangle mBackgroundHealthBar;
	private final Rectangle mHealthBar;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * 
	 * @param pType
	 * @param pProperties
	 * @param pBloodFactory
	 * @param pSprite
	 */
	public Monster(final MonsterType pType, 
			final Properties pProperties, 
			final BloodFactory pBloodFactory, 
			final MonsterSprite pSprite,
			final Rectangle pHealthBar) {

		this.mDead = false;
		this.mType = pType;
		this.mProperties = pProperties;
		this.mBloodFactory = pBloodFactory;
		this.mSprite = pSprite;

		this.mSpeed = PropertiesUtils.parseInt(pProperties, 
				IndexPropertyConstants.SPEED);
		
		this.mTotalHitPoints = PropertiesUtils.parseInt(pProperties, 
				IndexPropertyConstants.HITPOINTS);
		this.mHitPoints = this.mTotalHitPoints;

		this.mBlood = this.mBloodFactory.createFromType(BloodType.valueOf(
				this.mProperties.getProperty(IndexPropertyConstants.BLOOD_TYPE)),
				-5, -5);

		this.mBlood.getSprite().setVisible(false);
		//this.mSprite.attachChild(this.mBlood.getSprite());

		this.mHealthBar = pHealthBar;
		this.mBackgroundHealthBar = new Rectangle(
				pHealthBar.getX(),
				pHealthBar.getY(),
				pHealthBar.getWidth(),
				pHealthBar.getHeight(),
				pHealthBar.getVertexBufferObjectManager());
		
		this.mHealthBar.setColor(0, 0.8f, 0);
		this.mBackgroundHealthBar.setColor(0.8f, 0, 0);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public MonsterSprite getSprite() {
		return this.mSprite;
	}

	public MonsterType getType() {
		return this.mType;
	}

	public Blood getBlood() {
		return this.mBlood;
	}
	
	public int getSpeed() {
		return this.mSpeed;
	}

	public Rectangle getBackgroundHealthBar() {
		return this.mBackgroundHealthBar;
	}

	public Rectangle getHealthBar() {
		return this.mHealthBar;
	}

	public boolean isDead() {
		return mDead;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * 
	 */
	public void reset() {

		this.mSprite.setIgnoreUpdate(false);
		this.mSprite.setVisible(true);
		this.mHealthBar.setIgnoreUpdate(false);
		this.mHealthBar.setVisible(true);
		this.mBackgroundHealthBar.setIgnoreUpdate(false);
		this.mBackgroundHealthBar.setVisible(true);

		this.mDead = false;
		this.mHitPoints = this.mTotalHitPoints;
		this.mHealthBar.setWidth(this.mSprite.getWidth() / 2);
	}

	/**
	 * 
	 * @param pHitDamage
	 */
	public void receiveDamage(final int pHitDamage, final float pX, final float pY) {

		this.mHitPoints -= pHitDamage;
		this.bleed(pX, pY);
		if (this.mHitPoints <= 0) {
			die();
		} else {
			float percLife = (((float) this.mHitPoints) / ((float) this.mTotalHitPoints));
			this.mHealthBar.setWidth((mSprite.getWidth() / 2) * percLife);
		}
	}

	/**
	 * 
	 * @param pIndex
	 * @return
	 */
	public String getProperty(final String pIndex) {
		return this.mProperties.getProperty(pIndex, "");
	}

	/**
	 * 
	 * @return
	 */
	public Blood createDeathSplashBlood() {

		final Blood blood = this.mBloodFactory.createFromType(BloodType.DEATH_SPLASH, 
				this.mSprite.getX(), this.mSprite.getY());
		return blood;
	}

	private void die() {

		this.mDead = true;

		Blood deathSplashBlood = this.createDeathSplashBlood();
		Level level = (Level) this.mSprite.getParent();
		level.attachChild(deathSplashBlood.getSprite());
		level.removeMonster(this);
	}

	private void bleed(final float pX, final float pY) {

		this.mBlood.getSprite().setVisible(true);
		this.mBlood.getSprite().setPosition(
				pX - this.mBlood.getSprite().getWidth(), 
				pY - this.mBlood.getSprite().getHeight());
		this.mBlood.getSprite().animate(60, false,
				new AnimatedSprite.IAnimationListener() {

			@Override
			public void onAnimationStarted(
					AnimatedSprite pAnimatedSprite,
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
			public void onAnimationFinished(
					AnimatedSprite pAnimatedSprite) {
				pAnimatedSprite.setVisible(false);
			}
		});
	}
	
	// ===========================================================
  	// Inner and Anonymous Classes
  	// ===========================================================
	
	public enum MonsterType {
        
	    ZOMBIE;
	}
}

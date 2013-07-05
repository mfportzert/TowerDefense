/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers;

import java.util.Properties;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.primitive.vbo.LowMemoryRectangleVertexBufferObject;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemoryTiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

import com.mfp.td.buttons.Button.ButtonType;
import com.mfp.td.buttons.ButtonFactory;
import com.mfp.td.buttons.ClickableButton;
import com.mfp.td.towers.Tower.TowerType;
import com.mfp.td.towers.buildingclouds.BuildingCloudPool;
import com.mfp.td.towers.missiles.MissileFactory;
import com.mfp.td.utils.IndexPropertyConstants;
import com.mfp.td.utils.PropertiesUtils;

/**
 *
 * @author M-F.P
 */
public class TowerPool extends GenericPool<Tower> {

	// ===========================================================
	// Fields
	// ===========================================================

	private final Sound mFireSound;
	private final TowerType mType;
	private final Properties mProperties;

	private final ButtonFactory mButtonFactory;
	private final MissileFactory mMissileFactory;

	private final BuildingCloudPool mBuildingCloudPool;
	private final TextureRegion mRangeTextureRegion;
	private final TiledTextureRegion mTowerTiledTextureRegion;

	private final ITiledSpriteVertexBufferObject mSharedVertexBuffer;
	private final IRectangleVertexBufferObject mHealthBarVertexBufferObject;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	
	private final int mRangeSizePercent;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TowerPool(final TowerType pType,
			final Properties pProperties,
			final Sound pFireSound,
			final MissileFactory pMissileFactory,
			final ButtonFactory pButtonFactory,
			final BuildingCloudPool pBuildingCloudPool,
			final TextureRegion pRangeTextureRegion,
			final TiledTextureRegion pTowerTiledTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {

		this.mType = pType;
		this.mFireSound = pFireSound;
		this.mProperties = pProperties;
		this.mButtonFactory = pButtonFactory;
		this.mMissileFactory = pMissileFactory;
		this.mBuildingCloudPool = pBuildingCloudPool;
		this.mRangeTextureRegion = pRangeTextureRegion;
		this.mTowerTiledTextureRegion = pTowerTiledTextureRegion;

		this.mRangeSizePercent = PropertiesUtils.parseInt(
				this.mProperties, IndexPropertyConstants.RANGE);

		this.mSharedVertexBuffer = new LowMemoryTiledSpriteVertexBufferObject(
				pVertexBufferObjectManager,
				Sprite.SPRITE_SIZE, 
				DrawType.DYNAMIC, 
				true, 
				Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);

		this.mHealthBarVertexBufferObject = new LowMemoryRectangleVertexBufferObject(
				pVertexBufferObjectManager,
				Sprite.SPRITE_SIZE, 
				DrawType.STATIC, 
				true, 
				Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected Tower onAllocatePoolItem() {

		final Sprite rangeSprite = new Sprite(0, 0, 
				this.mRangeTextureRegion, 
				this.mVertexBufferObjectManager
				/*this.mSharedVertexBuffer*/);
		rangeSprite.setAlpha(0.5f);
		rangeSprite.setSize(
				rangeSprite.getWidth() * (this.mRangeSizePercent / 100), 
				rangeSprite.getHeight() * (this.mRangeSizePercent / 100));

		final TowerSprite towerSprite = new TowerSprite(0, 0,
				PropertiesUtils.parseFloat(this.mProperties, IndexPropertyConstants.SIZE_WIDTH),
				PropertiesUtils.parseFloat(this.mProperties, IndexPropertyConstants.SIZE_HEIGHT),
				this.mTowerTiledTextureRegion, 
				/*this.mSharedVertexBuffer, */
				this.mVertexBufferObjectManager,
				rangeSprite);

		final Rectangle healthBar = new Rectangle(0, -0, 50, 0, 
				this.mHealthBarVertexBufferObject);		

		final ClickableButton evolutionButtonA = 
				(ClickableButton) this.mButtonFactory.createFromType(
						ButtonType.FIRE_ARROW_TOWER, -30f, -30f);
		final ClickableButton evolutionButtonB = 
				(ClickableButton) this.mButtonFactory.createFromType(
						ButtonType.ICE_ARROW_TOWER, 20f, -30f);
		final ClickableButton specialEvolutionButton = 
				(ClickableButton) this.mButtonFactory.createFromType(
						ButtonType.MULTI_ARROW, 70f, -30f);
		final ClickableButton deleteButton = 
				(ClickableButton) this.mButtonFactory.createFromType(
						ButtonType.DELETE, 70f, 30f);		
		
		final TowerGUI towerGUI = new TowerGUI(towerSprite, 
				healthBar, 
				evolutionButtonA, 
				evolutionButtonB, 
				specialEvolutionButton,
				deleteButton);

		return new Tower(this.mType, 
				this.mProperties, 
				this.mFireSound, 
				this.mMissileFactory, 
				this.mBuildingCloudPool,
				towerSprite, 
				towerGUI);
	}

	@Override
	protected void onHandleRecycleItem(final Tower pTower) {

		pTower.setActive(false);
		pTower.getSprite().setIgnoreUpdate(true);
		pTower.getSprite().setVisible(false);
		pTower.getRangeSprite().setVisible(false);
	}

	@Override
	protected void onHandleObtainItem(final Tower pTower) {

		pTower.getSprite().setIgnoreUpdate(false);
		pTower.getSprite().setVisible(true);
		if (pTower.getRangeSprite() != null) {
			pTower.getRangeSprite().setVisible(true);
		}
	}
}

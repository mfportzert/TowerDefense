/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.buttons;

import java.util.ArrayList;
import java.util.Properties;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.pool.MultiPool;

import android.content.Context;

import com.mfp.td.buttons.Button.ButtonType;
import com.mfp.td.utils.BaseFactory;
import com.mfp.td.utils.IndexPropertyConstants;
import com.mfp.td.utils.PropertiesUtils;

/**
 *
 * @author M-F.P
 */
public class ButtonFactory extends BaseFactory {

	// ===========================================================
	// Constants
	// ===========================================================

	private final String PROPERTIES_DIR = "properties/buttons/";

	// ===========================================================
	// Fields
	// ===========================================================

	private final MultiPool<Button> mButtonPools;
	private final ArrayList<Properties> mButtonProperties;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ButtonFactory(final Engine pEngine, final Context pContext) {

		super();
		this.mButtonPools = new MultiPool<Button>();
		this.mButtonProperties = new ArrayList<Properties>();
		this.loadResources(pEngine, pContext);
	}
	
	// ===========================================================
    // Getter & Setter
    // ===========================================================
	
	public TextureRegion getRegion(final ButtonRegion pRegion) {
		return this.mTextureRegionLibrary.get(pRegion.getSrcName());
	}
	
	public TiledTextureRegion getTiledRegion(final ButtonRegion pRegion) {
		return this.getTiled(pRegion.getSrcName(), pRegion.getRows(), pRegion.getColumns());
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * @param pButtonType
	 * @param pX
	 * @param pY
	 * @return
	 */
	public Button createFromType(final ButtonType pButtonType, final float pX, final float pY) {

		final Button newButton = this.mButtonPools.obtainPoolItem(pButtonType.ordinal());
		newButton.setPosition(pX, pY);

		return newButton;
	}

	/**
	 * @param pButton
	 */
	public void recycle(final ClickableButton pButton) {

		this.mButtonPools.recyclePoolItem(pButton.getType().ordinal(), pButton);
	}

	private Properties loadProperties(final Context pContext, final ButtonType pButtonType) {

		String filename = PROPERTIES_DIR+pButtonType.name().toLowerCase()+".properties";
		return PropertiesUtils.loadProperties(pContext, filename);
	}

	private void loadResources(final Engine pEngine, final Context pContext) {

		super.loadSpritesheets(pEngine, pContext, "xml/buttons.xml");

		/* Load all default basic properties in properties/towers/ directory */
		for (ButtonType buttonType : ButtonType.values()) {

			final Properties properties = loadProperties(pContext, buttonType);
			this.mButtonProperties.add(buttonType.ordinal(), properties);
			
			final TiledTextureRegion tiledTextureRegion = this.getTiledRegion(
					ButtonRegion.valueOf(
							properties.getProperty(
									IndexPropertyConstants.TEXTURE_REGION)
							)
					);

			this.mButtonPools.registerPool(buttonType.ordinal(), 
					new ButtonPool(
							buttonType, 
							properties,
							tiledTextureRegion,
							pEngine.getVertexBufferObjectManager())
					);
		}
	}
}

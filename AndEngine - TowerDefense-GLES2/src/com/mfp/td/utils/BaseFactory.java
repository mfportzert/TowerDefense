/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.utils;

import org.andengine.engine.Engine;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackerTextureRegion;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.util.Log;

/**
 *
 * @author M-F.P
 */
public class BaseFactory {

	public static final String SPRITESHEET_DIR = "gfx/spritesheets/";

	private TexturePack mTexturePack;
	protected TexturePackTextureRegionLibrary mTextureRegionLibrary;

	/**
	 * 
	 * @param pEngine
	 * @param pContext
	 * @param filename
	 */
	protected void loadSpritesheets(Engine pEngine, Context pContext, String filename) {

		try {
			this.mTexturePack = new TexturePackLoader(
					pEngine.getTextureManager(), SPRITESHEET_DIR).loadFromAsset(
							pContext.getAssets(), filename);
			
			this.mTextureRegionLibrary = this.mTexturePack.getTexturePackTextureRegionLibrary();
			this.mTexturePack.getTexture().load();

		} catch (TexturePackParseException ex) {
			Log.e("LevelFactory", ex.getMessage(), ex);
		}
	}
	
	public TiledTextureRegion getTiled(String source, final int rows, final int columns) {

		TexturePackerTextureRegion packedTextureRegion = this.mTextureRegionLibrary.get(source);

		return TiledTextureRegion.create(
				packedTextureRegion.getTexture(), 
				(int) packedTextureRegion.getTextureX(), 
				(int) packedTextureRegion.getTextureY(), 
				(int) packedTextureRegion.getWidth(), 
				(int) packedTextureRegion.getHeight(), 
				columns, 
				rows);
	}
}

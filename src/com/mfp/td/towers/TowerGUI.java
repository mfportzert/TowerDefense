/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.towers;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.input.touch.detector.ClickDetector;

import android.util.Log;

import com.mfp.td.buttons.ClickableButton;
import com.mfp.td.scenes.Level;
import com.mfp.td.towers.Tower.TowerType;

/**
 *
 * @author M-F.P
 */
public class TowerGUI {

	// ===========================================================
	// Fields
	// ===========================================================

	private final TowerSprite mTowerSprite;

	private final ClickableButton mEvolutionButtonA;
	private final ClickableButton mEvolutionButtonB;
	private final ClickableButton mSpecialEvolutionButton;

	private final Rectangle mHealthBar;

	private final ClickableButton mDeleteButton;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TowerGUI(final TowerSprite pTowerSprite, 
			final Rectangle pHealthBar, 
			final ClickableButton pEvolutionButtonA, 
			final ClickableButton pEvolutionButtonB, 
			final ClickableButton pSpecialEvolutionButton,
			final ClickableButton pDeleteButton) {

		this.mTowerSprite = pTowerSprite;

		this.mEvolutionButtonA = pEvolutionButtonA;
		this.mEvolutionButtonA.setClickDetector(new ClickDetector(
				new ClickDetector.IClickDetectorListener() {

					@Override
					public void onClick(ClickDetector pClickDetector, int pPointerID,
							float pSceneX, float pSceneY) {

						Level level = (Level) TowerGUI.this.mTowerSprite.getParent();
						level.upgradeCurrentTower(TowerType.FIRE_ARROW_TOWER);
					}
				}));

		this.mEvolutionButtonB = pEvolutionButtonB;
		this.mEvolutionButtonB.setClickDetector(new ClickDetector(
				new ClickDetector.IClickDetectorListener() {

					@Override
					public void onClick(ClickDetector pClickDetector, int pPointerID,
							float pSceneX, float pSceneY) {
						Log.d("TOWER GUI", "EVOLUTION B");
					}
				}));

		this.mSpecialEvolutionButton = pSpecialEvolutionButton;
		this.mSpecialEvolutionButton.setClickDetector(new ClickDetector(
				new ClickDetector.IClickDetectorListener() {

					@Override
					public void onClick(ClickDetector pClickDetector, int pPointerID,
							float pSceneX, float pSceneY) {
						Log.d("TOWER GUI", "EVOLUTION SPECIALE");
					}
				}));

		this.mDeleteButton = pDeleteButton;
		this.mDeleteButton.setClickDetector(new ClickDetector(
				new ClickDetector.IClickDetectorListener() {

					@Override
					public void onClick(ClickDetector pClickDetector, int pPointerID,
							float pSceneX, float pSceneY) {

						final Level level = (Level) TowerGUI.this.mTowerSprite.getParent();
						level.getTowerFromSprite(TowerGUI.this.mTowerSprite).destroy();
					}
				}));

		this.mHealthBar = pHealthBar;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ClickableButton getEvolutionButtonA() {
		return this.mEvolutionButtonA;
	}

	public ClickableButton getEvolutionButtonB() {
		return this.mEvolutionButtonB;
	}

	public ClickableButton getSpecialEvolutionButton() {
		return this.mSpecialEvolutionButton;
	}

	public ClickableButton getDeleteButton() {
		return this.mDeleteButton;
	}

	public Rectangle getHealthBar() {
		return this.mHealthBar;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void display(final boolean pBoolean) {

		this.mEvolutionButtonA.setVisible(pBoolean);
		this.mEvolutionButtonA.getClickDetector().setEnabled(pBoolean);

		this.mEvolutionButtonB.setVisible(pBoolean);
		this.mEvolutionButtonB.getClickDetector().setEnabled(pBoolean);

		this.mSpecialEvolutionButton.setVisible(pBoolean);
		this.mSpecialEvolutionButton.getClickDetector().setEnabled(pBoolean);

		this.mDeleteButton.setVisible(pBoolean);
		this.mDeleteButton.getClickDetector().setEnabled(pBoolean);
	}
}

package com.mfp.td.scenes.events;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.util.math.MathUtils;

import com.mfp.td.monsters.Monster;
import com.mfp.td.monsters.Monster.MonsterType;
import com.mfp.td.scenes.Level;


/**
 *
 * @author M-F.P
 */
public class LevelEvents {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final int TIME_WAITING_WAVE = 15;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Level mLevel;

	private final ArrayList<Wave> mWaves;
	private final ArrayList<Road> mRoads;

	private final TimerHandler mWaveTimerHandler;
	private final TimerHandler mMonsterTimerHandler;

	private Iterator<MonsterEvent> mMonsterEventIterator;
	private Iterator<Wave> mWaveIterator;

	private final HashMap<MonsterType, Integer> mLevelMonstersAmounts;

	private int mStartTime;
	private int mTimeUntilNextMonster;
	private int mCurrentWaveNumber;

	private boolean mWaveFinished;
	private boolean mMonsterTimerRegistered;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelEvents(final Level pLevel) {

		this.mLevel = pLevel;
		this.mRoads = new ArrayList<Road>();
		this.mWaves = new ArrayList<Wave>();
		this.mLevelMonstersAmounts = new HashMap<MonsterType, Integer>();

		this.mStartTime = 0;
		this.mCurrentWaveNumber = 0;
		this.mMonsterTimerRegistered = false;

		this.mWaveTimerHandler = new TimerHandler(0.1f, false, new ITimerCallback() {

			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {

				if (!LevelEvents.this.mLevel.getGUI().isLocked()) {
					LevelEvents.this.mStartTime += (100 * LevelEvents.this.mLevel.getSpeed());

					if ((LevelEvents.this.mStartTime / 1000) >= LevelEvents.TIME_WAITING_WAVE) {
						LevelEvents.this.mLevel.getGUI().setNextWaveVisible(false);
						LevelEvents.this.displayNextWave();
					} else {
						LevelEvents.this.mLevel.getGUI().setNextWaveText("Next wave in "
								+ (LevelEvents.TIME_WAITING_WAVE - (LevelEvents.this.mStartTime / 1000)));
						pTimerHandler.reset();
					}
				}
			}
		});

		this.mMonsterTimerHandler = new TimerHandler(0.1f, false, new ITimerCallback() {

			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {

				if (!LevelEvents.this.mLevel.getGUI().isLocked()) {
					LevelEvents.this.mStartTime += (100 * LevelEvents.this.mLevel.getSpeed());
					if (LevelEvents.this.mStartTime >= LevelEvents.this.mTimeUntilNextMonster) {
						if (LevelEvents.this.mMonsterEventIterator != null) {
							LevelEvents.this.popNextMonster();
						}
					} else {
						pTimerHandler.reset();
					}
				}
			}
		});
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ArrayList<Wave> getWaves() {
		return this.mWaves;
	}

	public ArrayList<Road> getRoads() {
		return this.mRoads;
	}

	public Level getLevel() {
		return this.mLevel;
	}

	public HashMap<MonsterType, Integer> getLevelMonstersAmounts() {
		return this.mLevelMonstersAmounts;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void popNextMonster() {

		if (this.mMonsterEventIterator.hasNext()) {

			final MonsterEvent monsterEvent = this.mMonsterEventIterator.next();
			final Road road = this.mRoads.get(monsterEvent.getRoadId());
			final Path path = new Path(road.getPaths().size());
			
			final float offsetX = 32 * MathUtils.RANDOM.nextFloat();
			final float offsetY = 32 * MathUtils.RANDOM.nextFloat();
			
			for (float[] pathPosition : road.getPaths()) {
				path.to(pathPosition[0] + offsetX, pathPosition[1] + offsetY);
			}
			
			final Monster monster = this.mLevel.createMonster(monsterEvent.getType(), 
					path.getCoordinatesX()[0], path.getCoordinatesY()[0]);
			
			final float duration = path.getLength() / monster.getSpeed();
			monster.getSprite().registerEntityModifier(
					new LoopEntityModifier(
							new PathModifier(duration, path, null, 
									new IPathModifierListener() {

								@Override
								public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {

								}

								@Override
								public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
									/*switch (pWaypointIndex) {
				                        case 0:
				                            pMonster.getSprite().animate(new long[]{200, 200, 200}, 6, 8, true);
				                            break;
				                        case 1:
				                            pMonster.getSprite().animate(new long[]{200, 200, 200}, 3, 5, true);
				                            break;
				                        case 2:
				                            pMonster.getSprite().animate(new long[]{200, 200, 200}, 0, 2, true);
				                            break;
				                        case 3:
				                            pMonster.getSprite().animate(new long[]{200, 200, 200}, 9, 11, true);
				                            break;
				                    }*/
									monster.getSprite().animate(250, true);
								}

								@Override
								public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
								}

								@Override
								public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {

									LevelEvents.this.mLevel.removeMonster(monster);
									LevelEvents.this.startNextWaveTimer();
									LevelEvents.this.mLevel.unregisterEntityModifier(pPathModifier);
								}
							})));

			if (this.mMonsterEventIterator.hasNext()) {
				this.mStartTime = 0;
				this.mTimeUntilNextMonster = monsterEvent.getMilliSecUntilNext();
				if (this.mMonsterTimerRegistered) {
					this.mMonsterTimerHandler.reset();
				} else {
					this.mLevel.registerUpdateHandler(this.mMonsterTimerHandler);
					this.mMonsterTimerRegistered = true;
				}
			} else {
				this.mWaveFinished = true;
			}

		} else {
			this.mWaveFinished = true;
		}
	}

	public void startNextWaveTimer() {

		if (this.mWaveFinished == true && this.mLevel.getMonsters().isEmpty()) {

			this.mStartTime = 0;
			this.mLevel.getGUI().setNextWaveVisible(true);
			this.mWaveTimerHandler.reset();
		}
	}

	private void displayNextWave() {

		this.mCurrentWaveNumber += 1;
		this.mLevel.getGUI().setWaveNumberText(String.valueOf(this.mCurrentWaveNumber));

		if (this.mWaveIterator.hasNext()) {

			this.mWaveFinished = false;
			this.mMonsterEventIterator = this.mWaveIterator.next().getMonsterEvents().iterator();
			popNextMonster();
		}
	}

	public void start() {

		final TimerHandler startLevel = new TimerHandler(0.1f, false, new ITimerCallback() {

			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {

				LevelEvents.this.mWaveIterator = LevelEvents.this.mWaves.iterator();
				LevelEvents.this.mLevel.registerUpdateHandler(
						LevelEvents.this.mWaveTimerHandler);
			}
		});

		this.mLevel.getGUI().setWaveNumberText(String.valueOf(this.mCurrentWaveNumber));
		this.mLevel.getGUI().setMoneyText(String.valueOf(this.mLevel.getMoney()));
		this.mLevel.getGUI().setNextWaveVisible(true);

		this.mLevel.registerUpdateHandler(startLevel);
	}
}
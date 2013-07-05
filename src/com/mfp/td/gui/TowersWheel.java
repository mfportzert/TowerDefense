/**
 * 
 */
package com.mfp.td.gui;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/**
 * @author M-F.P
 *
 */
public class TowersWheel extends Sprite {
	
	private Body mBody;
	private RevoluteJointDef mMotor;
	
	private long mStartMovingTime;
	
	private float mStartPosX;
	private float mStartPosY;
	
	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pTextureRegion
	 * @param pVertexBufferObjectManager
	 */
	public TowersWheel(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, 
			PhysicsWorld pPhysicsWorld) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}

	public void setBody(Body pBody) {
		this.mBody = pBody;
	}
	
	public void setMotor(RevoluteJointDef pMotor) {
		this.mMotor = pMotor;
	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pTouchEvent, 
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

		GameGUI gui = ((GameGUI) getParent());
        if (!gui.isLocked()) {
            
            switch (pTouchEvent.getAction()) {
                case TouchEvent.ACTION_DOWN:
                	                	
                	// STOP INSTANT HERE
                	this.mMotor.motorSpeed = 0.0f;
                	
                	this.mBody.setLinearDamping(0.0f);
                	this.mBody.setAngularDamping(0.0f);
                	
                	this.mBody.setLinearDamping(0.0f);
                	this.mBody.setLinearVelocity(0.0f, 0.0f);
                	this.mBody.applyTorque(0.0f);
                	
                	this.mStartMovingTime = System.currentTimeMillis();                	
                	this.mStartPosX = pTouchEvent.getX();
                	this.mStartPosY = pTouchEvent.getY();
                    break;
                case TouchEvent.ACTION_MOVE:
                	
                	this.mMotor.motorSpeed = 10.0f;
                	
                	float posX = pTouchEvent.getX();
                	float posY = pTouchEvent.getY();
                	Log.d("TOUCH AREA LOCAL", "X : "+posX+", Y : "+posY);
                	
                	float distX = (this.mStartPosX - posX) * 2000f;
                	float distY = (this.mStartPosY - posY) * 2000f;
                	
                	long currentTimeMillis = this.mStartMovingTime - System.currentTimeMillis();
                	long time = (currentTimeMillis == 0) ? 1 : currentTimeMillis;
                	
                	Log.d("CURRENT TIME MILLIS", "CurrentTimeMillis : "+currentTimeMillis);
                	Log.d("TIME", "time : "+time);
                	Log.d("DISTANCES", "X : "+distX+", Y : "+distY);
                	
                	
                	float forceX = distX / time;
                	float forceY = distY / time;
                	
                	Log.d("FORCES WHEEL", "X : "+forceX+"Y : "+forceY);
                	
                	this.mBody.applyForce(forceX, forceY, 0, 0);
                	
                	
                	
                    break;
                case TouchEvent.ACTION_UP:
                	
                	// STOP SLOWLY HERE
                	
                    break;
            }
            return true;
        }
        return false;
	}
	
	private void stopWheel() {
		
		while (!hasStoppedMovement()) {
			this.mBody.applyForce(-1000, -1000, 0, 0);
		}
	}
	
	public boolean hasStoppedMovement() {
		
        if(!this.mBody.getLinearVelocity().equals(new Vector2(0,0))) {
            return false;
        }
        return true;
	}
}

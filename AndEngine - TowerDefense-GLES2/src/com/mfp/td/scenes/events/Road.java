package com.mfp.td.scenes.events;


import java.util.ArrayList;


/**
 *
 * @author M-F.P
 */
public class Road {
    
    private ArrayList<float[]> mPaths;
    
    public Road() {
        
        this.mPaths = new ArrayList<float[]>();
    }
    
    public ArrayList<float[]> getPaths() {
        return this.mPaths;
    }
    
    public void add(final float pX, final float pY) {
    	
    	float[] path = {pX, pY};
        this.mPaths.add(path);
    }
}
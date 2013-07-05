/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfp.td.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

/**
 *
 * @author M-F.P
 */
public class PropertiesUtils {
    
    /**
     * 
     * @param pContext
     * @param filename
     * @return
     */
    public static Properties loadProperties(final Context pContext, final String pFilename) {
        
        InputStream is = null;
        Properties properties = new Properties();
        
        try {
            try {
                is = pContext.getAssets().open(pFilename);
                properties.load(is);
            } catch (IOException ex) {
                Log.e("FactoryUtils", "Cannot load file: "+pFilename, ex);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch(IOException ex){
            Log.e("FactoryUtils", "Couldn't close stream: "+pFilename, ex);
        }
        
        return properties;
    }
    
    /**
     * @param pProperties
     * @param pIndex
     * @return
     */
    public static int parseInt(final Properties pProperties, final String pIndex) {
    	
    	return Integer.parseInt(pProperties.getProperty(pIndex));
    }
    
    /**
     * @param pProperties
     * @param pIndex
     * @return
     */
    public static float parseFloat(final Properties pProperties, final String pIndex) {
    	
    	return Float.parseFloat(pProperties.getProperty(pIndex));
    }
}

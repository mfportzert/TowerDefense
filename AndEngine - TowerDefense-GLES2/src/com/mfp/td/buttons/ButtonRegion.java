package com.mfp.td.buttons;

/**
 * TextureRegions qui ne sont pas associées à des fichiers de properties
 * 
 * @author M-F.P
 *
 */
public enum ButtonRegion {
    
	FAST_FORWARD("fast_forward.png", 1, 4),
    MENU_QUIT("menu_quit.png", 1, 2),
    MENU_RESET("menu_reset.png", 1, 2),
    PLAY_PAUSE("play_pause.png", 1, 4),
    DELETE("close.png", 1, 2),
    MONEY_ICON("money_icon.png", 1, 1),
    
    FIRE_ARROW_ICON("fire_arrow_icon.png", 1, 2),
    ICE_ARROW_ICON("ice_arrow_icon.jpg", 1, 2),
    MULTI_ARROW_ICON("multi_arrow_icon.jpg", 1, 2),
    
    WHEEL("wheel.png", 1, 1),
    
    CREATE_ARROW_TOWER("create_arrow_tower.png", 1, 2);
	
    private final int mRows;
    private final int mColumns;
    private final String mSrcName;
    
    private ButtonRegion(final String pSrcName, final int pRows, final int pColumns) {
        
    	this.mRows = pRows;
		this.mColumns = pColumns;
		this.mSrcName = pSrcName;
    } 
    
    public String getSrcName() {
        return this.mSrcName;
    }
    
    public int getRows() {
        return this.mRows;
    }
    
    public int getColumns() {
        return this.mColumns;
    }
}

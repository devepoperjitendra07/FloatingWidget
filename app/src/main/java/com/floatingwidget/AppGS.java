package com.floatingwidget;

/**
 * Created by Mayk on 9/26/2017.
 */

public class AppGS {
    public int getAppImage() {
        return appImage;
    }

    public void setAppImage(int appImage) {
        this.appImage = appImage;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public AppGS(int appImage, String appName, boolean isSelected) {

        this.appImage = appImage;
        this.appName = appName;
        this.isSelected = isSelected;
    }

    private int appImage;
    private String appName;
   private boolean isSelected;


}

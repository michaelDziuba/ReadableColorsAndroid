package com.michael.readablecolors;

/**
 * Created by cdu on 2017-05-05.
 */

public class Settings {

    private long id;
    private int textColorCode;
    private int textR;
    private int textG;
    private int textB;
    private int infoColorCode;
    private int contrastCode;
    private int sortCode;
    private int seekbarPosition;

    public Settings(){}

    public Settings(long id, int textColorCode, int textR, int textG, int textB, int infoColorCode, int contrastCode, int sortCode, int seekbarPosition){
        this.id = id;
        this.textColorCode = textColorCode;
        this.textR = textR;
        this.textG = textG;
        this.textB = textB;
        this.infoColorCode = infoColorCode;
        this.contrastCode = contrastCode;
        this.sortCode = sortCode;
        this.seekbarPosition = seekbarPosition;
    }

    public Settings(int textColorCode, int textR, int textG, int textB, int infoColorCode, int contrastCode, int sortCode, int seekbarPosition){
        this.id = 0;
        this.textColorCode = textColorCode;
        this.textR = textR;
        this.textG = textG;
        this.textB = textB;
        this.infoColorCode = infoColorCode;
        this.contrastCode = contrastCode;
        this.sortCode = sortCode;
        this.seekbarPosition = seekbarPosition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTextColorCode() {
        return textColorCode;
    }

    public void setTextColorCode(int textColorCode) {
        this.textColorCode = textColorCode;
    }

    public int getTextR() {
        return textR;
    }

    public void setTextR(int textR) {
        this.textR = textR;
    }

    public int getTextG() {
        return textG;
    }

    public void setTextG(int textG) {
        this.textG = textG;
    }

    public int getTextB() {
        return textB;
    }

    public void setTextB(int textB) {
        this.textB = textB;
    }

    public int getInfoColorCode() {
        return infoColorCode;
    }

    public void setInfoColorCode(int infoColorCode) {
        this.infoColorCode = infoColorCode;
    }

    public int getContrastCode() {
        return contrastCode;
    }

    public void setContrastCode(int contrastCode) {
        this.contrastCode = contrastCode;
    }

    public int getSortCode() {
        return sortCode;
    }

    public void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }

    public int getSeekbarPosition() {
        return seekbarPosition;
    }

    public void setSeekbarPosition(int seekbarPosition) {
        this.seekbarPosition = seekbarPosition;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public class MuPDFAlertInternal
{
    public final String message;
    public final int iconType;
    public final int buttonGroupType;
    public final String title;
    public int buttonPressed;
    
    MuPDFAlertInternal(final String aMessage, final int aIconType, final int aButtonGroupType, final String aTitle, final int aButtonPressed) {
        this.message = aMessage;
        this.iconType = aIconType;
        this.buttonGroupType = aButtonGroupType;
        this.title = aTitle;
        this.buttonPressed = aButtonPressed;
    }
    
    MuPDFAlertInternal(final MuPDFAlert alert) {
        this.message = alert.message;
        this.iconType = alert.iconType.ordinal();
        this.buttonGroupType = alert.buttonGroupType.ordinal();
        this.title = alert.message;
        this.buttonPressed = alert.buttonPressed.ordinal();
    }
    
    MuPDFAlert toAlert() {
        return new MuPDFAlert(this.message, MuPDFAlert.IconType.values()[this.iconType], MuPDFAlert.ButtonGroupType.values()[this.buttonGroupType], this.title, MuPDFAlert.ButtonPressed.values()[this.buttonPressed]);
    }
}

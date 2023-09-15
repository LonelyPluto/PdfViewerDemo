// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public class MuPDFAlert
{
    public final String message;
    public final IconType iconType;
    public final ButtonGroupType buttonGroupType;
    public final String title;
    public ButtonPressed buttonPressed;
    
    MuPDFAlert(final String aMessage, final IconType aIconType, final ButtonGroupType aButtonGroupType, final String aTitle, final ButtonPressed aButtonPressed) {
        this.message = aMessage;
        this.iconType = aIconType;
        this.buttonGroupType = aButtonGroupType;
        this.title = aTitle;
        this.buttonPressed = aButtonPressed;
    }
    
    public enum IconType
    {
        Error, 
        Warning, 
        Question, 
        Status;
    }
    
    public enum ButtonPressed
    {
        None, 
        Ok, 
        Cancel, 
        No, 
        Yes;
    }
    
    public enum ButtonGroupType
    {
        Ok, 
        OkCancel, 
        YesNo, 
        YesNoCancel;
    }
}

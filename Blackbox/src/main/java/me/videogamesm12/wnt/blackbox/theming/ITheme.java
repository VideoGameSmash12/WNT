package me.videogamesm12.wnt.blackbox.theming;

public interface ITheme
{
    String getInternalName();

    String getThemeName();

    String getThemeDescription();

    IThemeType getType();

    String getThemeClass();

    boolean isSupposedToShow();

    void apply();

    void showOptionalMessage();
}

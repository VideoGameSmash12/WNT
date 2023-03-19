package me.videogamesm12.wnt.blackbox.theming;

import lombok.Getter;

public enum ThemeType
{
    BUILT_IN("Built into Java"),
    FLATLAF("Built into FlatLAF"),
    NETBEANS("From NetBeans");

    @Getter
    private String label;

    ThemeType(String label)
    {
        this.label = label;
    }
}

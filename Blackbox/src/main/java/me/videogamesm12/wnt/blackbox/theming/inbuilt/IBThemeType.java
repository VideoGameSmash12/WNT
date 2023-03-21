package me.videogamesm12.wnt.blackbox.theming.inbuilt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.videogamesm12.wnt.blackbox.theming.IThemeType;

@AllArgsConstructor
@Getter
public enum IBThemeType implements IThemeType
{
    BUILT_IN("Built into Java", 93812),
    NETBEANS("From NetBeans", 32118);

    private final String label;

    private final int id;
}
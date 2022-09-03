package me.videogamesm12.wnt.module;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ModuleNotEnabledException extends Exception
{
    @Getter
    private Module module;
}

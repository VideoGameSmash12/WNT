package me.videogamesm12.wnt.config;

import lombok.Getter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "wnt")
public class WConfig implements ConfigData
{
    @Getter
    @ConfigEntry.Gui.Excluded
    private List<String> enabledModules = new ArrayList<>();
}

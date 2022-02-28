package me.videogamesm12.w95.supervisorgui.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.videogamesm12.w95.supervisorgui.SupervisorGUI;

public class ModMenuIntegration implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(SupervisorGUI.GUIConfig.class, parent).get();
    }
}

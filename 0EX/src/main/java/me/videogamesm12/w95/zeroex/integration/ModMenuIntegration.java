package me.videogamesm12.w95.zeroex.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.videogamesm12.w95.zeroex.ZeroEx;

/**
 * <h1>ModMenuIntegration</h1>
 * 0EX's integration with the ModMenu mod.
 */
public class ModMenuIntegration implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(ZeroEx.ZeroExConfig.class, parent).get();
    }
}
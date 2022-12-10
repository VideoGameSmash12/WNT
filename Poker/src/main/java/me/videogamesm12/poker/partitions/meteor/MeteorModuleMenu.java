package me.videogamesm12.poker.partitions.meteor;

import me.videogamesm12.poker.core.gui.PModModuleMenu;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.Identifier;

public class MeteorModuleMenu extends PModModuleMenu<Module>
{
    public MeteorModuleMenu(Module module)
    {
        super(new Identifier("poker", "meteor"), module);
    }

    @Override
    public String getName()
    {
        return getModule().name;
    }

    @Override
    public String getDescription()
    {
        return getModule().description;
    }

    @Override
    public void setModuleEnabled(boolean value)
    {
        if (isModuleEnabled() != value)
        {
            getModule().toggle();
        }
    }

    @Override
    public boolean isModuleEnabled()
    {
        return getModule().isActive();
    }
}

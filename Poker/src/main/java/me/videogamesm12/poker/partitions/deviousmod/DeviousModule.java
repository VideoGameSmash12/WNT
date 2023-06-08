package me.videogamesm12.poker.partitions.deviousmod;

import com.github.allinkdev.deviousmod.api.Module;
import me.videogamesm12.poker.core.gui.PModModuleMenu;
import net.minecraft.util.Identifier;

public class DeviousModule extends PModModuleMenu<Module>
{
    public DeviousModule(Module module)
    {
        super(new Identifier("poker", "deviousmod"), module);
    }

    @Override
    public String getName()
    {
        return getModule().getModuleName();
    }

    @Override
    public String getDescription()
    {
        return getModule().getDescription();
    }

    @Override
    public void setModuleEnabled(boolean value)
    {
        if (getModule().getModuleState() != value)
        {
            getModule().toggle();
        }
    }

    @Override
    public boolean isModuleEnabled()
    {
        return getModule().getModuleState();
    }
}

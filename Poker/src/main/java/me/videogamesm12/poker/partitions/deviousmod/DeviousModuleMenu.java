package me.videogamesm12.poker.partitions.deviousmod;

import me.allink.deviousmod.module.ModuleBase;
import me.videogamesm12.poker.core.gui.PModModuleMenu;
import net.minecraft.util.Identifier;

public class DeviousModuleMenu extends PModModuleMenu<ModuleBase>
{
    public DeviousModuleMenu(ModuleBase module)
    {
        super(new Identifier("poker", "deviousmod"), module);
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
        getModule().setToggled(value);
    }

    @Override
    public boolean isModuleEnabled()
    {
        return getModule().toggled;
    }
}

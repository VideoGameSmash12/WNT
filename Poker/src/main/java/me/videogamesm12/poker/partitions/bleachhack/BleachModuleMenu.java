package me.videogamesm12.poker.partitions.bleachhack;

import me.videogamesm12.poker.core.gui.PModModuleMenu;
import net.minecraft.util.Identifier;
import org.bleachhack.module.Module;

public class BleachModuleMenu extends PModModuleMenu<Module>
{
    public BleachModuleMenu(Module module)
    {
        super(new Identifier("poker", "bleachhack"), module);
    }

    @Override
    public String getName()
    {
        return getModule().getName();
    }

    @Override
    public String getDescription()
    {
        return getModule().getDesc();
    }

    @Override
    public void setModuleEnabled(boolean value)
    {
        getModule().setEnabled(value);
    }

    @Override
    public boolean isModuleEnabled()
    {
        return getModule().isEnabled();
    }
}

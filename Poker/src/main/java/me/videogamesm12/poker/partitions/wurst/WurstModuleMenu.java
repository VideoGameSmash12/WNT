package me.videogamesm12.poker.partitions.wurst;

import me.videogamesm12.poker.Poker;
import me.videogamesm12.poker.core.gui.PModModuleMenu;
import net.minecraft.util.Identifier;
import net.wurstclient.hack.Hack;

public class WurstModuleMenu extends PModModuleMenu<Hack>
{
    public WurstModuleMenu(Hack module)
    {
        super(new Identifier("poker", "wurst"), module);
    }

    @Override
    public String getName()
    {
        if (getModule() != null)
        {
            return getModule().getName();
        }
        else
        {
            Poker.getLogger().warn("WTF HOW IS IT NULL?");
            return "VIDEO FIX ME NOW NOW NOW NOW";
        }
    }

    @Override
    public String getDescription()
    {
        // An issue is present where some descriptions don't work correctly.
        return "";
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

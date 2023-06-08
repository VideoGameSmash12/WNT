package me.videogamesm12.poker.partitions.advancedchathud;

import me.videogamesm12.wnt.blackbox.window.tool.console.AbstractTab;
import net.minecraft.text.Text;

public class AdvancedChatTab extends AbstractTab
{
    public AdvancedChatTab()

    @Override
    public boolean shouldDisplay(Text message)
    {
        return false;
    }

    @Override
    public String name()
    {
        return null;
    }
}

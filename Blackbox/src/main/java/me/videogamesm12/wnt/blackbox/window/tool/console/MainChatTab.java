package me.videogamesm12.wnt.blackbox.window.tool.console;

import net.minecraft.text.Text;

public class MainChatTab extends AbstractTab
{
    @Override
    public boolean shouldDisplay(Text message)
    {
        return true;
    }

    @Override
    public String name()
    {
        return "Chat";
    }
}

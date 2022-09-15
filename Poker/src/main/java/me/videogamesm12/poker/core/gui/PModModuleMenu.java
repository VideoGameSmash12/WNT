package me.videogamesm12.poker.core.gui;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.videogamesm12.poker.Poker;
import me.videogamesm12.poker.core.event.ModuleToggleEvent;
import net.minecraft.util.Identifier;

import javax.swing.*;

public abstract class PModModuleMenu<M> extends JMenu implements PModSubMenu
{
    @Getter
    private final M module;
    //--
    private final JCheckBoxMenuItem enabledItem = new JCheckBoxMenuItem("Enabled");

    public PModModuleMenu(Identifier responsibleMod, M module)
    {
        this.module = module;
        //--
        setText(getName());
        setToolTipText(getDescription());
        //--
        enabledItem.setSelected(isModuleEnabled());
        enabledItem.addActionListener((event) -> setModuleEnabled(enabledItem.getState()));
        //--
        Poker.getHouse(responsibleMod).register(this);
        //--
        add(enabledItem);
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract void setModuleEnabled(boolean value);

    public abstract boolean isModuleEnabled();

    // TODO: This seems really inefficient for event handling. It works, sure, but is it really a great idea to have
    //  module menus that each individually listen for module toggles when there's a fuckton of modules to begin with?
    //  At least this is registered in a way where each mod has their own unique event bus...
    @Subscribe
    public final void onModuleToggled(ModuleToggleEvent<M> event)
    {
        if (event.getModule().equals(module))
            enabledItem.setSelected(event.isEnabledNow());
    }
}

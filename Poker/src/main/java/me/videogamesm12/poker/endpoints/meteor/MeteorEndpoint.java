package me.videogamesm12.poker.endpoints.meteor;

import me.videogamesm12.poker.core.gui.PModCategoryMenu;
import me.videogamesm12.poker.core.gui.PModMenu;
import me.videogamesm12.poker.partitions.meteor.MeteorModuleMenu;
import me.videogamesm12.wnt.blackbox.menus.WNTMenu;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class MeteorEndpoint extends MeteorAddon
{
    @Override
    public void onInitialize()
    {
        // Create the menu
        PModMenu<MeteorClient> menu = new PModMenu<>("Meteor", MeteorClient.INSTANCE);

        // Adds the modules from all the categories as their own separate menus
        Modules.loopCategories().forEach(category -> {
            PModCategoryMenu categoryMenu = new PModCategoryMenu(category.name);
            Modules.get().getGroup(category).forEach(module -> categoryMenu.addModule(new MeteorModuleMenu(module)));
            menu.addSubMenu(categoryMenu);
        });

        // Adds the final product
        WNTMenu.getQueue().add(menu);
    }

    @Override
    public String getPackage()
    {
        // We don't offer anything to Meteor, we just hook into it so that we can tamper with it
        return null;
    }
}

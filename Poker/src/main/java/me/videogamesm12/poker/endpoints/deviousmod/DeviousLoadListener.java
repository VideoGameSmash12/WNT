package me.videogamesm12.poker.endpoints.deviousmod;

import com.github.allinkdev.deviousmod.api.DeviousModSilhouette;
import com.github.allinkdev.deviousmod.api.load.LoadListener;
import me.videogamesm12.poker.core.gui.PModMenu;

public class DeviousLoadListener implements LoadListener
{
    @Override
    public void onLoad(DeviousModSilhouette deviousMod)
    {
        // Creates the menu
        PModMenu<DeviousModSilhouette> menu = new PModMenu<>("Meteor", deviousMod);

        deviousMod.getModuleManager().getModules()
    }
}

package me.videogamesm12.poker.mixins.deviousmod;

import me.allink.deviousmod.client.DeviousModClient;
import me.allink.deviousmod.managers.ModuleManager;
import me.videogamesm12.poker.core.gui.PModMenu;
import me.videogamesm12.poker.partitions.deviousmod.DeviousModuleMenu;
import me.videogamesm12.wnt.blackbox.menus.WNTMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeviousModClient.class)
public class DeviousModClientMixin
{
    @Inject(method = "onInitializeClient", at = @At("TAIL"), remap = false)
    public void hookClientInit(CallbackInfo ci)
    {
        // First things first, let's build a menu
        PModMenu<DeviousModClient> menu = new PModMenu<>("DeviousMod", DeviousModClient.getInstance());

        // Adds the modules from ModuleManager as their own separate menus
        ModuleManager.getModules().forEach(module -> menu.addSubMenu(new DeviousModuleMenu(module)));

        // Adds the final product
        WNTMenu.getQueue().add(menu);
    }
}

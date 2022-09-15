package me.videogamesm12.poker.mixins.bleachhack;

import me.videogamesm12.poker.core.gui.PModCategoryMenu;
import me.videogamesm12.poker.core.gui.PModMenu;
import me.videogamesm12.poker.partitions.bleachhack.BleachModuleMenu;
import me.videogamesm12.wnt.blackbox.menus.WNTMenu;
import org.apache.commons.lang3.StringUtils;
import org.bleachhack.BleachHack;
import org.bleachhack.module.ModuleCategory;
import org.bleachhack.module.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(BleachHack.class)
public class BleachHackMixin
{
    @Inject(method = "postInit", at = @At("TAIL"), remap = false)
    public void onPostInit(CallbackInfo ci)
    {
        PModMenu<BleachHack> menu = new PModMenu<>("BleachHack", BleachHack.getInstance());

        Arrays.stream(ModuleCategory.values()).forEach(category -> {
            PModCategoryMenu cMenu = new PModCategoryMenu(StringUtils.capitalize(category.name().toLowerCase()));
            ModuleManager.getModulesInCat(category).forEach(module -> cMenu.addModule(new BleachModuleMenu(module)));
            menu.addSubMenu(cMenu);
        });

        WNTMenu.getQueue().add(menu);
    }
}

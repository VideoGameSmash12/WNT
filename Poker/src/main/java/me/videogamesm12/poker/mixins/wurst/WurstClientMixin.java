/*
 * Copyright (c) 2022 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.videogamesm12.poker.mixins.wurst;

import me.videogamesm12.poker.core.gui.PModCategoryMenu;
import me.videogamesm12.poker.core.gui.PModMenu;
import me.videogamesm12.poker.partitions.wurst.WurstModuleMenu;
import me.videogamesm12.wnt.blackbox.window.menu.WNTMenu;
import net.wurstclient.Category;
import net.wurstclient.WurstClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(WurstClient.class)
public class WurstClientMixin
{
    @Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/wurstclient/altmanager/AltManager;<init>(Ljava/nio/file/Path;Ljava/nio/file/Path;)V", shift = At.Shift.AFTER), remap = false)
    public void injectInitialize(CallbackInfo ci)
    {
        // First things first, let's build a menu
        PModMenu<WurstClient> menu = new PModMenu<>("Wurst", WurstClient.INSTANCE);

        // Adds the modules from all the categories as their own separate menus
        Arrays.stream(Category.values()).forEach(category -> {
            PModCategoryMenu categoryMenu = new PModCategoryMenu(category.getName());
            WurstClient.INSTANCE.getHax().getAllHax().stream().filter(hack -> hack.getCategory() == category).forEach(hack -> categoryMenu.addModule(new WurstModuleMenu(hack)));
            menu.addSubMenu(categoryMenu);
        });

        // Adds the final product
        WNTMenu.queueModMenu(menu);
    }
}
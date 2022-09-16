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

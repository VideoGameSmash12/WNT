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

package me.videogamesm12.wnt.toolbox.commands;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.command.WCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class LTellrawCommand extends WCommand
{
	private static final GsonComponentSerializer GSON = GsonComponentSerializer.gson();

	public LTellrawCommand()
	{
		super("ltellraw", "Locally tellraw yourself JSON components.", "/ltellraw <json>");
	}

	@Override
	public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
	{
		if (args.length == 0)
		{
			return false;
		}

		Component component;
		String joined = StringUtils.join(args, " ");

		try
		{
			component = GSON.deserialize(joined);
		}
		catch (JsonParseException ex)
		{
			component = Component.translatable("wnt.toolbox.commands.ltellraw.invalid_json",
					Component.text(joined)).color(NamedTextColor.RED);
		}

		msg(component);
		return true;
	}

	@Override
	public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
	{
		return null;
	}
}

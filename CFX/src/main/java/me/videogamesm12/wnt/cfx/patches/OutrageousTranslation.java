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

package me.videogamesm12.wnt.cfx.patches;

import com.google.gson.*;
import me.videogamesm12.wnt.cfx.CFX;
import me.videogamesm12.wnt.cfx.base.CPatch;
import me.videogamesm12.wnt.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>OutrageousTranslation</h1>
 * Patches a critical exploit in the translatable component system that is caused by a design flaw in translatable
 * components that will not be described here.
 */
@CPatch(name = "wnt.cfx.outrageous_translation.name", description = "wnt.cfx.outrageous_translation.desc")
@Mixin(Text.Serializer.class)
public class OutrageousTranslation
{
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%[0-9]{1,}\\$s");
	private static final Gson GSON = new Gson();

	@Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonElement;getAsJsonObject()Lcom/google/gson/JsonObject;", shift = At.Shift.AFTER), cancellable = true)
	public void fixMajorExploit(JsonElement json, Type type, JsonDeserializationContext context, CallbackInfoReturnable<Object> cir)
	{
		if (CFX.getConfig().getCompPatches().getTranslatePatches().isBoundaryPatchEnabled()
				&& getNumberOfPlaceholders(json) > 32)
		{
			Component component = Component.translatable("wnt.replacements.cfx.text.too_many_placeholders",
					NamedTextColor.RED).hoverEvent(HoverEvent.showText(Component.text(GSON.toJson(json))));

			cir.setReturnValue(Messenger.convert(component));
		}
	}

	private long getNumberOfPlaceholders(JsonElement element)
	{
		long amount = 0;

		// God dammit!
		if (!element.isJsonObject())
		{
			return amount;
		}

		JsonObject from = element.getAsJsonObject();

		// Figure out how many placeholders are in a single translatable component
		if (from.has("translate"))
		{
			String key = JsonHelper.getString(from, "translate");
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(key);
			amount += matcher.results().count();
		}

		// This apparently exists as of 1.19.4, so we'll take that into account as well
		if (from.has("fallback"))
		{
			String key = JsonHelper.getString(from, "fallback");
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(key);
			amount += matcher.results().count();
		}

		// Also applies to keybind components, but to a lesser extent
		if (from.has("keybind"))
		{
			String key = JsonHelper.getString(from, "keybind");
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(key);
			amount += matcher.results().count();
		}

		// Recursively figure out how many placeholders the component has in the "with" shit
		if (from.has("with"))
		{
			JsonArray array = JsonHelper.getArray(from, "with");

			for (JsonElement within : array)
			{
				long amountWithin = getNumberOfPlaceholders(within);

				if (amountWithin == 1)
				{
					amount++;
				}
				else if (amountWithin > 1)
				{
					amount = amount * amountWithin;
				}
			}
		}

		return amount;
	}
}

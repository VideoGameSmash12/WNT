package me.videogamesm12.w95.zeroex.patches;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.videogamesm12.w95.zeroex.ZeroEx;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HoverIdentifier
{
    @Mixin(HoverEvent.EntityContent.class)
    public static class Entities
    {
        /**
         * Fixes an exploit caused by invalid identifiers in the "show_entity" hover event.
         * @param json  JsonElement
         * @param cir   CallbackInfoReturnable
         */
        @Inject(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$EntityContent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", shift = At.Shift.BEFORE), cancellable = true)
        private static void injectJsonParse(JsonElement json, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
        {
            if (ZeroEx.CONFIG.hoverIdentifierPatch())
            {
                JsonObject object = json.getAsJsonObject();

                if (JsonHelper.hasString(object, "type") && !Identifier.isValid(JsonHelper.getString(json.getAsJsonObject(), "type")))
                {
                    cir.setReturnValue(null);
                }
            }
        }

        /**
         * Fixes an exploit caused by invalid identifiers in the "show_entity" hover event.
         * @param text  Text
         * @param cir   CallbackInfoReturnable
         */
        @Inject(method = "parse(Lnet/minecraft/text/Text;)Lnet/minecraft/text/HoverEvent$EntityContent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", shift = At.Shift.BEFORE), cancellable = true)
        private static void injectTextParse(Text text, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
        {
            if (ZeroEx.CONFIG.hoverIdentifierPatch())
            {
                try
                {
                    NbtCompound nbt = StringNbtReader.parse(text.getString());
                    if (!Identifier.isValid(nbt.getString("type")))
                    {
                        cir.setReturnValue(null);
                    }
                }
                catch (CommandSyntaxException ignored)
                {
                    cir.setReturnValue(null);
                }
            }
        }
    }

    @Mixin(HoverEvent.ItemStackContent.class)
    public static class Items
    {
        /**
         * Fixes an exploit caused by invalid identifiers in the "show_item" hover event.
         * @param id    String
         * @return      String
         */
        @ModifyArg(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$ItemStackContent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
        private static String validate(String id)
        {
            if (!ZeroEx.CONFIG.hoverIdentifierPatch())
                return id;

            return Identifier.isValid(id) ? id : "minecraft:air";
        }
    }
}

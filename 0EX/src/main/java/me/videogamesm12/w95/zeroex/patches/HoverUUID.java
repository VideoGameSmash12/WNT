package me.videogamesm12.w95.zeroex.patches;

import com.google.gson.JsonElement;
import me.videogamesm12.w95.zeroex.ZeroEx;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

/**
 * <b>HoverUUID</b>
 * <p>Patches an exploit that causes clients to crash trying to process malicious text.</p>
 */
@Mixin(HoverEvent.EntityContent.class)
public class HoverUUID
{
    @Inject(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$EntityContent;",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;",
            shift = At.Shift.AFTER),
        cancellable = true)
    private static void injectParseJson(JsonElement json, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
    {
        if (ZeroEx.CONFIG.getHoverUUIDPatchMethod() == ZeroEx.HoverUUIDPatchMethod.SILENT)
        {
            try
            {
                UUID.fromString(JsonHelper.getString(json.getAsJsonObject(), "id"));
            }
            catch (Exception ex)
            {
                cir.setReturnValue(null);
            }
        }
    }

    @Inject(method = "parse(Lnet/minecraft/text/Text;)Lnet/minecraft/text/HoverEvent$EntityContent;",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;",
            shift = At.Shift.AFTER),
        cancellable = true)
    private static void injectParseText(Text text, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
    {
        if (ZeroEx.CONFIG.getHoverUUIDPatchMethod() == ZeroEx.HoverUUIDPatchMethod.SILENT)
        {
            try
            {
                UUID.fromString(StringNbtReader.parse(text.getString()).getString("id"));
            }
            catch (Exception ex)
            {
                cir.setReturnValue(null);
            }
        }
    }

    @ModifyArg(method = "parse(Lnet/minecraft/text/Text;)Lnet/minecraft/text/HoverEvent$EntityContent;",
            at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;"))
    private static String modifyUuidText(String uuid)
    {
        if (ZeroEx.CONFIG.getHoverUUIDPatchMethod() == ZeroEx.HoverUUIDPatchMethod.VISIBLE)
        {
            try
            {
                return UUID.fromString(uuid).toString();
            }
            catch (Exception ex)
            {
                return "DEADDEAD-DEAD-DEAD-DEAD-DEADDEADDEAD";
            }
        }

        return uuid;
    }

    @ModifyArg(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$EntityContent;",
            at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;"))
    private static String modifyUuidJson(String uuid)
    {
        if (ZeroEx.CONFIG.getHoverUUIDPatchMethod() == ZeroEx.HoverUUIDPatchMethod.VISIBLE)
        {
            try
            {
                return UUID.fromString(uuid).toString();
            }
            catch (Exception ex)
            {
                return "DEADDEAD-DEAD-DEAD-DEAD-DEADDEADDEAD";
            }
        }

        return uuid;
    }
}

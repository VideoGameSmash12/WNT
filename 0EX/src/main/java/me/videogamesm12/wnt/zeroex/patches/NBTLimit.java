package me.videogamesm12.wnt.zeroex.patches;

import me.videogamesm12.wnt.zeroex.ZeroEx;
import net.minecraft.nbt.NbtTagSizeTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NbtTagSizeTracker.class)
public class NBTLimit
{
    /**
     * Completely disables the enforcement of any NBT size limits for clients.
     * @implNote    This will <i>not</i> fix chunkbans caused by an issue on the server-side.
     * @param bits  long
     * @param ci    CallbackInfo
     */
    @Inject(method = "add", at = @At(value = "INVOKE", target = "Ljava/lang/RuntimeException;<init>(Ljava/lang/String;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void noCapNoProblem(long bits, CallbackInfo ci)
    {
        if (ZeroEx.CONFIG.getNetwork().isDisableNbtCap())
        {
            ci.cancel();
        }
    }
}

package io.github.superpro148.quickarmorswap.mixin;

import io.github.superpro148.quickarmorswap.config.QuickArmorSwapConfig;
import io.github.superpro148.quickarmorswap.QuickArmorSwap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraItem.class)
public class ElytraSwapMixin {
    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/TypedActionResult;fail(Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;"
            ),
            cancellable = true
    )
    public void quickarmorswap$swapElytra(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (QuickArmorSwapConfig.ENABLE.getValue()) {
            switch (QuickArmorSwapConfig.MODE.getValue()) {
                case SWAP -> QuickArmorSwap.swapArmor(world, user, hand, cir);
                case DROP -> QuickArmorSwap.dropArmor(world, user, hand, cir);
            }
        }
    }
}

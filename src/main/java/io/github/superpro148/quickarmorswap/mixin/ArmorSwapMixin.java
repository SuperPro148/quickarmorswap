package io.github.superpro148.quickarmorswap.mixin;

import io.github.superpro148.quickarmorswap.DropConfig;
import io.github.superpro148.quickarmorswap.QuickArmorSwap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.class)
public abstract class ArmorSwapMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "net/minecraft/util/TypedActionResult.fail (Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;"), cancellable = true)
    public void quickarmorswap$swapArmor(World world, @NotNull PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (DropConfig.DROP_INSTEAD_OF_SWAP) {
            QuickArmorSwap.dropArmor(world, user, hand, cir);
        } else {
            QuickArmorSwap.swapArmor(world, user, hand, cir);
        }
    }
}

package io.github.superpro148.quickarmorswap.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.class)
public abstract class ArmorSwapMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "net/minecraft/util/TypedActionResult.fail (Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;"), cancellable = true)
    public void quickarmorswap$swapArmor(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack handStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(handStack);
        ItemStack equippedStack = user.getEquippedStack(equipmentSlot);
        PlayerInventory inv = user.getInventory();
        if (!equippedStack.isEmpty()) {
            if (world.isClient()) {
                ClickSlotC2SPacket packet = new ClickSlotC2SPacket(
                        user.currentScreenHandler.syncId,
                        user.currentScreenHandler.getRevision(),
                        8 - equipmentSlot.getEntitySlotId(),
                        inv.getSlotWithStack(handStack),
                        SlotActionType.SWAP,
                        handStack,
                        new Int2ObjectOpenHashMap<ItemStack>()
                );
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
            }
            inv.setStack(inv.getSlotWithStack(handStack), equippedStack);
            inv.setStack(inv.getSlotWithStack(equippedStack), handStack);
            cir.setReturnValue(TypedActionResult.success(handStack));
        }
    }
}

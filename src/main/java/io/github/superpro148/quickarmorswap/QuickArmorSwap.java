package io.github.superpro148.quickarmorswap;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class QuickArmorSwap implements ClientModInitializer {
    public static void dropArmor(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack handStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(handStack);
        ItemStack equippedStack = user.getEquippedStack(equipmentSlot);
        if (!equippedStack.isEmpty() && hand == Hand.MAIN_HAND) {
            if (world.isClient()) {
                ClickSlotC2SPacket packet = new ClickSlotC2SPacket(
                        user.currentScreenHandler.syncId,
                        user.currentScreenHandler.getRevision(),
                        8 - equipmentSlot.getEntitySlotId(),
                        8 - equipmentSlot.getEntitySlotId(),
                        SlotActionType.THROW,
                        equippedStack,
                        new Int2ObjectOpenHashMap<>()
                );
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
            }
            user.dropItem(equippedStack, false);
            user.equipStack(equipmentSlot, handStack);
            cir.setReturnValue(TypedActionResult.success(handStack));
        }
    }
    public static void swapArmor(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack handStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(handStack);
        ItemStack equippedStack = user.getEquippedStack(equipmentSlot);
        PlayerInventory inv = user.getInventory();
        if (!equippedStack.isEmpty() && hand == Hand.MAIN_HAND) {
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

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("quickarmorswap").executes(context -> {
                DropConfig.toggle();
                String configValue = DropConfig.DROP_INSTEAD_OF_SWAP ? "DROP" : "SWAP";
                context.getSource().sendFeedback(Text.of("armor will now: " + configValue));
                return 1;
            }));
        });
    }
}

package io.github.superpro148.quickarmorswap;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.BooleanController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class DropConfig {
    public static boolean DROP_INSTEAD_OF_SWAP = false;

    public static void save() {}

    public static Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Quick Armor Swap"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Quick Armor Swap"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Drop instead of swap"))
                                .tooltip(Text.of("Drops the equipped armor piece instead of swapping it with the one in your hand."))
                                .binding(
                                        false,
                                        () -> DROP_INSTEAD_OF_SWAP,
                                        newValue -> DROP_INSTEAD_OF_SWAP = newValue
                                )
                                .controller(BooleanController::new)
                                .build()
                        )
                        .build())
                .save(DropConfig::save)
                .build()
                .generateScreen(parent);
    }

    public static void toggle() {
        DROP_INSTEAD_OF_SWAP = !DROP_INSTEAD_OF_SWAP;
    }
}

package io.github.superpro148.quickarmorswap.config;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.EnumController;
import io.github.superpro148.configlib148.ConfigList;
import io.github.superpro148.configlib148.ConfigOption;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class QuickArmorSwapConfig {
    public static ConfigList CONFIG_LIST = new ConfigList("quickarmorswap");
    public static boolean DROP_INSTEAD_OF_SWAP;
    public static ConfigOption<Boolean> ENABLE = CONFIG_LIST.register(new ConfigOption<>("enable", Boolean.class, true));
    public static ConfigOption<Mode> MODE = CONFIG_LIST.register(new ConfigOption<>("mode", Mode.class, Mode.SWAP));

    public static void save() {
        CONFIG_LIST.saveConfig();
    }

    public static Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Quick Armor Swap"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Quick Armor Swap"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.translatable("option.quickarmorswap.enable"))
                                .tooltip(Text.translatable("tooltip.quickarmorswap.enable"))
                                .binding(
                                        true,
                                        () -> ENABLE.getValue(),
                                        newValue -> ENABLE.setValue(newValue)
                                )
                                .controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(Mode.class)
                                .name(Text.translatable("option.quickarmorswap.mode"))
                                .tooltip(Text.translatable("tooltip.quickarmorswap.mode"))
                                .binding(
                                        Mode.SWAP,
                                        () -> MODE.getValue(),
                                        newValue -> MODE.setValue(newValue)
                                )
                                .controller(option -> new EnumController<Mode>(
                                        option,
                                        enumConst -> Text.translatable("enum.quickarmorswap.mode_" + enumConst.toString().toLowerCase())
                                ))
                                .build())
                        .build())
                .save(QuickArmorSwapConfig::save)
                .build()
                .generateScreen(parent);
    }

 }

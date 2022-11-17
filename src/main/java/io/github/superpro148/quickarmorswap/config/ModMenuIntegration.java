package io.github.superpro148.quickarmorswap.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.superpro148.quickarmorswap.config.QuickArmorSwapConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return QuickArmorSwapConfig.createGui(parent);
        };
    }
}

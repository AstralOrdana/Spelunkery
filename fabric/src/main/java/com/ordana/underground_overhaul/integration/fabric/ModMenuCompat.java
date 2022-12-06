package com.ordana.underground_overhaul.integration.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.ordana.underground_overhaul.configs.ClientConfigs;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClientConfigs.CONFIG_SPEC::makeScreen;
    }
}
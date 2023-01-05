package com.ordana.spelunkery.integration.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.ordana.spelunkery.configs.ClientConfigs;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClientConfigs.CONFIG_SPEC::makeScreen;
    }
}
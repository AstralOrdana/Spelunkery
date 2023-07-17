package com.ordana.spelunkery.integration.fabric;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModBlocks;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.mehvahdjukaar.moonlight.api.platform.configs.fabric.FabricConfigListScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModConfigScreen::new;
    }

    private static class ModConfigScreen extends FabricConfigListScreen {

        public ModConfigScreen(Screen parent) {
            super(Spelunkery.MOD_ID, ModBlocks.ROCK_SALT.get().asItem().getDefaultInstance(),
                    Component.literal("\u00A76Spelunkery Configs"),
                    Spelunkery.res("textures/block/rock_salt_brick.png"),
                    parent, ClientConfigs.CLIENT_SPEC, CommonConfigs.SERVER_SPEC);
        }

        @Override
        protected void addExtraButtons() {

            int y = this.height - 27;
            int centerX = this.width / 2;

            this.addRenderableWidget(new Button(centerX - 45, y, 90, 20, CommonComponents.GUI_BACK, (button) -> this.minecraft.setScreen(this.parent)));

            //TODO: link buttons

        }

    }

}
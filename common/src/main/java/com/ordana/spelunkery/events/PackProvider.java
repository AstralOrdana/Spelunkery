package com.ordana.spelunkery.events;

import com.google.gson.JsonElement;
import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

public class PackProvider extends DynServerResourcesProvider {

    public static final PackProvider INSTANCE = new PackProvider();

    public PackProvider() {
        super(new DynamicDataPack(Spelunkery.res("generated_pack"), Pack.Position.TOP, true, true));
        this.dynamicPack.generateDebugResources = true;
    }

    @Override
    public Logger getLogger() {
        return Spelunkery.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {

        ResourceLocation asurineTarget = new ResourceLocation("create", "crushing/asurine");
        ResourceLocation asurineSource = new ResourceLocation("spelunkery","injects/crushing/asurine.json");

        var json = manager.getResource(asurineSource);
        try (var bsStream = json.orElseThrow().open()) {
            JsonElement bsElement = RPUtils.deserializeJson(bsStream);

            if (PlatformHelper.isModLoaded("create")) {
                dynamicPack.addJson(asurineTarget, bsElement, ResType.RECIPES);
            }

        } catch (Exception ignored) {
        }
    }

    @Override
    public void generateStaticAssetsOnStartup(ResourceManager manager) {

    }


}
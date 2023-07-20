package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import java.io.IOException;

@Mod(Spelunkery.MOD_ID)
public class SpelunkeryForge {
    public static final String MOD_ID = Spelunkery.MOD_ID;

    public SpelunkeryForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);

        Spelunkery.commonInit();

        if (PlatHelper.getEnv().isClient()) {
            SpelunkeryClient.init();
        }
    }

    @SubscribeEvent
    public void addPackFinders(AddPackFindersEvent event) {

        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            registerBuiltinResourcePack(event, Component.literal("Better Vanilla Gems"), "better_vanilla_gems");
            registerBuiltinResourcePack(event, Component.literal("Unlit Redstone Ores"), "unlit_redstone_ores");
            registerBuiltinResourcePack(event, Component.literal("Emissive Ores"), "emissive_ores");
            registerBuiltinResourcePack(event, Component.literal("Emissive Better Vanilla Ores"), "emissive_better_vanilla_ores");
        }
    }

    private static void registerBuiltinResourcePack(AddPackFindersEvent event, MutableComponent name, String folder) {
        event.addRepositorySource((consumer, constructor) -> {
            String path = Spelunkery.res(folder).toString();
            IModFile file = ModList.get().getModFileById(Spelunkery.MOD_ID).getFile();
            try (PathPackResources pack = new PathPackResources(
                    path,
                    file.findResource("resourcepacks/" + folder));) {

                consumer.accept(constructor.create(
                        Spelunkery.res(folder).toString(),
                        name,
                        false,
                        () -> pack,
                        pack.getMetadataSection(PackMetadataSection.SERIALIZER),
                        Pack.Position.TOP,
                        PackSource.BUILT_IN,
                        false));

            } catch (IOException e) {
                if (!DatagenModLoader.isRunningDataGen())
                    e.printStackTrace();
            }
        });
    }
}


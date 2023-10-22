package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class ModGameEvents {
    public static void init() {
    }

    public static final Supplier<GameEvent> COMPASS_PING_EVENT = RegHelper.register(Spelunkery.res("compass_ping"),
            () -> new GameEvent("compass_ping", CommonConfigs.MAGNETITE_RANGE.get()), Registries.GAME_EVENT);
    public static final Supplier<GameEvent> FORK_TONE_EVENT = RegHelper.register(Spelunkery.res("fork_tone"),
            () -> new GameEvent("fork_tone", CommonConfigs.MAGNETITE_RANGE.get()), Registries.GAME_EVENT);

    /*

    public static final RegSupplier<PoiType> MAGNETITE_POI = RegHelper.register(Spelunkery.res("magnetite_poi"),
            () -> new PoiType(getBlockStates(ModBlocks.MAGNETITE.get()), 0, 1), Registry.POINT_OF_INTEREST_TYPE);



    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    private static ResourceKey<PoiType> createKey(String string) {
        return ResourceKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(string));
    }

    private static PoiType register(Registry<PoiType> registry, ResourceKey<PoiType> resourceKey, Set<BlockState> set, int i, int j) {
        PoiType poiType = new PoiType(set, i, j);
        Registry.register(registry, resourceKey, poiType);
        registerBlockStates(registry.getHolderOrThrow(resourceKey));
        return poiType;
    }

    private static void registerBlockStates(Holder<PoiType> holder) {
        holder.value().matchingStates().forEach((blockState) -> {
            Holder holder2 = (Holder)TYPE_BY_STATE.put(blockState, holder);
            if (holder2 != null) {
                throw Util.pauseInIde(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", blockState)));
            }
        });
    }

    public static final ResourceKey<PoiType> MAGNETITE_POI_OLD = createKey("magnetite_poi");
    private static final Map<BlockState, Holder<PoiType>> TYPE_BY_STATE = Maps.newHashMap();


    public static PoiType bootstrap(Registry<PoiType> registry) {
        return register(registry, MAGNETITE_POI_OLD, getBlockStates(ModBlocks.MAGNETITE.get()), 0, 1);
    }

     */
}

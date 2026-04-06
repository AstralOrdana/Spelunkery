package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.entities.EggPlither;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface ModEntityTypes {
    Supplier<EntityType<EggPlither>> EGG_PLITHER = registerEntityType(
            "egg_plither",
            EggPlither::new,
            MobCategory.MONSTER,
            builder -> builder
                    .fireImmune()
                    .immuneTo(Blocks.WITHER_ROSE)
                    .sized(0.9F, 3.5F)
                    .clientTrackingRange(10));

    private static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> operation) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, category);
        return RegHelper.registerEntityType(Spelunkery.res(name), operation.apply(builder));
    }

    static void init() {
    }

}

package com.ordana.spelunkery.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Early predefines later-used constants to
 * avoid heavy constant replicates.
 */
enum Components {
    SNEAK   (Component.keybind("key.sneak")),
    USE     (Component.keybind("key.use")),
    ;

    final Component val;

    Components(Component c) {
        val = c;
    }
}

enum Styles {
    GRAY        (ChatFormatting.GRAY),
    ITALIC_GRAY (ChatFormatting.GRAY, ChatFormatting.ITALIC),
    ;

    final Style val;

    Styles(ChatFormatting... formats) {
        this.val = Style.EMPTY.applyFormats(formats);
    }
}

public enum TranslationUtils {
    CROUCH              ("tooltip.spelunkery.hold_crouch",
            Style.EMPTY.withColor(ChatFormatting.GOLD), Components.SNEAK.val),
    THROWABLE           ("tooltip.spelunkery.throwable", Styles.ITALIC_GRAY.val),
    GRINDABLE           ("tooltip.spelunkery.grindable", Styles.ITALIC_GRAY.val),
    DIAMOND_GRINDABLE   ("tooltip.spelunkery.diamond_grindable", Styles.ITALIC_GRAY.val),
    DEATH_KEEP          ("tooltip.spelunkery.keep_on_death",
            Style.EMPTY.applyFormat(ChatFormatting.DARK_AQUA)),
    GRINDSTONE_1        ("tooltip.spelunkery.grindstone_1", Styles.GRAY.val, Components.USE.val),
    GRINDSTONE_2        ("tooltip.spelunkery.grindstone_2", Styles.GRAY.val,
            Components.SNEAK.val, Components.USE.val),
    ROPE_LADDER_3       ("tooltip.spelunkery.rope_ladder_3", Styles.GRAY.val,
            Components.SNEAK.val, Components.USE.val),
    MAGNETIC_COMPASS_4  ("tooltip.spelunkery.magnetic_compass_4", Styles.GRAY.val,
            Components.SNEAK.val, Components.USE.val),
    ITEM_MAGNET_4       ("tooltip.spelunkery.item_magnet_4", Styles.GRAY.val, Components.USE.val),
    HAMMER_AND_CHISEL_4 ("tooltip.spelunkery.hammer_and_chisel_4", Styles.GRAY.val,
            Components.SNEAK.val, Components.USE.val),
    NEPHRITE_CHARM_3    ("tooltip.spelunkery.nephrite_charm_3", Styles.GRAY.val,
            Components.SNEAK.val, Components.USE.val),
    ;

    private final MutableComponent component;

    /**
     * Returns a copy of the inner component
     * @return A copy of the inner component
     * @apiNote Creates a copy to avoid exposing inner value
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public MutableComponent component() {
        return component.copy();
    }

    @NotNull
    public static MutableComponent text(String s, ChatFormatting... formats) {
        if (formats != null && formats.length != 0) {
            return Component.translatable(s).withStyle(formats);
        }
        return Component.translatable(s);
    }

    TranslationUtils(String s, Style st, Object... o) {
        component = Component.translatable(s, o);
        if (st != null) {
            component.setStyle(st);
        }
    }
}

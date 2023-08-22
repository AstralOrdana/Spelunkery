package com.ordana.spelunkery.fluids;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.reg.ModFluids;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PortalFluidRenderer extends ModFluidRenderProperties {
    private final ResourceLocation overlay;
    private final ResourceLocation renderOverlay;
    private final Vec3 fogColor;

    public PortalFluidRenderer(ResourceLocation still, ResourceLocation flowing, int tint, ResourceLocation overlay, ResourceLocation renderOverlay, Vec3 fogColor) {
        super(still, flowing, tint);
        this.overlay = overlay;
        this.renderOverlay = renderOverlay;
        this.fogColor = fogColor;
    }


    static ResourceLocation portalFluidCommon = Spelunkery.res("block/portal_fluid");
    static ResourceLocation portalFluidRare = Spelunkery.res("block/portal_fluid_rare");

    static ResourceLocation portalFluidN = Spelunkery.res("block/portal_fluid_n");
    static ResourceLocation portalFluidE = Spelunkery.res("block/portal_fluid_e");
    static ResourceLocation portalFluidS = Spelunkery.res("block/portal_fluid_s");
    static ResourceLocation portalFluidW = Spelunkery.res("block/portal_fluid_w");

    static ResourceLocation portalFluidSW = Spelunkery.res("block/portal_fluid_sw");
    static ResourceLocation portalFluidSE = Spelunkery.res("block/portal_fluid_se");
    static ResourceLocation portalFluidNW = Spelunkery.res("block/portal_fluid_nw");
    static ResourceLocation portalFluidNE = Spelunkery.res("block/portal_fluid_ne");

    static ResourceLocation portalFluidNS = Spelunkery.res("block/portal_fluid_ns");
    static ResourceLocation portalFluidEW = Spelunkery.res("block/portal_fluid_ew");

    static ResourceLocation portalFluidWNE = Spelunkery.res("block/portal_fluid_wne");
    static ResourceLocation portalFluidNES = Spelunkery.res("block/portal_fluid_nes");
    static ResourceLocation portalFluidESW = Spelunkery.res("block/portal_fluid_esw");
    static ResourceLocation portalFluidSWN = Spelunkery.res("block/portal_fluid_swn");

    static ResourceLocation portalFluidNESW = Spelunkery.res("block/portal_fluid_nesw");
    static ResourceLocation portalFluidNONE = Spelunkery.res("block/portal_fluid_none");

    static Level level = Minecraft.getInstance().level;


    TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];
    TextureAtlasSprite[] spritesRare = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteN = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteE = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteS = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteW = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteSW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteSE = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNE = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteNS = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteEW = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteWNE  = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNES  = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteESW  = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteSWN  = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteNESW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNONE = new TextureAtlasSprite[3];

    private boolean isRandomPos(BlockPos pos) {
        Random random = new Random(Mth.getSeed(pos));
        return random.nextInt(10) == 0;
    }

    private boolean getCorner(BlockPos pos) {
        return (pos.getX() % 2 == 0 && pos.getZ() % 2 == 0);
    }

    private boolean isFluidAdjacent(BlockPos pos, Direction dir) {
        Level level = Minecraft.getInstance().level;
        return level != null && !level.getFluidState(pos.relative(dir)).is(ModFluids.PORTAL_FLUID.get());
    }

    @Override
    public ResourceLocation getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        var texture = portalFluidNONE;
        if (isRandomPos(pos)) texture = portalFluidCommon;

        if (isFluidAdjacent(pos, Direction.NORTH) && isFluidAdjacent(pos, Direction.SOUTH) && isFluidAdjacent(pos, Direction.EAST) && isFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidNESW;
        }
        else if (isFluidAdjacent(pos, Direction.NORTH)) {
            texture = portalFluidN;

            if (isFluidAdjacent(pos, Direction.EAST)) {
                texture = portalFluidNE;

                if (isFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidWNE;
                }
            }

            else if (isFluidAdjacent(pos, Direction.WEST)) {
                texture = portalFluidNW;

                if (isFluidAdjacent(pos, Direction.SOUTH)) {
                    return portalFluidSWN;
                }
            }

            else if (isFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidNS;

                if (isFluidAdjacent(pos, Direction.EAST)) {
                    return portalFluidNES;
                }
            }
        }

        else if (isFluidAdjacent(pos, Direction.EAST)) {
            texture = portalFluidE;

            if (isFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidEW;
            }

            else if (isFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidSE;

                if (isFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidESW;
                }
            }
        }

        else if (isFluidAdjacent(pos, Direction.SOUTH)) {
            texture = portalFluidS;

            if (isFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidSW;
            }
        }

        else if (isFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidW;
        }

        //else texture = portalFluidNONE;

        return texture;



        //if (getCorner(pos)) return portalFluidSW;
        //if (getCorner(pos.south())) return portalFluidNW;
        //if (getCorner(pos.east())) return portalFluidSE;
        //else return portalFluidNE;
    }

    public void reloadTextures(TextureAtlas textureAtlas) {

        //still textures
        sprites[0] = textureAtlas.getSprite(portalFluidCommon);
        spritesRare[0] = textureAtlas.getSprite(portalFluidRare);

        portalFluidSpriteN[0] = textureAtlas.getSprite(portalFluidN);
        portalFluidSpriteE[0] = textureAtlas.getSprite(portalFluidE);
        portalFluidSpriteS[0] = textureAtlas.getSprite(portalFluidS);
        portalFluidSpriteW[0] = textureAtlas.getSprite(portalFluidW);

        portalFluidSpriteSW[0] = textureAtlas.getSprite(portalFluidSW);
        portalFluidSpriteSE[0] = textureAtlas.getSprite(portalFluidSE);
        portalFluidSpriteNW[0] = textureAtlas.getSprite(portalFluidNW);
        portalFluidSpriteNE[0] = textureAtlas.getSprite(portalFluidNE);

        portalFluidSpriteNS[0] = textureAtlas.getSprite(portalFluidNS);
        portalFluidSpriteEW[0] = textureAtlas.getSprite(portalFluidEW);

        portalFluidSpriteWNE[0] = textureAtlas.getSprite(portalFluidWNE);
        portalFluidSpriteNES[0] = textureAtlas.getSprite(portalFluidNES);
        portalFluidSpriteESW[0] = textureAtlas.getSprite(portalFluidESW);
        portalFluidSpriteSWN[0] = textureAtlas.getSprite(portalFluidSWN);

        portalFluidSpriteNESW[0] = textureAtlas.getSprite(portalFluidNESW);
        portalFluidSpriteNONE[0] = textureAtlas.getSprite(portalFluidNONE);

        //flowing textures
        sprites[1] = textureAtlas.getSprite(getFlowingTexture());
        spritesRare[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteN[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteS[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteW[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteSW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteSE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNE[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteNS[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteEW[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteWNE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNES[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteESW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteSWN[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteNESW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNONE[1] = textureAtlas.getSprite(getFlowingTexture());


        var overlayTexture = this.getOverlayTexture();
        if (overlayTexture != null) {
            sprites[2] = textureAtlas.getSprite(overlayTexture);
            spritesRare[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteN[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteS[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteW[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteSW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteSE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNE[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteNS[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteEW[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteWNE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNES[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteESW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteSWN[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteNESW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNONE[2] = textureAtlas.getSprite(overlayTexture);
        }
    }

    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        if (pos == null) return sprites;
        var texture = portalFluidSpriteNONE;
        if (isRandomPos(pos)) texture = sprites;

        if (isFluidAdjacent(pos, Direction.NORTH) && isFluidAdjacent(pos, Direction.SOUTH) && isFluidAdjacent(pos, Direction.EAST) && isFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidSpriteNESW;
        }
        else if (isFluidAdjacent(pos, Direction.NORTH)) {
            texture = portalFluidSpriteN;

            if (isFluidAdjacent(pos, Direction.EAST)) {
                texture = portalFluidSpriteNE;

                if (isFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidSpriteWNE;
                }
            }

            if (isFluidAdjacent(pos, Direction.WEST)) {
                texture = portalFluidSpriteNW;

                if (isFluidAdjacent(pos, Direction.SOUTH)) {
                    return portalFluidSpriteSWN;
                }
            }

            if (isFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidSpriteNS;

                if (isFluidAdjacent(pos, Direction.EAST)) {
                    return portalFluidSpriteNES;
                }
            }
        }

        else if (isFluidAdjacent(pos, Direction.EAST)) {
            texture = portalFluidSpriteE;

            if (isFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidSpriteSE;

                if (isFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidSpriteESW;
                }
            }

            else if (isFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidSpriteEW;
            }
        }

        else if (isFluidAdjacent(pos, Direction.SOUTH)) {
            texture = portalFluidSpriteS;

            if (isFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidSpriteSW;
            }
        }

        else if (isFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidSpriteW;
        }

        //else texture = portalFluidNONE;

        return texture;
        
        //if (getCorner(pos)) return portalFluidSpriteSW;
        //if (getCorner(pos.south())) return portalFluidSpriteNW;
        //if (getCorner(pos.east())) return portalFluidSpriteSE;
        //else return portalFluidSpriteNE;
    }

    @Nullable
    @Override
    public ResourceLocation getOverlayTexture() {
        return this.overlay;
    }

    public ResourceLocation getOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getOverlayTexture();
    }

    public ResourceLocation getRenderOverlayTexture() {
        return this.renderOverlay;
    }

    public ResourceLocation getRenderOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getRenderOverlayTexture();
    }

    public Vec3 modifyFogColor() {
        return this.fogColor;
    }

    public Vec3 modifyFogColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.modifyFogColor();
    }

}

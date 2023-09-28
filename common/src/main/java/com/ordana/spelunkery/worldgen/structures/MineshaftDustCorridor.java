package com.ordana.spelunkery.worldgen.structures;

import com.ordana.spelunkery.blocks.DustBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModWorldgenFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.Nullable;

public class MineshaftDustCorridor extends MineshaftPieces.MineShaftPiece {

    private final boolean hasRails;
    private final boolean dustCorridor;
    private boolean hasPlacedDust;
    private final int numSections;

    public MineshaftDustCorridor(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super(ModWorldgenFeatures.MINESHAFT_DUST_CORRIDOR.get(), compoundTag);
        this.hasRails = compoundTag.getBoolean("hr");
        this.dustCorridor = compoundTag.getBoolean("sc");
        this.hasPlacedDust = compoundTag.getBoolean("hps");
        this.numSections = compoundTag.getInt("Num");
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("hr", this.hasRails);
        tag.putBoolean("sc", this.dustCorridor);
        tag.putBoolean("hps", this.hasPlacedDust);
        tag.putInt("Num", this.numSections);
    }

    public MineshaftDustCorridor(int i, RandomSource randomSource, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
        super(ModWorldgenFeatures.MINESHAFT_DUST_CORRIDOR.get(), i, type, boundingBox);
        this.setOrientation(direction);
        this.hasRails = randomSource.nextInt(3) == 0;
        this.dustCorridor = !this.hasRails && randomSource.nextInt(23) == 0;
        if (this.getOrientation().getAxis() == Direction.Axis.Z) {
            this.numSections = boundingBox.getZSpan() / 5;
        } else {
            this.numSections = boundingBox.getXSpan() / 5;
        }

    }

    @Nullable
    public static BoundingBox findCorridorSize(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction direction) {
        for(int i = random.nextInt(3) + 2; i > 0; --i) {
            int j = i * 5;
            BoundingBox boundingBox;
            switch(direction) {
                case NORTH:
                default:
                    boundingBox = new BoundingBox(0, 0, -(j - 1), 2, 2, 0);
                    break;
                case SOUTH:
                    boundingBox = new BoundingBox(0, 0, 0, 2, 2, j - 1);
                    break;
                case WEST:
                    boundingBox = new BoundingBox(-(j - 1), 0, 0, 0, 2, 2);
                    break;
                case EAST:
                    boundingBox = new BoundingBox(0, 0, 0, j - 1, 2, 2);
            }

            boundingBox.move(x, y, z);
            if (pieces.findCollisionPiece(boundingBox) == null) {
                return boundingBox;
            }
        }

        return null;
    }

    public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
        int i = this.getGenDepth();
        int j = random.nextInt(4);
        Direction direction = this.getOrientation();
        if (direction != null) {
            switch(direction) {
                case NORTH:
                default:
                    if (j <= 1) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, direction, i);
                    } else if (j == 2) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.WEST, i);
                    } else {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.EAST, i);
                    }
                    break;
                case SOUTH:
                    if (j <= 1) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, direction, i);
                    } else if (j == 2) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.WEST, i);
                    } else {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.EAST, i);
                    }
                    break;
                case WEST:
                    if (j <= 1) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, i);
                    } else if (j == 2) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                    } else {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                    }
                    break;
                case EAST:
                    if (j <= 1) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, i);
                    } else if (j == 2) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                    } else {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                    }
            }
        }

        if (i < 8) {
            int k;
            int l;
            if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                for(k = this.boundingBox.minX() + 3; k + 3 <= this.boundingBox.maxX(); k += 5) {
                    l = random.nextInt(5);
                    if (l == 0) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, k, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i + 1);
                    } else if (l == 1) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, k, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i + 1);
                    }
                }
            } else {
                for(k = this.boundingBox.minZ() + 3; k + 3 <= this.boundingBox.maxZ(); k += 5) {
                    l = random.nextInt(5);
                    if (l == 0) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), k, Direction.WEST, i + 1);
                    } else if (l == 1) {
                        MineshaftPieces.generateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), k, Direction.EAST, i + 1);
                    }
                }
            }
        }

    }

    protected boolean createChest(WorldGenLevel level, BoundingBox box, RandomSource random, int x, int y, int z, ResourceLocation lootTable) {
        BlockPos blockPos = this.getWorldPos(x, y, z);
        if (box.isInside(blockPos) && level.getBlockState(blockPos).isAir() && !level.getBlockState(blockPos.below()).isAir()) {
            BlockState blockState = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
            this.placeBlock(level, blockState, x, y, z, box);
            MinecartChest minecartChest = new MinecartChest(level.getLevel(), (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D);
            minecartChest.setLootTable(lootTable, random.nextLong());
            level.addFreshEntity(minecartChest);
            return true;
        } else {
            return false;
        }
    }

    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        if (!this.isInInvalidLocation(level, box)) {
            int m = this.numSections * 5 - 1;
            BlockState blockState = this.type.getPlanksState();
            this.generateBox(level, box, 0, 0, 0, 2, 1, m, CAVE_AIR, CAVE_AIR, false);
            this.generateMaybeBox(level, box, random, 0.8F, 0, 2, 0, 2, 2, m, CAVE_AIR, CAVE_AIR, false, false);
            if (this.dustCorridor) {
                this.generateMaybeBox(level, box, random, 0.6F, 0, 0, 0, 2, 0, m, ModBlocks.DUST.get().defaultBlockState().setValue(DustBlock.LAYERS_8, 1), CAVE_AIR, false, true);
                this.generateMaybeBox(level, box, random, 0.4F, 0, 0, 0, 2, 0, m, ModBlocks.DUST.get().defaultBlockState().setValue(DustBlock.LAYERS_8, 3), CAVE_AIR, false, true);
                this.generateMaybeBox(level, box, random, 0.2F, 0, 0, 0, 2, 0, m, ModBlocks.DUST.get().defaultBlockState().setValue(DustBlock.LAYERS_8, 5), CAVE_AIR, false, true);
                this.generateMaybeBox(level, box, random, 0.05F, 0, 0, 0, 2, 0, m, ModBlocks.DUST.get().defaultBlockState().setValue(DustBlock.LAYERS_8, 8), CAVE_AIR, false, true);
            }

            int n;
            int o;
            for(n = 0; n < this.numSections; ++n) {
                o = 2 + n * 5;
                this.placeSupport(level, box, 0, 0, o, 2, 2, random);
                this.maybePlaceDust(level, box, random, 0.1F, 0, 0, o - 1);
                this.maybePlaceDust(level, box, random, 0.1F, 2, 0, o - 1);
                this.maybePlaceDust(level, box, random, 0.1F, 0, 0, o + 1);
                this.maybePlaceDust(level, box, random, 0.1F, 2, 0, o + 1);
                this.maybePlaceDust(level, box, random, 0.05F, 0, 0, o - 2);
                this.maybePlaceDust(level, box, random, 0.05F, 2, 0, o - 2);
                this.maybePlaceDust(level, box, random, 0.05F, 0, 0, o + 2);
                this.maybePlaceDust(level, box, random, 0.05F, 2, 0, o + 2);
                this.maybePlaceWebs(level, box, random, 0.1F, 0, 2, o - 1);
                this.maybePlaceWebs(level, box, random, 0.1F, 2, 2, o - 1);
                this.maybePlaceWebs(level, box, random, 0.1F, 0, 2, o + 1);
                this.maybePlaceWebs(level, box, random, 0.1F, 2, 2, o + 1);
                this.maybePlaceWebs(level, box, random, 0.05F, 0, 2, o - 2);
                this.maybePlaceWebs(level, box, random, 0.05F, 2, 2, o - 2);
                this.maybePlaceWebs(level, box, random, 0.05F, 0, 2, o + 2);
                this.maybePlaceWebs(level, box, random, 0.05F, 2, 2, o + 2);
                if (random.nextInt(100) == 0) {
                    this.createChest(level, box, random, 2, 0, o - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
                }

                if (random.nextInt(100) == 0) {
                    this.createChest(level, box, random, 0, 0, o + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
                }

                if (this.dustCorridor && !this.hasPlacedDust) {
                    int q = o - 1 + random.nextInt(3);
                    BlockPos blockPos = this.getWorldPos(1, 0, q);
                    if (box.isInside(blockPos) && this.isInterior(level, 1, 0, q, box)) {
                        this.hasPlacedDust = true;
                        level.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 2);
                        BlockEntity blockEntity = level.getBlockEntity(blockPos);
                        if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                            spawnerBlockEntity.getSpawner().setEntityId(ModEntities.DUST_BUNNY.get());
                        }
                    }
                }
            }

            for(n = 0; n <= 2; ++n) {
                for(o = 0; o <= m; ++o) {
                    this.setPlanksBlock(level, box, blockState, n, -1, o);
                }
            }

            this.placeDoubleLowerOrUpperSupport(level, box, 0, -1, 2);
            if (this.numSections > 1) {
                o = m - 2;
                this.placeDoubleLowerOrUpperSupport(level, box, 0, -1, o);
            }

            if (this.hasRails) {
                BlockState blockState2 = (BlockState)Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);

                for(int p = 0; p <= m; ++p) {
                    BlockState blockState3 = this.getBlock(level, 1, -1, p, box);
                    if (!blockState3.isAir() && blockState3.isSolidRender(level, this.getWorldPos(1, -1, p))) {
                        float f = this.isInterior(level, 1, 0, p, box) ? 0.7F : 0.9F;
                        this.maybeGenerateBlock(level, box, random, f, 1, 0, p, blockState2);
                    }
                }
            }

        }
    }

    private void placeDoubleLowerOrUpperSupport(WorldGenLevel level, BoundingBox box, int x, int y, int z) {
        BlockState blockState = this.type.getWoodState();
        BlockState blockState2 = this.type.getPlanksState();
        if (this.getBlock(level, x, y, z, box).is(blockState2.getBlock())) {
            this.fillPillarDownOrChainUp(level, blockState, x, y, z, box);
        }

        if (this.getBlock(level, x + 2, y, z, box).is(blockState2.getBlock())) {
            this.fillPillarDownOrChainUp(level, blockState, x + 2, y, z, box);
        }

    }

    protected void fillColumnDown(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox box) {
        BlockPos.MutableBlockPos mutableBlockPos = this.getWorldPos(x, y, z);
        if (box.isInside(mutableBlockPos)) {
            int i = mutableBlockPos.getY();

            while(this.isReplaceableByStructures(level.getBlockState(mutableBlockPos)) && mutableBlockPos.getY() > level.getMinBuildHeight() + 1) {
                mutableBlockPos.move(Direction.DOWN);
            }

            if (this.canPlaceColumnOnTopOf(level, mutableBlockPos, level.getBlockState(mutableBlockPos))) {
                while(mutableBlockPos.getY() < i) {
                    mutableBlockPos.move(Direction.UP);
                    level.setBlock(mutableBlockPos, state, 2);
                }

            }
        }
    }

    protected void fillPillarDownOrChainUp(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox box) {
        BlockPos.MutableBlockPos mutableBlockPos = this.getWorldPos(x, y, z);
        if (box.isInside(mutableBlockPos)) {
            int i = mutableBlockPos.getY();
            int j = 1;
            boolean bl = true;

            for(boolean bl2 = true; bl || bl2; ++j) {
                BlockState blockState;
                boolean bl3;
                if (bl) {
                    mutableBlockPos.setY(i - j);
                    blockState = level.getBlockState(mutableBlockPos);
                    bl3 = this.isReplaceableByStructures(blockState) && !blockState.is(Blocks.LAVA);
                    if (!bl3 && this.canPlaceColumnOnTopOf(level, mutableBlockPos, blockState)) {
                        fillColumnBetween(level, state, mutableBlockPos, i - j + 1, i);
                        return;
                    }

                    bl = j <= 20 && bl3 && mutableBlockPos.getY() > level.getMinBuildHeight() + 1;
                }

                if (bl2) {
                    mutableBlockPos.setY(i + j);
                    blockState = level.getBlockState(mutableBlockPos);
                    bl3 = this.isReplaceableByStructures(blockState);
                    if (!bl3 && this.canHangChainBelow(level, mutableBlockPos, blockState)) {
                        level.setBlock(mutableBlockPos.setY(i + 1), this.type.getFenceState(), 2);
                        fillColumnBetween(level, Blocks.CHAIN.defaultBlockState(), mutableBlockPos, i + 2, i + j);
                        return;
                    }

                    bl2 = j <= 50 && bl3 && mutableBlockPos.getY() < level.getMaxBuildHeight() - 1;
                }
            }

        }
    }

    private static void fillColumnBetween(WorldGenLevel level, BlockState state, BlockPos.MutableBlockPos pos, int minY, int maxY) {
        for(int i = minY; i < maxY; ++i) {
            level.setBlock(pos.setY(i), state, 2);
        }

    }

    private boolean canPlaceColumnOnTopOf(LevelReader level, BlockPos pos, BlockState state) {
        return state.isFaceSturdy(level, pos, Direction.UP);
    }

    private boolean canHangChainBelow(LevelReader level, BlockPos pos, BlockState state) {
        return Block.canSupportCenter(level, pos, Direction.DOWN) && !(state.getBlock() instanceof FallingBlock);
    }

    private void placeSupport(WorldGenLevel level, BoundingBox box, int minX, int minY, int z, int maxY, int maxX, RandomSource random) {
        if (this.isSupportingBox(level, box, minX, maxX, maxY, z)) {
            BlockState blockState = this.type.getPlanksState();
            BlockState blockState2 = this.type.getFenceState();
            this.generateBox(level, box, minX, minY, z, minX, maxY - 1, z, blockState2.setValue(FenceBlock.WEST, true), CAVE_AIR, false);
            this.generateBox(level, box, maxX, minY, z, maxX, maxY - 1, z, blockState2.setValue(FenceBlock.EAST, true), CAVE_AIR, false);
            if (random.nextInt(4) == 0) {
                this.generateBox(level, box, minX, maxY, z, minX, maxY, z, blockState, CAVE_AIR, false);
                this.generateBox(level, box, maxX, maxY, z, maxX, maxY, z, blockState, CAVE_AIR, false);
            } else {
                this.generateBox(level, box, minX, maxY, z, maxX, maxY, z, blockState, CAVE_AIR, false);
                this.maybeGenerateBlock(level, box, random, 0.05F, minX + 1, maxY, z - 1, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH));
                this.maybeGenerateBlock(level, box, random, 0.05F, minX + 1, maxY, z + 1, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH));
            }

        }
    }

    private void maybePlaceDust(WorldGenLevel level, BoundingBox box, RandomSource random, float chance, int x, int y, int z) {
        if (this.isInterior(level, x, y, z, box) && random.nextFloat() < chance && this.hasSturdyNeighbours(level, box, x, y, z, 2)) {
            this.placeBlock(level, ModBlocks.DUST.get().defaultBlockState().setValue(DustBlock.LAYERS_8, random.nextIntBetweenInclusive(1, 3)), x, y, z, box);
        }
    }

    private void maybePlaceWebs(WorldGenLevel level, BoundingBox box, RandomSource random, float chance, int x, int y, int z) {
        if (this.isInterior(level, x, y, z, box) && random.nextFloat() < chance && this.hasSturdyNeighbours(level, box, x, y, z, 2)) {
            this.placeBlock(level, Blocks.COBWEB.defaultBlockState(), x, y, z, box);
        }
    }

    private boolean hasSturdyNeighbours(WorldGenLevel level, BoundingBox box, int x, int y, int z, int required) {
        BlockPos.MutableBlockPos mutableBlockPos = this.getWorldPos(x, y, z);
        int i = 0;
        Direction[] var9 = Direction.values();
        int var10 = var9.length;

        for(int var11 = 0; var11 < var10; ++var11) {
            Direction direction = var9[var11];
            mutableBlockPos.move(direction);
            if (box.isInside(mutableBlockPos) && level.getBlockState(mutableBlockPos).isFaceSturdy(level, mutableBlockPos, direction.getOpposite())) {
                ++i;
                if (i >= required) {
                    return true;
                }
            }

            mutableBlockPos.move(direction.getOpposite());
        }

        return false;
    }
}
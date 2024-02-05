/*
Methods in this class from MajruszLibrary under the MIT license

Full repo can be found here: https://github.com/Majrusz/MajruszLibrary

        Permission is hereby granted, free of charge, to any person obtaining
        a copy of this software and associated documentation files (the
        'Software'), to deal in the Software without restriction, including
        without limitation the rights to use, copy, modify, merge, publish,
        distribute, sublicense, and/or sell copies of the Software, and to
        permit persons to whom the Software is furnished to do so, subject to
        the following conditions:

        The above copyright notice and this permission notice shall be
        included in all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
        EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
        MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
        IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
        CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
        TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
        SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.ordana.spelunkery.utils;

import com.mojang.datafixers.util.Pair;
import com.ordana.spelunkery.reg.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Optional;

public class LevelHelper {
    public static Pair<Vec3, ServerLevel> getSpawnData(ServerPlayer player) {
        BlockPos respawnPosition = player.getRespawnPosition();
        ServerLevel serverLevel = player.server.getLevel(player.getRespawnDimension());
        Vec3 exactSpawnPosition = null;
        if (serverLevel == null)
            return new Pair<>(Vec3.ZERO, null);

        if (respawnPosition != null) {
            float angle = player.getRespawnAngle();
            Optional< Vec3 > spawnPosition = Player.findRespawnPositionAndUseSpawnBlock(serverLevel, respawnPosition, angle, true, true);
            if (spawnPosition.isPresent())
                exactSpawnPosition = spawnPosition.get();
        }
        if (exactSpawnPosition == null) {
            serverLevel = player.server.getLevel(Level.OVERWORLD);
            assert serverLevel != null;
            exactSpawnPosition = Vec3.atCenterOf(serverLevel.getSharedSpawnPos());
        }
        return new Pair<>(exactSpawnPosition, serverLevel);
    }

    public static void teleportToSpawnPosition(ServerPlayer player) {
        Pair<Vec3, ServerLevel> spawnData = getSpawnData(player);
        Vec3 spawnPosition = spawnData.getFirst();
        ServerLevel serverLevel = spawnData.getSecond();

        player.teleportTo(serverLevel, spawnPosition.x, spawnPosition.y, spawnPosition.z, player.getYRot(), player.getXRot());
        player.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), 1.0f, 1.0f);
    }

    public static void teleportToAnchorPosition(ServerPlayer player, GlobalPos pos) {

        player.teleportTo(Objects.requireNonNull(player.server.getLevel(pos.dimension())), pos.pos().getX() + 0.5, pos.pos().getY() + 1, pos.pos().getZ() + 0.5, player.getYRot(), player.getXRot());
        player.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), 1.0f, 1.0f);
    }

    public static void teleportToWorldspawn(Level level, Entity entity) {
        Vec3 spawnPosition = Vec3.atCenterOf(level.getSharedSpawnPos());

        entity.teleportTo(spawnPosition.x, spawnPosition.y, spawnPosition.z);
        entity.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get());
    }

    /*
    public static void portalFluidBehavior(BlockState state, Level level, BlockPos pos, Entity entity, int tickCounter) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions() && !pos.equals(level.getSharedSpawnPos())) {
            if (entity.isInWater()) {
                tickCounter++;
                if (tickCounter < 1) {
                    entity.playSound(ModSoundEvents.PORTAL_FLUID_SUBMERGE, 1.0f, 1.0f);
                }
                level.scheduleTick(pos, this, 120);
                if (tickCounter >= 100) {

                    setTickCounter(0);

                    if (entity instanceof ServerPlayer player) {
                        if (player.isSecondaryUseActive()) return;
                        LevelHelper.teleportToSpawnPosition(player);
                    } else {
                        LevelHelper.teleportToWorldspawn(level, entity);
                        entity.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT, 1.0f, 1.0f);
                    }
                }
            }
        }
    }
     */
    
}

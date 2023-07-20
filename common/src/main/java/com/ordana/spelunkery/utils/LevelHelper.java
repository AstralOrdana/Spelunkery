/*
Methods in this class from MajruszLibrary under the MIT license

https://github.com/Majrusz/MajruszLibrary

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
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
            exactSpawnPosition = vec3(serverLevel.getSharedSpawnPos());
        }
        return new Pair<>(exactSpawnPosition, serverLevel);
    }

    public static void teleportToSpawnPosition(ServerPlayer player) {
        Pair<Vec3, ServerLevel> spawnData = getSpawnData(player);
        Vec3 spawnPosition = spawnData.getFirst();
        ServerLevel serverLevel = spawnData.getSecond();
        player.level.playSound(null, player.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 1f, 1f);

        player.teleportTo(serverLevel, spawnPosition.x, spawnPosition.y, spawnPosition.z, player.getYRot(), player.getXRot());
    }

    public static void teleportToAnchorPosition(ServerPlayer player, GlobalPos pos) {

        player.level.playSound(null, player.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 1f, 1f);
        player.teleportTo(Objects.requireNonNull(player.server.getLevel(pos.dimension())), pos.pos().getX() + 0.5, pos.pos().getY() + 1, pos.pos().getZ() + 0.5, player.getYRot(), player.getXRot());
    }

    public static void teleportToWorldspawn(Level level, Entity entity) {
        Vec3 spawnPosition = vec3(level.getSharedSpawnPos());

        entity.teleportTo(spawnPosition.x, spawnPosition.y, spawnPosition.z);
    }
    
    public static Vec3 vec3(BlockPos blockPosition) {
        return new Vec3(blockPosition.getX() + 0.5, blockPosition.getY() + 0.5, blockPosition.getZ() + 0.5);
    }
}

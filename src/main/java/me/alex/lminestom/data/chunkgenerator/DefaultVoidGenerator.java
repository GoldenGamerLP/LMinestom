package me.alex.lminestom.data.chunkgenerator;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DefaultVoidGenerator implements ChunkGenerator {

    private final LMinestomNoise overN;
    private final LMinestomNoise lNoise;
    private final LMinestomNoise riverTerrainNoise;
    private final Random Seed;
    private final List<Pos> chunkList = Collections.synchronizedList(new ArrayList<>());

    public DefaultVoidGenerator() {
        this.Seed = new Random(22112005);
        this.overN = new LMinestomNoise(LMinestomNoiseType.OverworldTerrain, Seed.nextInt());
        this.lNoise = new LMinestomNoise(LMinestomNoiseType.LandmassTerrain, Seed.nextInt());
        this.riverTerrainNoise = new LMinestomNoise(LMinestomNoiseType.RiverTerrain, Seed.nextInt());
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        List<Pos> water = new ArrayList<>();
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                int posX = chunkX * 16 + x;
                int posZ = chunkZ * 16 + z;


                var e = 0.0;
                synchronized (overN) {
                     e = (overN.GetNoise(1 * posX, 1 * posZ)
                            + 0.50 * overN.GetNoise(2 * posX, 2 * posZ)
                            + 0.25 * overN.GetNoise(4 * posX, 4 * posZ)
                            + 0.13 * overN.GetNoise(8 * posX, 8 * posZ)
                            + 0.06 * overN.GetNoise(16 * posX, 16 * posZ)
                            + 0.03 * overN.GetNoise(32 * posX, 32 * posZ));
                    e = e / (1.00 + 0.50 + 0.25 + 0.13 + 0.06 + 0.03);
                    //e = Math.round(e * 64.0) / 64.0;
                    e = Math.pow(e, 3.25);
                }


                var m = 0.0;
                synchronized (lNoise) {
                    m = (lNoise.GetNoise(1 * posX, 1 * posZ)
                            + 0.75 * lNoise.GetNoise(2 * posX, 2 * posZ)
                            + 0.33 * lNoise.GetNoise(4 * posX, 4 * posZ)
                            + 0.33 * lNoise.GetNoise(8 * posX, 8 * posZ)
                            + 0.33 * lNoise.GetNoise(16 * posX, 16 * posZ)
                            + 0.50 * lNoise.GetNoise(32 * posX, 32 * posZ));
                    m = m / (1.00 + 0.75 + 0.33 + 0.33 + 0.33 + 0.50);
                }

                var height = (int) (e * 64) + 64;
                Block block = getBlock(e, m);
                for (int y = 1; y < height; y++) {
                    batch.setBlock(x, y, z, Block.STONE);
                }

                batch.setBlock(x, height, z, block);
                batch.setBlock(x, 0, z, Block.BEDROCK);


                if (block.isLiquid()) {
                    water.add(new Pos(x, height, z));
                }
            }
        }
        if (!water.isEmpty()) {
            synchronized (chunkList) {
                water.addAll(chunkList.stream().filter(pos -> pos.distance(water.get(0)) <= 16).toList());
            }
            water.sort(Comparator.comparingInt(Point::blockY).reversed());
            if (water.get(0) != null) {
                Pos maxHeight = water.get(0);
                for (Pos pos : water) {
                    if (pos.blockY() <= maxHeight.blockY()) {
                        for (int y = pos.blockY(); y <= maxHeight.blockY(); y++) {
                            batch.setBlock(pos.blockX(), y, pos.blockZ(), Block.WATER);
                        }
                    }
                }
                synchronized (chunkList) {
                    chunkList.add(maxHeight);
                }
            }
        }
        water.clear();
        System.out.println(chunkList.size());
    }


    @Override
    public @Nullable List<ChunkPopulator> getPopulators() {
        return null;
    }

    private Block getBlock(double e, double m) {
        if (e < 0.1) return Block.WATER;
        if (e < 0.12) return Block.SAND;

        if (e > 0.8) {
            if (m < 0.1) return Block.COARSE_DIRT;
            if (m < 0.2) return Block.SNOW;
            if (m < 0.5) return Block.POWDER_SNOW;
            return Block.SNOW_BLOCK;
        }

        if (e > 0.6) {
            if (m < 0.33) return Block.SAND;
            if (m < 0.66) return Block.SNOW_BLOCK;
            return Block.SAND;
        }

        if (e > 0.3) {
            if (m < 0.16) return Block.GRASS;
            if (m < 0.50) return Block.GRASS_BLOCK;
            if (m < 0.83) return Block.GRASS_BLOCK;
            return Block.JUNGLE_WOOD;
        }

        if (m < 0.16) return Block.SAND;
        if (m < 0.33) return Block.GRASS_BLOCK;
        if (m < 0.66) return Block.GRASS_BLOCK;
        return Block.JUNGLE_LEAVES;
    }
}

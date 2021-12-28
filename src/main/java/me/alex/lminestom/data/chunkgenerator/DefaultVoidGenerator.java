package me.alex.lminestom.data.chunkgenerator;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DefaultVoidGenerator implements ChunkGenerator {

    private final LMinestomNoise overworldNoise;
    private final LMinestomNoise landmassNoise;
    private final LMinestomNoise riverTerrainNoise;
    private final Random Seed;

    public DefaultVoidGenerator() {
        this.Seed = new Random(22112005);
        this.overworldNoise = new LMinestomNoise(LMinestomNoiseType.OverworldTerrain,Seed.nextInt());
        this.landmassNoise = new LMinestomNoise(LMinestomNoiseType.LandmassTerrain,Seed.nextInt());
        this.riverTerrainNoise = new LMinestomNoise(LMinestomNoiseType.RiverTerrain,Seed.nextInt());
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                int posX = chunkX*16+x;
                int posZ = chunkZ*16+z;


                batch.setBlock(x, 0, z, Block.BEDROCK);

                // Get normalised balance between water and land
                double landmassDelta = landmassNoise.GetNoise(posX, posZ);
                double waterDelta = riverTerrainNoise.GetNoise(posX, posZ);
                double ratio = 1 / (landmassDelta + waterDelta);
                landmassDelta = landmassDelta * ratio;
                waterDelta = waterDelta * ratio;

                // Calculate height based on normalised balance
                double terrainHeight = 32 + 64 * landmassDelta * Math.pow(overworldNoise.GetNoise(posX, posZ), 1/3.0);
                double waterHeight = 64 * waterDelta * overworldNoise.GetNoise(posX, posZ);
                int height = (int) (terrainHeight + waterHeight);

                for (int y = 1; y < height; y++) {
                    batch.setBlock(posX, y, posZ, Block.DIRT);
                }
                batch.setBlock(posX, height, posZ, Block.DIRT);
                for (int y = height; y < 64; y++) {
                    batch.setBlock(posX, y, posZ, Block.WATER);
                }
            }
        }
    }

    @Override
    public void fillBiomes(@NotNull Biome[] biomes, int chunkX, int chunkZ) {
        Arrays.fill(biomes, Biome.PLAINS);
    }

    @Override
    public @Nullable List<ChunkPopulator> getPopulators() {
        return null;
    }
}

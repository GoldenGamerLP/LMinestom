package me.alex.lminestom.data.chunkgenerator;


import de.articdive.jnoise.JNoise;

public class LMinestomNoise {

    private final JNoise jNoise;

    public LMinestomNoise(Integer type, Integer random) {
        switch (type) {
            case 0 ->
                    //Landmass
                    jNoise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.0025).build();
            case 1 ->
                    //Overworld Terrain
                    jNoise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.015).build();
            case 2 ->
                    //OverworldFauna
                    jNoise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.015).build();
            case 3 -> jNoise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.015).build();
            case 4 -> jNoise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.015).build();
            case 5 -> jNoise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.015).build();
            default -> jNoise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.01).build();
        }
    }

    public double GetNoise(double x, double y) {
        return (jNoise.getNoise(x, y)) / 2 + 0.5;
    }

    public double GetNoise(double x, double y, double z) {
        return (jNoise.getNoise(x, y, z)) / 2 + 0.5;
    }
}

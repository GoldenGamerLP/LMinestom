package me.alex.lminestom.data.chunkgenerator;


import de.articdive.jnoise.JNoise;

public class LMinestomNoise {

    private final JNoise noise;

    public LMinestomNoise(Integer type, Integer random) {
        switch (type) {
            case 0 ->
                    //Landmass
                    noise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.0025).build();
            case 1 ->
                    //Overworld Terrain
                    noise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(2.02).build();
            case 2 ->
                    //OverworldFauna
                    noise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.005).build();
            case 3 -> noise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.02).build();
            case 4 -> noise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.02).build();
            case 5 -> noise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.02).build();
            default -> noise = JNoise.newBuilder().superSimplex().setSeed(random).setFrequency(0.01).build();
        }
    }

    public double GetNoise(double x, double y) {
        return (noise.getNoise(x,y) / 2) + 0.5;
    }

    public double GetNoise(double x, double y, double z) {
        return (noise.getNoise(x, y, z)) / 2 + 0.5;
    }
}

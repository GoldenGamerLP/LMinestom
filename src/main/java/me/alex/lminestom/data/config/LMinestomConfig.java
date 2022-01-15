package me.alex.lminestom.data.config;

import me.alex.lminestom.start.LMinestom;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class LMinestomConfig {

    private static volatile boolean isInit = false;
    private final Logger logger = LMinestom.getMainLogger();
    private final Properties properties = new Properties();
    private final File saveFile;


    public LMinestomConfig(File path) throws IOException {
        this.saveFile = path;
        if (isInit) {
            logger.error("You cant have multiple instances of this class");
            return;
        }
        isInit = true;

        if (saveFile == null) throw new UnsupportedOperationException("You need to specify a Path with File!");
        if (!this.saveFile.exists()) {
            if (createFile()) logger.info("The File was successfully created.");
            else {
                logger.info("Something went wrong during file creation.");
                return;
            }
        }
        reloadFileAndValues()
                .thenAccept(properties1 -> properties1.forEach((o, o2) -> System.setProperty((String) o, (String) o2)))
                .thenRun(() -> logger.info("Succesfully loaded/set config file!"));
    }

    public CompletableFuture<Properties> reloadFileAndValues() {
        return CompletableFuture.supplyAsync(() -> {
            try (FileReader reader = new FileReader(saveFile)) {
                properties.load(reader);
            } catch (IOException e) {
                logger.error("Something went wrong while loading the File: ", e);
            }

            try (FileWriter fileWriter = new FileWriter(saveFile)) {
                List<LMinestomDefaultValues> lValues = Arrays.stream(LMinestomDefaultValues.values())
                        .filter(lMinestomDefaultValues -> !properties.containsKey(lMinestomDefaultValues.getIdentifier()))
                        .toList();

                if (!lValues.isEmpty()) {
                    String values = lValues.stream().map(LMinestomDefaultValues::getIdentifier).toList().toString();
                    logger.info(String.format("Adding default values: %s", values));

                    lValues.forEach(lValue -> properties.put(lValue.getIdentifier(), lValue.getDefaultValue()));
                }

                properties.store(fileWriter, "LMinestom Configuration File");
            } catch (IOException e) {
                logger.error("Something went wrong while Saving the File: ", e);
            }
            return properties;
        });
    }

    public void saveFile() {
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            properties.store(fileWriter, "");
        } catch (IOException e) {
            logger.info("Something went wrong while storing save file", e);
        }
    }

    private CompletableFuture<Void> loadFileAndValues() {
        return CompletableFuture.runAsync(() -> {
            try (FileReader fileReader = new FileReader(saveFile)) {
                properties.load(fileReader);
            } catch (IOException e) {
                logger.info("Something went wrong while reading: ", e);

                for (LMinestomDefaultValues value : LMinestomDefaultValues.values()) {
                    properties.put(value.getIdentifier(), value.getDefaultValue());
                }
            }
        });
    }

    public String getConfigEntry(LMinestomDefaultValues config) {
        return System.getProperty(config.getIdentifier(), config.getDefaultValue());
    }

    public boolean createFile() throws IOException {
        return this.saveFile.createNewFile();
    }
}

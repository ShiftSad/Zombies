package codes.shiftmc.zombies;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WorldConverter {

    private final Path path;

    public WorldConverter(final Path path) {
        this.path = path;
        convert();
    }

    private void convert() {
        try (Stream<Path> paths = Files.walk(path)) {
            paths.forEach(path -> {
                Path polarPath = Path.of(path + ".polar");
                if (
                        !path.resolve("/regions").toFile().exists()
                        || polarPath.toFile().exists()
                ) return;
                try {
                    var polarWorld = AnvilPolar.anvilToPolar(path);
                    var bytes = PolarWriter.write(polarWorld);
                    Files.write(polarPath, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

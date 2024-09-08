package codes.shiftmc.zombies;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.nio.file.Path;
import java.text.DecimalFormat;

public class Zombies {

    public static void main(String[] args) {
        final var server = MinecraftServer.init();

        // Convert anvil worlds
        new WorldConverter(Path.of("./anvil"));

        final var instanceManager = MinecraftServer.getInstanceManager();
        final var instanceContainer = instanceManager.createInstanceContainer();

        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 1, Block.GLASS));
        instanceContainer.setChunkSupplier(LightingChunk::new);

        final var geh = MinecraftServer.getGlobalEventHandler();
        geh.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final var player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setGameMode(GameMode.CREATIVE);
            player.setRespawnPoint(new Pos(0, 2, 0));
        });

        // Debug show MSPT
        BossBar bossBar = BossBar.bossBar(Component.empty(), 1f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        DecimalFormat dec = new DecimalFormat("0.00");
        geh.addListener(ServerTickMonitorEvent.class, e -> {
            double tickTime = Math.floor(e.getTickMonitor().getTickTime() * 100.0) / 100.0;
            bossBar.name(
                    Component.text()
                            .append(Component.text("MSPT: " + dec.format(tickTime)))
            );
            bossBar.progress(Math.min((float)tickTime / (float)MinecraftServer.TICK_MS, 1f));

            if (tickTime > MinecraftServer.TICK_MS) bossBar.color(BossBar.Color.RED);
            else bossBar.color(BossBar.Color.GREEN);
        });

        geh.addListener(PlayerSpawnEvent.class, e ->
                e.getPlayer().showBossBar(bossBar)
        );

        geh.addListener(PlayerChatEvent.class, e -> {
            final var player = e.getPlayer();
            final var instance = player.getInstance();
            final var pos = player.getPosition();

            if (e.getMessage().equals("zombie")) {
                for (int i = 0; i < 100; i++) new BaseZombie(pos, instance);
            }

            if (e.getMessage().equals("kill")) {
                instance.getEntities().stream().filter(entity -> entity instanceof Zombie).forEach(entity -> ((Zombie) entity).kill());
            }
        });

        server.start("0.0.0.0", 25565);
    }
}

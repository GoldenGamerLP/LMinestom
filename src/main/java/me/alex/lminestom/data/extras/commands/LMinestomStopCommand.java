package me.alex.lminestom.data.extras.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.SimpleCommand;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class LMinestomStopCommand extends Command {


    public LMinestomStopCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);

        var literal = ArgumentType.Literal("now");

        setCondition((sender, commandString) -> sender.hasPermission("lminestom.stop"));
        setDefaultExecutor((sender, context) -> MinecraftServer.getSchedulerManager().buildTask(() -> {
            MinecraftServer.getConnectionManager().getOnlinePlayers().stream().filter(player -> player.hasPermission("lminestom.stop")).forEach(player -> player.sendMessage(Component.text(sender.asPlayer().getUsername() + "has shut the Server down!")));
        }).delay(Duration.ofSeconds(5)));

        addSyntax((sender, context) -> MinecraftServer.getSchedulerManager().buildTask(MinecraftServer::stopCleanly).delay(Duration.ofSeconds(1)), literal);

        MinecraftServer.getCommandManager().register(this);

    }
}

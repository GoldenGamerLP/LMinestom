package me.alex.lminestom.data.extras.commands;

import me.alex.lminestom.start.LMinestom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.extensions.DiscoveredExtension;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LMinestomExtensionManager extends Command {


    private final MiniMessage miniMessage = MiniMessage.get();
    private final ExtensionManager exManager = MinecraftServer.getExtensionManager();
    private final Logger logger = LMinestom.getMainLogger();

    private final String listTitleNothingLoaded = "<gray>There were <red>no <gray>extensions loaded.";
    private final String listTitle = "<gray>There were <green><extensions><gray> extensions loaded: \n";
    private final String extensionInfo = "<gray><hover:show_text:'<extensioninfo>'><namedtextcolor><extenionname><gray>,";
    private final String extensionHoverInfo = """
            Name: <name>\s
            Author: <author>\s
            Entry-Point: <entrypoint>\s
            Depend: <depend>""";


    public LMinestomExtensionManager(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);

        //setCondition(Conditions::playerOnly);

        var list = ArgumentType.Literal("list");
        var literal = ArgumentType.Literal("about");
        var extensions = ArgumentType.Word("extension").from(exManager.getExtensions().stream().map(extension -> extension.getOrigin().getName()).toArray(String[]::new));

        setDefaultExecutor((sender, context) -> sender.sendMessage("Use: exm list/about"));

        addSyntax((sender, context) -> getExtensionList(sender, context).thenAcceptAsync(sender::sendMessage), list);
        addSyntax((sender, context) -> getExtensionInfo(sender, context).thenAcceptAsync(sender::sendMessage), literal, extensions);

        MinecraftServer.getCommandManager().register(this);
        logger.info("Enabled ExtensionManager!");

    }

    private CompletableFuture<Component> getExtensionInfo(CommandSender sender, CommandContext context) {
        return CompletableFuture.supplyAsync(() -> {
            Component component;
            Extension extension = exManager.getExtension(context.get("extension"));
            if (extension == null) {
                component = miniMessage.parse("<red>You need to specify a extension</red>");
            } else {
                DiscoveredExtension origin = extension.getOrigin();
                component = miniMessage.parse(extensionHoverInfo,
                        Template.of("name", origin.getName()),
                        Template.of("author", Arrays.toString(origin.getAuthors())),
                        Template.of("entrypoint", origin.getEntrypoint()),
                        Template.of("depend", Arrays.toString(extension.getDependents().toArray())));
            }
            return component;
        });
    }


    private CompletableFuture<Component> getExtensionList(CommandSender commandSender, CommandContext context) {
        return CompletableFuture.supplyAsync(() -> {
            List<Extension> extensions = exManager.getExtensions().stream().toList();
            TextComponent.Builder builder = Component.text().append(Component.newline());

            if (extensions.isEmpty()) {
                builder.append(miniMessage.parse(listTitleNothingLoaded));
            } else {
                builder.append(miniMessage.parse(listTitle, Template.of("extensions", Component.text(extensions.size()))));

                for (Extension extension : extensions) {
                    DiscoveredExtension origin = extension.getOrigin();

                    Component exHoverInfo = miniMessage.parse(extensionHoverInfo,
                            Template.of("name", origin.getName()),
                            Template.of("author", Arrays.toString(origin.getAuthors())),
                            Template.of("entrypoint", origin.getEntrypoint()),
                            Template.of("depend", Arrays.toString(extension.getDependents().toArray())));

                    Component exInfo = miniMessage.parse(extensionInfo,
                            Template.of("extensioninfo", exHoverInfo),
                            Template.of("extenionname", origin.getName()),
                            Template.of("namedtextcolor", Component.text("").color(NamedTextColor.GREEN)));

                    builder.append(exInfo);
                }
            }
            return builder.asComponent();
        });
    }
}

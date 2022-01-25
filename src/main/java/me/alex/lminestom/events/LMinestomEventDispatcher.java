package me.alex.lminestom.events;

import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;
import org.slf4j.Logger;

public class LMinestomEventDispatcher {

    private final EventNode<Event> eventNode = EventNode.all("LMinestom").setPriority(0);
    private final MiniMessage miniMessage = MiniMessage.get();
    private final Component motd = miniMessage.parse(System.getProperty("lminestom.motd", "LMinestom by GoldenGamer_LP"));
    private final String kickMessage = "<red>Can not connect to server. Please try again later. \n <gray><reason>";
    private final int maxPlayers = Integer.getInteger("lminestom.server.maxplayers", 50);
    private final Logger logger = LMinestom.getMainLogger();

    public LMinestomEventDispatcher() {
        MinecraftServer.getGlobalEventHandler().addChild(eventNode);

        logger.info("Current MOTD: {}", miniMessage.serialize(motd));
    }

    public void registerListeners() {

        eventNode.addListener(PlayerDisconnectEvent.class, playerDisconnectEvent -> {

        });

        eventNode.addListener(PlayerLoginEvent.class, playerLoginEvent -> {
            if(!Boolean.getBoolean(LMinestomDefaultValues.EnableDefaultInstance.getIdentifier())) return;

            playerLoginEvent.setSpawningInstance(LMinestom.getDefaultInstance());
            playerLoginEvent.getPlayer().setRespawnPoint(Pos.ZERO.add(0, 100, 0));
        });

        eventNode.addListener(AsyncPlayerPreLoginEvent.class, asyncPlayerPreLoginEvent -> {
            if(MinecraftServer.getInstanceManager().getInstances().size() == 0) {
                Component kickMSG = miniMessage.parse(kickMessage, Template.of("reason", "Server has no World to join."));
                asyncPlayerPreLoginEvent.getPlayer().kick(kickMSG);
                MinecraftServer.getConnectionManager().removePlayer(asyncPlayerPreLoginEvent.getPlayer().getPlayerConnection());
            }

            int players = MinecraftServer.getConnectionManager().getOnlinePlayers().size();
            if (players > maxPlayers) {
                Component kickMSG = miniMessage.parse(kickMessage, Template.of("reason", "Server is full."));
                asyncPlayerPreLoginEvent.getPlayer().kick(kickMSG);
                MinecraftServer.getConnectionManager().removePlayer(asyncPlayerPreLoginEvent.getPlayer().getPlayerConnection());
            }
        });

        eventNode.addListener(ServerListPingEvent.class, serverListPingEvent -> {
            ResponseData responseData = serverListPingEvent.getResponseData();
            responseData.setDescription(motd);
            responseData.setOnline(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
            responseData.setMaxPlayer(maxPlayers);
            serverListPingEvent.setResponseData(responseData);
        });
    }
}

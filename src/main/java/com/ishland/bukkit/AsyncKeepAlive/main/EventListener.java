package com.ishland.bukkit.AsyncKeepAlive.main;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;

public class EventListener implements Listener {

    private Plugin plugin = null;

    public EventListener(Plugin plugin) {
	this.plugin = plugin;
    }

    public void onPlayerKick(PlayerKickEvent event) {
	if (event.getReason().equals("Timed out"))
	    event.setCancelled(true);
    }

}

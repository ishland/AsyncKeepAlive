package com.ishland.bukkit.AsyncKeepAlive.launcher;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.ishland.bukkit.AsyncKeepAlive.main.PlaceHolderMain;
import com.ishland.bukkit.AsyncKeepAlive.thread.AsyncPacketListener;
import com.ishland.bukkit.AsyncKeepAlive.thread.AsyncPacketListenerFor1_12toLatest;
import com.ishland.bukkit.AsyncKeepAlive.thread.AsyncPacketListenerFor1_8to1_11;
import com.ishland.bukkit.AsyncKeepAlive.thread.AsyncPacketThread;

public class LauncherForPacketListener {
    private AsyncPacketListener object;

    public void register(Plugin plugin, boolean debug, AsyncPacketThread packetThread, PlaceHolderMain placeHolder) {
	if (Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8")
		|| Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")
		|| Bukkit.getVersion().contains("1.11")) {
	    this.object = new AsyncPacketListenerFor1_8to1_11();
	} else if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.13")
		|| Bukkit.getVersion().contains("1.14")) {
	    this.object = new AsyncPacketListenerFor1_12toLatest();
	}
	object.register(plugin, debug, packetThread, placeHolder);
    }
}

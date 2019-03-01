/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.main;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

/**
 * @author ishland
 *
 */
public class AsyncKeepAlive extends JavaPlugin {
    private Runnable PacketThread;
    private FileConfiguration configuration;
    private boolean debug = false;

    private Metrics metrics;

    @Override
    public void onEnable() {
	setMetrics(new Metrics(this));
	getLogger().info("AsyncKeepAlive by ishland");
	loadConfig();
	if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
	    getLogger().warning("You need ProtocolLib in order to use this plugin");
	    getServer().getPluginManager().disablePlugin(this);
	    return;
	}
	if (!(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.12")
		|| Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.10")
		|| Bukkit.getVersion().contains("1.7.10")))
	    getLogger().warning("Minecraft " + Bukkit.getVersion() + " hasn't been tested yet!");
	try {
	    setPacketThread(new AsyncPacketThread());
	    ((AsyncPacketThread) getPacketThread()).setPlugin(this);
	    if (debug)
		((AsyncPacketThread) getPacketThread()).doDebug();
	    Thread thread = new Thread(getPacketThread());
	    thread.setName("AsyncKeepAlive-PacketThread");
	    thread.start();
	    ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
		    new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
			    if (e.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
				try {
				    PacketContainer keepAlivePacket = e.getPacket();
				    if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.12")) {
					StructureModifier<Long> packetData = keepAlivePacket.getLongs();
					Long packetValue = packetData.readSafely(0);
					if (debug)
					    getLogger().info("[Debug] Got keepalive " + String.valueOf(packetValue)
						    + " from " + e.getPlayer());
					if (packetValue == 0L) {
					    if (debug)
						getLogger().info(
							"[Debug] Got plugin-sent keepalive from " + e.getPlayer());
					    e.setCancelled(true);
					}
				    } else {
					StructureModifier<Integer> packetData = keepAlivePacket.getIntegers();
					int packetValue = packetData.readSafely(0);
					if (debug)
					    getLogger().info("[Debug] Got keepalive " + String.valueOf(packetValue)
						    + " from " + e.getPlayer());
					if (packetValue == 0L) {
					    if (debug)
						getLogger().info(
							"[Debug] Got plugin-sent keepalive from " + e.getPlayer());
					    e.setCancelled(true);
					}
				    }
				} catch (Throwable t) {
				    System.out.println("Caught a exception");
				    System.out.println(t.getMessage());
				    t.printStackTrace();
				}
			    }
			}
		    }).start();
	} catch (Throwable t) {
	    t.printStackTrace();
	    getServer().getPluginManager().disablePlugin(this);
	    return;
	}
	getLogger().info("AsyncKeepAlive 0.2.1-SNAPSHOT is now Enabled!");
    }

    @Override
    public void onDisable() {
	ProtocolLibrary.getProtocolManager().removePacketListeners(this);
	((AsyncPacketThread) getPacketThread()).doStop();
	getLogger().info("AsyncKeepAlive 0.2.1-SNAPSHOT is now Disabled!");
    }

    protected void loadConfig() {
	getLogger().info("Loading configurations...");
	this.saveDefaultConfig();
	setConfiguration(this.getConfig());
	getConfiguration().options().copyDefaults(true);
	if (getConfiguration().getBoolean("debug", false))
	    doDebug();
	getLogger().info("Configurations loaded!");
    }

    public Metrics getMetrics() {
	return metrics;
    }

    public void setMetrics(Metrics metrics) {
	this.metrics = metrics;
    }

    public Runnable getPacketThread() {
	return PacketThread;
    }

    public void setPacketThread(Runnable packetThread) {
	PacketThread = packetThread;
    }

    /**
     * @return the configuration
     */
    public FileConfiguration getConfiguration() {
	return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(FileConfiguration configuration) {
	this.configuration = configuration;
    }

    public void doDebug() {
	getLogger().info("Debug mode active.");
	debug = true;
    }

}

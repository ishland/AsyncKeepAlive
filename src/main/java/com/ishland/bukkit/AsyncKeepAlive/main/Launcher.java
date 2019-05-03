/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.main;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.ishland.bukkit.AsyncKeepAlive.launcher.LauncherForPacketListener;
import com.ishland.bukkit.AsyncKeepAlive.launcher.LauncherForPacketThread;

/**
 * @author ishland
 *
 */
public class Launcher extends JavaPlugin {
    private FileConfiguration configuration;
    private boolean debug = false;
    private long frequency = 4000;
    private PlaceHolderMain PlaceHolder;
    private Metrics metrics;
    private LauncherForPacketThread packetThread;
    private LauncherForPacketListener packetListener;

    @Override
    public void onEnable() {
	setMetrics(new Metrics(this));
	getLogger().info("AsyncKeepAlive by ishland");
	loadConfig();
	if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
	    setPlaceHolder(new PlaceHolderMain());
	    getPlaceHolder().register();
	}
	if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
	    getLogger().warning("You need ProtocolLib in order to use this plugin");
	    getServer().getPluginManager().disablePlugin(this);
	    return;
	}
	if (!(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.12")
		|| Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.10")
		|| Bukkit.getVersion().contains("1.7.10")))
	    getLogger().warning("Minecraft " + Bukkit.getVersion() + " hasn't been tested yet!");

	this.startSendingThread();
	this.startPacketListener();

	getLogger().info("AsyncKeepAlive 0.3-SNAPSHOT is now Enabled!");
    }

    protected void startSendingThread() {
	setPacketThread(new LauncherForPacketThread());
	getPacketThread().launch(this, debug);
    }

    protected void startPacketListener() {
	setPacketListener(new LauncherForPacketListener());
	getPacketListener().register(this, debug, packetThread.getObject(), PlaceHolder);
    }

    @Override
    public void onDisable() {
	ProtocolLibrary.getProtocolManager().removePacketListeners(this);
	getPacketThread().getObject().doStop();
	getLogger().info("AsyncKeepAlive 0.3-SNAPSHOT is now Disabled!");
    }

    protected void loadConfig() {
	getLogger().info("Loading configurations...");
	this.saveDefaultConfig();
	setConfiguration(this.getConfig());
	getConfiguration().options().copyDefaults(true);
	this.debug = getConfiguration().getBoolean("debug", false);
	this.frequency = getConfiguration().getLong("frequency");
	getLogger().info("Configurations loaded!");
    }

    public Metrics getMetrics() {
	return metrics;
    }

    public void setMetrics(Metrics metrics) {
	this.metrics = metrics;
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

    /**
     * @return the placeHolder
     */
    public PlaceHolderMain getPlaceHolder() {
	return PlaceHolder;
    }

    /**
     * @param placeHolder the placeHolder to set
     */
    public void setPlaceHolder(PlaceHolderMain placeHolder) {
	PlaceHolder = placeHolder;
    }

    /**
     * @return the packetThread
     */
    public LauncherForPacketThread getPacketThread() {
	return packetThread;
    }

    /**
     * @param packetThread the packetThread to set
     */
    public void setPacketThread(LauncherForPacketThread packetThread) {
	this.packetThread = packetThread;
    }

    /**
     * @return the packetListener
     */
    public LauncherForPacketListener getPacketListener() {
	return packetListener;
    }

    /**
     * @param packetListener the packetListener to set
     */
    public void setPacketListener(LauncherForPacketListener packetListener) {
	this.packetListener = packetListener;
    }

}

/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
	long startTime = System.currentTimeMillis();
	getLogger().info("AsyncKeepAlive by " + this.getDescription().getAuthors().toString());
	loadConfig();
	startMetric();
	if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
	    getLogger().info("Hooked into PlaceHolderAPI");
	    setPlaceHolder(new PlaceHolderMain());
	    getPlaceHolder().setOrigPlugin(this);
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

	getLogger().info("AsyncKeepAlive " + this.getDescription().getVersion() + " enabled in "
		+ String.valueOf(System.currentTimeMillis() - startTime) + "ms");
    }

    @SuppressWarnings("unchecked")
    protected void startMetric() {
	setMetrics(new Metrics(this));
	long freq = this.frequency;
	String result;
	if (freq % 1000 == 0 && freq >= 1000) {
	    result = String.valueOf(freq - 999) + " - " + String.valueOf(freq);
	} else {
	    result = String.valueOf((long) (freq / 1000) * 1000) + " - "
		    + String.valueOf((long) ((freq / 1000) + 1) * 1000);
	}
	getLogger().info("Frequency: " + String.valueOf(freq));
	getLogger().info("Frequency: " + result);
	getMetrics().addCustomChart(new Metrics.DrilldownPie("keepalive_frequency",
		(Callable<Map<String, Map<String, Integer>>>) this.metricFreq()));

    }

    // Java 7 support!
    protected Callable<?> metricFreq() {
	return new Callable<Object>() {
	    @Override
	    public Map<String, Map<String, Integer>> call() throws Exception {
		Map<String, Map<String, Integer>> map = new HashMap<>();
		Long freq = frequency;
		String result;
		Map<String, Integer> entry = new HashMap<>();
		entry.put(String.valueOf(freq), 1);
		if (freq.longValue() % 1000 == 0 && freq.longValue() >= 1000) {
		    result = String.valueOf(freq.longValue() - 999) + " - " + String.valueOf(freq);
		} else {
		    result = String.valueOf((long) (freq.longValue() / 1000) * 1000) + " - "
			    + String.valueOf((long) ((freq.longValue() / 1000) + 1) * 1000);
		}
		map.put(result, entry);
		return map;
	    }

	};
    }

    protected void startSendingThread() {
	setPacketThread(new LauncherForPacketThread());
	getPacketThread().launch(this, debug, frequency);
    }

    protected void startPacketListener() {
	setPacketListener(new LauncherForPacketListener());
	getPacketListener().register(this, debug, packetThread.getObject(), PlaceHolder);
    }

    @Override
    public void onDisable() {
	long startTime = System.currentTimeMillis();
	getLogger().info("Removing packet listener...");
	ProtocolLibrary.getProtocolManager().removePacketListeners(this);
	getLogger().info("Sending signal to packet thread...");
	getPacketThread().getObject().doStop();
	getLogger().info("Packet thread will stop in 1 second");
	getLogger().info("AsyncKeepAlive " + this.getDescription().getVersion() + " is disabled in "
		+ String.valueOf(System.currentTimeMillis() - startTime) + "ms");
    }

    protected void loadConfig() {
	getLogger().info("Loading configurations...");
	this.saveDefaultConfig();
	setConfiguration(this.getConfig());
	getConfiguration().options().copyDefaults(true);
	this.debug = getConfiguration().getBoolean("debug", false);
	this.frequency = getConfiguration().getLong("frequency");
	if (this.frequency < 1) {
	    getLogger().warning("Invaild frequency! Setting it to 1000");
	    this.frequency = 1000;
	}
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

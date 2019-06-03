/**
 * 
 */
package com.ishland.sponge.AsyncKeepAlive.main;

import java.io.IOException;
import java.nio.file.Path;

import org.bstats.sponge.Metrics2;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;
import com.ishland.sponge.AsyncKeepAlive.configuration.ConfigurationLoader;

/**
 * @author ishland
 *
 */
@Plugin(id = "asynckeepalive", name = "AsyncKeepAlive", version = "0.4-SNAPSHOT", description = "This is a plugin that reduce the probability of disconnects", dependencies = @Dependency(id = "utilities", optional = false))
public class Launcher {

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    @Inject
    private Metrics2 metrics;

    private byte state = 0;

    ConfigurationLoader configLoader;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) throws RuntimeException {
	if (state != 0)
	    throw new RuntimeException("onGamePreInitialization called wrongly");
	getLogger().info("Loading AsyncKeepAlive");
	getLogger().info("Loading configuration file...");
	this.configLoader = new ConfigurationLoader(this);
	try {
	    this.configLoader.load();
	} catch (IOException e) {
	    throw new RuntimeException("Error while loading configuration file");
	}
	this.configLoader.getConfig().getNode("");
	state = 1;
	getLogger().info("Loaded AsyncKeepAlive");
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) throws RuntimeException {
	if (state != 1)
	    throw new RuntimeException("onGameInitialization called wrongly");
	getLogger().info("Initializing AsyncKeepAlive");
    }

    /**
     * @return the logger
     */
    public Logger getLogger() {
	return logger;
    }

    /**
     * @return the privateConfigDir
     */
    public Path getPrivateConfigDir() {
	return privateConfigDir;
    }
}

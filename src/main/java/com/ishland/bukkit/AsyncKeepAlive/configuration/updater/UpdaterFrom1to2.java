/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.configuration.updater;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author ishland
 *
 */
public class UpdaterFrom1to2 {
    public static void update(FileConfiguration config) {
	config.set("version", 2);
	config.set("enableFreezeDetection", true);
    }
}

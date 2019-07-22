package com.ishland.bukkit.AsyncKeepAlive.updater;

import org.bukkit.plugin.Plugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

public class UpdaterThreadForReleases implements Runnable {
    private Plugin plugin;

    public UpdaterThreadForReleases(Plugin plugin) {
	this.plugin = plugin;
    }

    @Override
    public void run() {
	plugin.getLogger().info("Checking updates asynchronously...");
	SpigetUpdate updater = new SpigetUpdate(this.plugin, 64676);
	updater.setVersionComparator(VersionComparator.EQUAL);
	updater.checkForUpdate(new UpdateCallback() {
	    @Override
	    public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
		plugin.getLogger().info(
			"A newer version is Available! " + plugin.getDescription().getVersion() + " -> " + newVersion);
		if (hasDirectDownload && plugin.getConfig().getBoolean("updater.autodownload", true)) {
		    plugin.getLogger().info("Now trying to download the update asynchronously...");
		    if (updater.downloadUpdate()) {
			plugin.getLogger().info("Downloaded successfully! Restart the server to take effect");
		    } else {
			// Update failed
			plugin.getLogger().warning("Failed to download the update: " + updater.getFailReason());
		    }
		}
	    }

	    @Override
	    public void upToDate() {
		plugin.getLogger().info("Congratulation! Your plugin is up-to-date.");
	    }
	});
    }

}

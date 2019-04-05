/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.main;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

/**
 * @author ishland
 *
 */
public class PlaceHolderMain extends PlaceholderExpansion {

    private Runnable PacketThread;

    @Override
    public boolean canRegister() {
	return true;
    }

    @Override
    public String getAuthor() {
	return "ishland";
    }

    @Override
    public String getIdentifier() {
	return "AsyncKeepAlive";
    }

    @Override
    public String getVersion() {
	return "0.2.1-SNAPSHOT";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and
     * needs a value. <br>
     * We specify the value identifier in this method. <br>
     * Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player     A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param identifier A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

	if (identifier.equals("ping")) {
	    return "ping works";
	}

	// We return null if an invalid placeholder
	// was provided
	return null;
    }

    /**
     * @return the packetThread
     */
    public Runnable getPacketThread() {
	return PacketThread;
    }

    /**
     * @param packetThread the packetThread to set
     */
    public void setPacketThread(Runnable packetThread) {
	PacketThread = packetThread;
    }

}

/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.packet;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;

/**
 * @author ishland
 *
 */
public class KeepAlivePacket {
    protected Plugin plugin;
    protected byte state = -1;
    protected PacketContainer keepAlivePacket;
    protected ArrayList<Integer> checkedPlayers = new ArrayList<Integer>();

    protected long sendTime;

    protected void init(Plugin plugin) {
	this.sendTime = -1;
	this.keepAlivePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.KEEP_ALIVE);
	this.plugin = plugin;
	this.state = 0;
	this.checkedPlayers.clear();
    }

    @Override
    public void finalize() throws Throwable {
	super.finalize();
    }

    public void boardcast() throws IllegalAccessException {
	if (this.state == 0) {
	    try {
		ProtocolLibrary.getProtocolManager().broadcastServerPacket(getKeepAlivePacket());
	    } catch (FieldAccessException e) {
		getPlugin().getLogger().log(Level.WARNING, "Error while boardcasting packet", e);
	    }
	    this.sendTime = System.currentTimeMillis();
	    this.state = 1;
	} else {
	    throw new IllegalAccessException();
	}
    }

    public long getLatencyMillis() {
	if (this.state == 1)
	    return System.currentTimeMillis() - this.sendTime;

	return -1;
    }

    public long getPlayerPing(Player player) {
	if (this.state != 1)
	    return -1;
	if (checkedPlayers.contains(player.hashCode()))
	    return -1;
	checkedPlayers.add(player.hashCode());
	return getLatencyMillis();
    }

    /**
     * @return the keepAlivePacket
     */
    public PacketContainer getKeepAlivePacket() {
	return keepAlivePacket;
    }

    /**
     * @return the plugin
     */
    public Plugin getPlugin() {
	return plugin;
    }

    public long getBody() {
	return 0;
    }

}

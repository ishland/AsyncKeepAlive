/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.packet;

import org.bukkit.plugin.Plugin;

/**
 * @author ishland
 *
 */
public class KeepAlivePacketFor1_7to1_11 extends KeepAlivePacket {
    private int PacketBody;

    public KeepAlivePacketFor1_7to1_11(Plugin plugin) {
	init(plugin);
	this.PacketBody = (int) (1 + Math.random() * 1000000000);
	this.getKeepAlivePacket().getIntegers().write(0, this.PacketBody);
    }

    /**
     * @return the PacketBody
     */
    @Override
    public long getBody() {
	return PacketBody;
    }
}

package com.ishland.bukkit.AsyncKeepAlive.packet;

import org.bukkit.plugin.Plugin;

public class KeepAlivePacketFor1_12toLatest extends KeepAlivePacket {
    private long PacketBody;

    public KeepAlivePacketFor1_12toLatest(Plugin plugin) {
	init(plugin);
	this.PacketBody = (long) (1 + Math.random() * 1000000000);
	this.getKeepAlivePacket().getLongs().write(0, this.PacketBody);
    }

    /**
     * @return the PacketBody
     */
    @Override
    public long getBody() {
	return PacketBody;
    }
}

package com.ishland.sponge.AsyncKeepAlive.packet;

public class KeepAlivePacketFor1_12toLatest extends KeepAlivePacket {
    long packetBody;

    public KeepAlivePacketFor1_12toLatest() {
	init();
	this.packetBody = (long) (1 + Math.random() * 1000000000);
	this.packet.injector().add(0, packetBody).inject();
    }

    /**
     * @return the PacketBody
     */
    @Override
    public long getBody() {
	return packetBody;
    }

}

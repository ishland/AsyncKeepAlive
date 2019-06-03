package com.ishland.sponge.AsyncKeepAlive.packet;

public class KeepAlivePacketFor1_8to1_11 extends KeepAlivePacket {
    int packetBody;

    public KeepAlivePacketFor1_8to1_11() {
	init();
	this.packetBody = (int) (1 + Math.random() * 1000000000);
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

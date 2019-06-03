package com.ishland.sponge.AsyncKeepAlive.packet;

import org.spongepowered.api.Sponge;

import net.year4000.utilities.sponge.protocol.Packet;
import net.year4000.utilities.sponge.protocol.PacketTypes;
import net.year4000.utilities.sponge.protocol.Packets;

public class KeepAlivePacket {
    protected Packets packets;
    protected Packet packet;
    private byte state;

    protected long sendTime;

    protected void init() {
	this.sendTime = -1;
	this.packets = Sponge.getServiceManager().provide(Packets.class).orElseThrow(RuntimeException::new);
	this.packet = new Packet(PacketTypes.PLAY_CLIENT_KEEP_ALIVE);
	state = 0;
    }

    protected void boardcast() {
	packets.sendPacket(packet);
    }

    @Override
    public void finalize() throws Throwable {
	super.finalize();
    }

    public long getLatencyMillis() {
	if (this.state == 1)
	    return System.currentTimeMillis() - this.sendTime;

	return -1;
    }

    public long getBody() {
	return 0;
    }
}

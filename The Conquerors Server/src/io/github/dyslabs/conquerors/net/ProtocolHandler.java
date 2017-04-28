package io.github.dyslabs.conquerors.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import io.github.dyslabs.conquerors.Client;
import io.github.dyslabs.conquerors.Main;
import io.github.dyslabs.conquerors.PacketInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtocolHandler extends ChannelInboundHandlerAdapter {
	private final ProtocolServer server;
	private String ip;
	protected Client c;
	private final boolean register = false;

	public ProtocolHandler(final ProtocolServer s) {
		this.server = s;
	}

	/**
	 * Called when an connection is established
	 *
	 * @throws IOException
	 * @throws NoSuchFieldException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx)
			throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
			SecurityException, InvocationTargetException, InstantiationException, NoSuchFieldException {
		this.ip = ctx.channel().remoteAddress().toString();
		Main.out.info(this.ip + " connected");
		this.c = new Client(this.ip, new ByteArrayOutputStream());

	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg)
			throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
			SecurityException, InvocationTargetException, InstantiationException, NoSuchFieldException,
			ClassNotFoundException, URISyntaxException {
		final ByteBuf m = (ByteBuf) msg;
		final byte[] data = new byte[m.readableBytes()];
		m.readBytes(data);
		Main.out.info(data.length + " bytes from " + this.ip);
		final PacketInputStream pin = new PacketInputStream(new ByteArrayInputStream(data));
		while ((pin.available()) > 0) {
			this.c.poll(pin.readPacket());
		}
		final ArrayList<ByteBuffer> packets = this.server.packets.get(this.ip);
		final Iterator<ByteBuffer> iter = packets.iterator();
		while (iter.hasNext()) {
			final ByteBuffer b = iter.next();
			iter.remove();
			final byte[] raw_packet = b.array();
			final ByteBuf packet = ctx.alloc().buffer(raw_packet.length);
			packet.writeBytes(raw_packet);
			final byte[] packet_len = this.server.convertInt(raw_packet.length);
			final ByteBuf packet_len_buf = ctx.alloc().buffer(packet_len.length);
			packet_len_buf.writeBytes(packet_len);
			ctx.writeAndFlush(packet_len_buf);
			ctx.write(packet);
			Main.out.info(packet_len.length + " = message length");
			Main.out.info(raw_packet.length + " = packet length");
			Main.out.info((packet_len.length + raw_packet.length) + " bytes wrote");
		}
		ctx.flush();
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable t) {
		this.server.exception("An error occured while handling a network event", t, this);
		ctx.close();
	}
}

package io.github.dyslabs.conquerors.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import io.github.dyslabs.conquerors.Main;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ProtocolServer {
	protected HashMap<String, ArrayList<ByteBuffer>> packets = new HashMap<>();
	protected ArrayList<String> registeredIps = new ArrayList<>();

	public void addPacket(final String ip, final byte[] b) {
		this.addPacket(ip, ByteBuffer.wrap(b));
	}

	public void addPacket(final String ip, final ByteBuffer b) {
		if (this.packets.get(ip) == null) {
			this.packets.put(ip, new ArrayList<ByteBuffer>());
		}
		this.packets.get(ip).add(b);
	}

	public byte[] convertInt(final int i) {
		return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(i).array();
	}

	public void exception(final String msg, final Throwable b,ProtocolHandler h) {
		Main.out.log(Level.SEVERE, msg, b);
		h.c.valid(false);
	}

	public void run(final int port) throws InterruptedException {
		final EventLoopGroup bossGroup = new NioEventLoopGroup();
		final EventLoopGroup workerGroup = new NioEventLoopGroup();
		final ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(final SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new ProtocolDecoder(),new ProtocolHandler(ProtocolServer.this));
					}
				}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		b.bind(port).sync();
	}
}

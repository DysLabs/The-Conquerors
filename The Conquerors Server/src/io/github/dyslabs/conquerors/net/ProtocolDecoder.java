package io.github.dyslabs.conquerors.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import io.github.dyslabs.conquerors.Main;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
public class ProtocolDecoder extends ByteToMessageDecoder {
	private int packet_len=-1;
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Main.out.info("Waiting for 4 bytes ("+in.readableBytes()+" are readable)");
		if (in.readableBytes()<4 && packet_len==-1) return;
		
		byte[] data_len=new byte[4];
		in.readBytes(data_len);
		packet_len=ByteBuffer.wrap(data_len).order(ByteOrder.BIG_ENDIAN).getInt();
		Main.out.info("Waiting for "+packet_len+" bytes ("+in.readableBytes()+" are readable)");
		
		if (in.readableBytes()<packet_len) return;
		out.add(in.readBytes(packet_len));
	}

}

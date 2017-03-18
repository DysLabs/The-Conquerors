package org.genius.conquerors.server;

import p.Packet;

import java.io.IOException;

import p.*;

public class PacketHandler {
	private final int pid;
	private final Packet p;
	private final ClientHandler h;
	public PacketHandler(Packet p,ClientHandler h) throws IOException {
		this.h=h;
		this.p=p;
		pid=Integer.parseInt(p.getClass().getName().replaceAll("p.Packet", ""));
		switch (pid) {
		case 0:
			handle0();
			break;
		}
	}
	
	private void handle0() throws IOException {
		Packet0 packet=(Packet0)p;
		if (packet.getProtocolVersion()!=Main.VERSION) {
			Packet2 loginFailure=new Packet2(h.getIn(),h.getOut());
			loginFailure.setReason("You're running a different protocol version. We have protocol-"+Main.VERSION+", you have "+packet.getProtocolVersion());
			h.sendPacket(loginFailure);
		} else {
			if (Main.playerCount>=16) {
				Packet2 loginFailure=new Packet2(h.getIn(),h.getOut());
				loginFailure.setReason("Server is full, sorry");
				h.sendPacket(loginFailure);
			} else {
				Packet1 loginSuccess=new Packet1(h.getIn(),h.getOut());
				Main.playerCount++;
				String[] players=new String[Main.playerCount];
				for (int i=0;i<Main.playerCount-1;i++) {
					players[i]=Main.playerList[i];
				}
				players[Main.playerCount-1]=packet.getName();
				Main.playerList=players;
				loginSuccess.setPlayer(Main.playerList);
				loginSuccess.setPlayerListLength(Main.playerCount);
				h.sendPacket(loginSuccess);
				h.username=packet.getName();
				h.spatialId=Main.getSpatialID("player("+packet.getName()+")");
				Packet13 playerList=new Packet13(h.getIn(),h.getOut());
				playerList.setList(Main.playerList);
				playerList.setListCount(Main.playerCount);
				Main.broadcastPacket(playerList);
			}
		}
	}
}

package edu.unomaha.comm.net.vnk.semester.project;

import java.io.IOException;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ExampleSimple {

	final private Peer peer;

	public ExampleSimple(int peerId) throws Exception {
		peer = new PeerMaker(Number160.createHash(peerId)).setPorts(4000 + peerId).makeAndListen();
		FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(4001).start();
		fb.awaitUninterruptibly();
		if (fb.getBootstrapTo() != null) {
			peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}
	}

	public static void main(String[] args) throws NumberFormatException, Exception {
//		args = new String[] { "1", "test.me", "192.168.1.1" };
		args = new String[] { "2", "test.me" };
		ExampleSimple dns = new ExampleSimple(Integer.parseInt(args[0]));
		if (args.length == 3) {
			dns.store(args[1]);
		}
		if (args.length == 2) {
			System.out.println("Name:" + args[1] + " Message:" + dns.get(args[1]));
		}
	}

	private Message get(String name) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return ((Message) futureDHT.getData().getObject());
		}
		return null;
	}

	private void store(String name) throws IOException {
		peer.put(Number160.createHash(name)).setData(new Data(new Message("Hello World!"))).start().awaitUninterruptibly();
	}

}

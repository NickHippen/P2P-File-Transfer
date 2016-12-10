package edu.unomaha.comm.net.vnk.semester.project;

import java.io.IOException;
import java.util.Scanner;

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
//		args = new String[] { "1", "test.me", "Hello from peer 1" };
//		args = new String[] { "2", "test.me" };
//		args = new String[] { "3", "test.me" };
//		args = new String[] { "3", "test.me.2", "Hello from peer 3" };
//		args = new String[] { "4", "test.me.2" };
//		args = new String[] { "5", "test.me.2" };
//		args = new String[] { "6", "test.me" };
		args = new String[] { "7", "test.me.2" };
		System.out.println("Peer ID: " + args[0]);
		ExampleSimple dns = new ExampleSimple(Integer.parseInt(args[0]));
		if (args.length == 3) {
			dns.store(args[1], args[2]);
			dns.peer.broadcast(Number160.createHash(args[1]));
		}
		if (args.length == 2) {
			Message message = dns.get(args[1]);
			System.out.println("Name: " + args[1] + "\nMessage: " + message);
			dns.store(args[1], message.getMessage());
		}
		System.out.println("Type anything to leave the network.");
		new Scanner(System.in).nextLine();
		dns.peer.shutdown();
	}

	private Message get(String name) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return ((Message) futureDHT.getData().getObject());
		}
		System.out.println("Unable to get key!");
		return null;
	}

	private void store(String name, String message) throws IOException {
		peer.put(Number160.createHash(name)).setData(new Data(new Message(message))).start().awaitUninterruptibly();
	}

}

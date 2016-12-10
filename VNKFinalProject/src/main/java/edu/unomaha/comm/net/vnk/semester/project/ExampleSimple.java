package edu.unomaha.comm.net.vnk.semester.project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
//		args = new String[] { "1", "file-key", "super-secret-file.txt" };
		args = new String[] { "2", "file-key" };
		System.out.println("Peer ID: " + args[0]);
		ExampleSimple dns = new ExampleSimple(Integer.parseInt(args[0]));
		if (args.length == 3) {
			dns.store(args[1], args[2]);
			dns.peer.broadcast(Number160.createHash(args[1]));
		}
		if (args.length == 2) {
			System.out.println(dns.get(args[1]));
//			System.out.println("Name: " + args[1] + "\nMessage: " + message);
		}
		System.out.println("Type anything to leave the network.");
		new Scanner(System.in).nextLine();
		dns.peer.shutdown();
	}

	private boolean get(String name) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			FileOutputStream fos = new FileOutputStream("transferred-file.txt");
			System.out.println("Received: " + Arrays.toString(futureDHT.getData().getData()));
			fos.write(futureDHT.getData().getData());
			fos.close();
			return true;
		}
		System.out.println("Unable to get key!");
		return false;
	}

	private void store(String name, String fileName) throws IOException {
		Path path = Paths.get(fileName);
		byte[] data = Files.readAllBytes(path);
		System.out.println("Putting: " + Arrays.toString(data));
		peer.put(Number160.createHash(name)).setData(new Data(data)).start().awaitUninterruptibly();
	}

}

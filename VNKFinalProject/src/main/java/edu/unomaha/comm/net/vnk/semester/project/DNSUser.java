package edu.unomaha.comm.net.vnk.semester.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class DNSUser {

	private static final Random RANDOM = new Random();
	
	private final Peer peer;
	
	public DNSUser(int port, int connectionPort) throws IOException {
		peer = new PeerMaker(new Number160(RANDOM)).setPorts(port).makeAndListen();
		FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(connectionPort).start();
		fb.awaitUninterruptibly();
		if (fb.getBootstrapTo() != null) {
			peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}
	}
	
	public boolean get(String fileName) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(Number160.createHash(fileName)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			FileWrapper fileWrapper = (FileWrapper) futureDHT.getData().getObject();
			File file = new File("downloads");
			if (!file.exists()) {
				file.mkdir();
			}
			FileOutputStream fos = new FileOutputStream("downloads/" + fileWrapper.getFileName());
			fos.write(fileWrapper.getBytes());
			fos.close();
			store(fileName, "downloads/" + fileWrapper.getFileName());
			return true;
		}
		System.out.println("Unable to get key!");
		return false;
	}

	public void store(String keyName, String filePath) throws IOException {
		FileWrapper fileWrapper = Utils.wrapFile(filePath);
		peer.put(Number160.createHash(keyName)).setData(new Data(fileWrapper)).start().awaitUninterruptibly();
	}
	
	public void shutdown() {
		peer.shutdown();
	}
	
}

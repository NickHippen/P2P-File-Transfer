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

/**
 * A peer wrapper class that sets up a user as a peer and provides functionality for putting and getting files from the DHT
 * @author Nick Hippen
 */
public class DNSUser {

	private static final Random RANDOM = new Random();
	
	/**
	 * The TomP2P Peer object that contains basic network functionality
	 */
	private final Peer peer;

	/**
	 * Constructs a new DNSUser under the port, and connects to the P2P network through the connectionPort
	 * @param port the port to open connections on
	 * @param connectionPort the port to connect to the P2P network through
	 * @throws IOException
	 */
	public DNSUser(int port, int connectionPort) throws IOException {
		peer = new PeerMaker(new Number160(RANDOM)).setPorts(port).makeAndListen();
		// Prepare bootstrapping into the network
		FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(connectionPort).start();
		// Wait for a response from the connection to bootstrap in
		fb.awaitUninterruptibly();
		if (fb.getBootstrapTo() != null) {
			peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}
	}
	
	/**
	 * Gets a file under a specific key
	 * @param keyName the key name to get the file from
	 * @return whether or not the get attempt was successful
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean get(String keyName) throws ClassNotFoundException, IOException {
		// Start a future to get the file under the hash
		FutureDHT futureDHT = peer.get(Number160.createHash(keyName)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			// Retrieve file as a FileWrapper
			FileWrapper fileWrapper = (FileWrapper) futureDHT.getData().getObject();
			File file = new File("downloads");
			if (!file.exists()) {
				file.mkdir();
			}
			// Place file in downloads folder
			FileOutputStream fos = new FileOutputStream("downloads/" + fileWrapper.getFileName());
			fos.write(fileWrapper.getBytes());
			fos.close();
			// Mark peer as a valid seeder of the file
			store(keyName, "downloads/" + fileWrapper.getFileName());
			return true;
		}
		System.out.println("Unable to get key!");
		return false;
	}

	/**
	 * Puts the file at the file path into the DHT under the key name
	 * @param keyName the key name to store the file
	 * @param filePath the path to the file
	 * @throws IOException
	 */
	public void store(String keyName, String filePath) throws IOException {
		FileWrapper fileWrapper = Utils.wrapFile(filePath);
		peer.put(Number160.createHash(keyName)).setData(new Data(fileWrapper)).start().awaitUninterruptibly();
	}
	
	/**
	 * Calls the shutdown method on the peer, handled by TomP2P, closing all connections of this node
	 */
	public void shutdown() {
		peer.shutdown();
	}
	
}

package server.model;

import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.*;

/**
 * ClientConnection is a thread managing the connection between the server and a
 * client.
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
public class ClientConnection extends Thread {

	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private DictionaryServer dict;
	private boolean listening;

	/**
	 * ClientConnection is a thread managing the connection between the server and a
	 * client.
	 * 
	 * @param s    the socket corresponding to the connection with the client
	 * @param ois  the input stream to use
	 * @param oos  the output stream to use
	 * @param dict the server
	 */
	public ClientConnection(Socket s, ObjectInputStream ois, ObjectOutputStream oos, DictionaryServer dict) {
		this.s = s;
		this.ois = ois;
		this.oos = oos;
		this.dict = dict;
	}

	@Override
	public void run() {
		super.run();

		listening = true;

		while (listening) {

			try {

				JSONObject message = (JSONObject) ois.readObject();

				if (message.get("TYPE").equals("QUIT")) {
					ois.close();
					oos.close();
					s.close();
					listening = false;
					dict.closeClient(this);
				}

				else if (message.get("TYPE").equals("SEARCH")) {
					JSONArray definitions = this.dict.search((String) message.get("WORD"));
					oos.writeObject(definitions);
				}

				else if (message.get("TYPE").equals("ADD")) {

					String result = this.dict.addWord(message);

					this.oos.writeObject(result);
				}

				else if (message.get("TYPE").equals("REMOVE")) {

					String result = this.dict.removeWord((String) message.get("WORD"));

					this.oos.writeObject(result);
				}

			} catch (IOException e) {
				if (listening) {
					System.out.println("Error: A client suddenly terminated the connection.");
				}
				listening = false;
				dict.closeClient(this);
			} catch (ClassNotFoundException e) {
				System.out.println("Error: Something went wrong when receiving data from a client.");
			}
		}

	}

	/**
	 * Close all the streams and the socket, notify the server that the client has
	 * exited, and finally end the thread.
	 * 
	 */
	public void stopConnection() {

		try {
			ois.close();
			oos.close();
			s.close();
		} catch (IOException e) {
			System.out.println("Error: A client suddenly terminated the connection.");
			e.printStackTrace();
		}
		listening = false;
		dict.closeClient(this);

	}

}

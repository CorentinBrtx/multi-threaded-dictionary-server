package client.model;

import java.io.*;
import java.net.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import client.view.WindowClient;

/**
 * DictionaryClient manages the communication with the server. When started, it
 * establishes a TCP connection with the server.
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
public class DictionaryClient {

	private Socket s1;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	/**
	 * DictionaryClient manages the communication with the server. When started, it
	 * establishes a TCP connection with the server.
	 * <p>
	 * During the execution, it sends messages containing requests, and receive the
	 * result of the operation through another message.
	 * 
	 * @param address a String indicating the address of the server
	 * @param port    the port used by the server
	 */
	public DictionaryClient(String address, int port) {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				closeConnection();
			}
		});

		try {

			this.s1 = new Socket(address, port);
			this.oos = new ObjectOutputStream(s1.getOutputStream());
			this.ois = new ObjectInputStream(s1.getInputStream());

		} catch (UnknownHostException e) {
			System.out.println("Error: The address you provided is incorrect.");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Error: The server is not responding (the port you entered might be incorrect).");
			System.exit(2);
		}

		new WindowClient(this);
	}

	public static void main(String[] args) {

		new DictionaryClient(args[0], Integer.parseInt(args[1]));
	}

	/**
	 * Sends a message to the server requesting the definitions of the word chosen,
	 * and returns the JSONArray it gets from the server (can be the definitions, or
	 * a message indicating the word is not in the dictionary)
	 * 
	 * @param word the word for which we want the definitions
	 * @return a JSONArray containing either the definitions or an error message
	 */
	@SuppressWarnings("unchecked")
	public JSONArray submitSearch(String word) {

		try {

			JSONObject message = new JSONObject();
			message.put("TYPE", "SEARCH");

			message.put("WORD", word);

			this.oos.writeObject(message);

			JSONArray data = (JSONArray) this.ois.readObject();
			return data;

		} catch (IOException e) {
			System.out.println("Error: The server is not responding anymore.");
			System.exit(2);
		} catch (ClassNotFoundException e) {
			System.out.println("Error: Something went wrong with the server.");
			return null;
		}

		return null;

	}

	/**
	 * Sends a message to the server to add a word an its definitions to the
	 * dictionary. It returns a String with a message indicating either a success or
	 * a failure.
	 * 
	 * @param word        the word related to the definitions
	 * @param definitions the definitions of the word
	 * @return a message indicating if the operation succeeded or not
	 */
	@SuppressWarnings("unchecked")
	public String addWord(String word, JSONArray definitions) {

		try {

			JSONObject message = new JSONObject();
			message.put("TYPE", "ADD");

			message.put("WORD", word);

			message.put("DEFINITIONS", definitions);

			this.oos.writeObject(message);

			String result = (String) this.ois.readObject();
			return result;

		} catch (IOException e) {
			System.out.println("Error: The server is not responding anymore.");
			System.exit(2);
		} catch (ClassNotFoundException e) {
			System.out.println("Error: Something went wrong with the server.");
			return "Error: Something went wrong with the server.";
		}

		return "Error: Something went wrong";

	}

	/**
	 * Sends a message to the server to delete a word from the dictionary, and
	 * returns a String containing the result of the operation
	 * 
	 * @param word the word to delete from the dictionary
	 * @return a message indicating if the operation succeeded or not
	 */
	@SuppressWarnings("unchecked")
	public String removeWord(String word) {

		try {

			JSONObject message = new JSONObject();
			message.put("TYPE", "REMOVE");
			message.put("WORD", word);

			this.oos.writeObject(message);

			String result = (String) this.ois.readObject();
			return result;

		} catch (IOException e) {
			System.out.println("Error: The server is not responding anymore.");
			System.exit(2);
		} catch (ClassNotFoundException e) {
			System.out.println("Error: Something went wrong with the server.");
			return "Error: Something went wrong with the server.";
		}

		return null;

	}

	/**
	 * Sends a message to the server indicating the end of the connection, and close
	 * the streams and the socket.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void closeConnection() {

		try {

			JSONObject message = new JSONObject();
			message.put("TYPE", "QUIT");

			this.oos.writeObject(message);

			this.ois.close();
			this.oos.close();
			this.s1.close();

		} catch (IOException e) {
			return;
		} catch (NullPointerException e) {
			return;
		}
	}

}

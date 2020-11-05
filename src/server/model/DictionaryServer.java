package server.model;

import java.net.*;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import server.controller.ControllerServer;
import server.view.WindowServer;

import java.io.*;

/**
 * DictionaryServer reads the data from a file, and start a server at the
 * specified port. It waits for new connections while running, and starts a new
 * thread whenever there is one.
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
public class DictionaryServer {

	private int port;
	private JSONObject dictionary;
	private String filename;
	private ServerSocket s;
	private ControllerServer controller;

	/**
	 * DictionaryServer reads the data from a file, and start a server at the
	 * specified port. It waits for new connections while running, and starts a new
	 * thread whenever there is one.
	 * 
	 * @param port     the port specified for the server
	 * @param filename the file from which to read the data
	 */
	public DictionaryServer(int port, String filename) {

		this.port = port;
		this.filename = filename;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				closeServer();
			}
		});

		JSONParser jsonParser = new JSONParser();

		try {

			FileReader reader = new FileReader(filename);

			Object obj = jsonParser.parse(reader);

			this.dictionary = (JSONObject) obj;

		} catch (FileNotFoundException e) {
			System.out.println("Error: This file doesn't exist.");
			System.exit(1);
		} catch (ParseException | IOException e) {
			System.out.println("Error: Something went wrong with the lecture of the file.");
			this.dictionary = new JSONObject();
		}

		WindowServer window = new WindowServer(this);
		this.controller = window.getControllerServer();
	}

	/**
	 * Closes the server. It closes the server socket, and writes the new data into
	 * the same file from where it read it at the start.
	 * 
	 */
	protected void closeServer() {

		try {

			this.s.close();

		} catch (IOException | NullPointerException e1) {
		}

		try {

			if (this.dictionary != null) {

				FileWriter file = new FileWriter(this.filename);

				file.write(this.dictionary.toJSONString());
				file.flush();
				file.close();
			}

		} catch (IOException | NoClassDefFoundError e) {
			System.out.println("Error: Something went wrong when writing the data on the disk.");
		}

	}

	/**
	 * Starts the server socket and creates a new {@link ClientConnection} whenever
	 * a new client establishes a connection.
	 * 
	 */
	private void startServer() {

		try {

			this.s = new ServerSocket(this.port);

			while (true) {

				Socket s1 = null;

				s1 = s.accept();

				ObjectInputStream ois = new ObjectInputStream(s1.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());

				ClientConnection newClient = new ClientConnection(s1, ois, oos, this);
				newClient.start();

				this.controller.displayNewClient(s1, newClient, new Date());

			}

		} catch (SocketException e) {
		}

		catch (IOException e) {
			System.out.println("Error: Something went wrong when starting the server.");
			System.exit(1);
		}

	}

	public static void main(String[] args) {

		DictionaryServer server = new DictionaryServer(Integer.parseInt(args[0]), args[1]);
		server.startServer();

	}

	/**
	 * Returns the definitions of a word if this one is in the dictionary.
	 * 
	 * @param word the word to search in the dictionary
	 * @return a {@link JSONArray} containing the definitions, or an error message
	 *         if the word is not in the dictionary
	 */
	@SuppressWarnings("unchecked")
	public synchronized JSONArray search(String word) {

		word = word.trim().substring(0, 1).toUpperCase() + word.trim().substring(1);

		if (this.dictionary.containsKey(word)) {
			return (JSONArray) this.dictionary.get(word);
		} else {
			JSONArray result = new JSONArray();
			result.add("NOT_FOUND");
			return result;
		}

	}

	/**
	 * Adds a word and its definitions to the dictionary if it is not already in it.
	 * It returns a string indicating the result of the operation
	 * 
	 * @param data a JSONObject with a field "WORD" containing the world as a
	 *             String, and a field "DEFINITIONS" containing a JSONArray with the
	 *             definitions
	 * @return a message indicating if the operation succeeded or not
	 */
	@SuppressWarnings("unchecked")
	public synchronized String addWord(JSONObject data) {

		String word = (String) data.get("WORD");
		word = word.trim().substring(0, 1).toUpperCase() + word.trim().substring(1);
		JSONArray definitions = (JSONArray) data.get("DEFINITIONS");

		if (this.dictionary.containsKey(word)) {
			return "Error: the word is already in the dictionary";
		} else {
			JSONArray defs = new JSONArray();
			definitions.forEach(definition -> {
				defs.add(((String) definition).trim().substring(0, 1).toUpperCase()
						+ ((String) definition).trim().substring(1));
			});
			this.dictionary.put(word, defs);
			this.controller.updateWindow();
			return "The word has been successfully added";
		}

	}

	/**
	 * Removes a word from the dictionary and returns a String indicating the result
	 * of the operation
	 * 
	 * @param word the word to remove from the dictionary
	 * @return a message indicating if the operation succeeded or not
	 */
	public synchronized String removeWord(String word) {

		word = word.trim().substring(0, 1).toUpperCase() + word.trim().substring(1);

		if (this.dictionary.containsKey(word)) {
			this.dictionary.remove(word);
			this.controller.updateWindow();
			return "The word has been successfully removed";
		} else {
			return "Error: this word does not exist in the dictionary";
		}
	}

	public JSONObject getDictionary() {
		return dictionary;
	}

	/**
	 * Notify the GUI that a client closed the connection
	 * 
	 * @param client the client who closed the connection
	 */
	public void closeClient(ClientConnection client) {
		this.controller.closeClient(client);

	}

}

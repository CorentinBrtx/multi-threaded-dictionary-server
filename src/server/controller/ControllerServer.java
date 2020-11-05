package server.controller;

import java.net.Socket;
import java.util.Date;

import org.json.simple.JSONObject;

import server.model.ClientConnection;
import server.model.DictionaryServer;
import server.view.WindowServer;

/**
 * ControllerServer is the link between the classes of the view module and the
 * ones in the model module
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
public class ControllerServer {

	private DictionaryServer server;
	private WindowServer window;

	/**
	 * ControllerServer is the link between the classes of the view module and the
	 * ones in the model module
	 * 
	 * @param server the DictionaryServer instance to link to the view module
	 * @param window the WindowServer instance to link to the model module
	 */
	public ControllerServer(DictionaryServer server, WindowServer window) {

		this.server = server;
		this.window = window;

	}

	public JSONObject getDictionary() {
		return server.getDictionary();
	}

	public void updateWindow() {
		this.window.updateDictionary();
	}

	public void removeWord(String word) {
		this.server.removeWord(word);
	}

	public void editWord(String oldWord, JSONObject newData) {
		this.server.removeWord(oldWord);
		this.server.addWord(newData);

	}

	public void displayNewClient(Socket s1, ClientConnection newClient, Date creationDate) {
		this.window.displayNewClient(s1, newClient, creationDate);
	}

	public void closeClient(ClientConnection client) {
		this.window.closeClient(client);

	}

}

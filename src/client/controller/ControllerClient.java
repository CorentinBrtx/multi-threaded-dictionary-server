package client.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import client.model.DictionaryClient;
import client.view.WindowClient;

/**
 * ControllerClient is the link between the classes of the view module and the
 * ones in the model module
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
public class ControllerClient {

	private DictionaryClient client;
	@SuppressWarnings("unused")
	private WindowClient window;

	/**
	 * ControllerClient is the link between the classes of the view module and the
	 * ones in the model module
	 * 
	 * @param client       the DictionaryClient instance to link to the view module
	 * @param windowClient the WindowClient instance to link to the model module
	 */
	public ControllerClient(DictionaryClient client, WindowClient windowClient) {
		this.client = client;
		this.window = windowClient;
	}

	public JSONArray searchWord(String word) {
		return client.submitSearch(word);
	}

	public void closeClient() {
		client.closeConnection();
	}

	public String addWord(String word, JSONArray definitions) {
		return client.addWord(word, definitions);
	}

	public String removeWord(String word) {
		return client.removeWord(word);
	}

	public String editWord(String oldWord, JSONObject newData) {

		if (oldWord.equals((String) newData.get("WORD"))) {

			String resultRemove = client.removeWord(oldWord);
			if (resultRemove.contains("Error")) {
				return resultRemove;
			}
			return client.addWord((String) newData.get("WORD"), (JSONArray) newData.get("DEFINITIONS"));

		} else {
			String resultAdd = client.addWord((String) newData.get("WORD"), (JSONArray) newData.get("DEFINITIONS"));
			if (resultAdd.contains("Error")) {
				return resultAdd;
			}
			return client.removeWord(oldWord);
		}
	}

}

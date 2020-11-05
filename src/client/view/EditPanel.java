package client.view;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import client.controller.ControllerClient;

/**
 * EditPanel is in charge of the tab offering the possibility to modify an entry
 * of the dictionary
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
@SuppressWarnings("serial")
public class EditPanel extends JPanel {

	private JTextField searchBox;
	private ControllerClient controller;
	private JLabel result;
	private JPanel panel;

	/**
	 * EditPanel is in charge of the tab offering the possibility to modify an entry
	 * of the dictionary
	 * 
	 * @param controllerClient the controller to use to communicate with the model
	 *                         classes
	 */
	public EditPanel(ControllerClient controllerClient) {

		this.controller = controllerClient;
		this.panel = this;

		this.setLayout(new GridBagLayout());

		enterSearchMode("");

	}

	/**
	 * Displays the components to allow the user to search the word he wants to
	 * modify
	 * 
	 * @param resultContent a message to display to the user (can be empty)
	 */
	private void enterSearchMode(String resultContent) {

		this.removeAll();

		JLabel welcomeLabel = new JLabel("Search the word you want to modify.");
		welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
		GridBagConstraints welcomeC = new GridBagConstraints();
		welcomeC.anchor = GridBagConstraints.CENTER;
		welcomeC.gridwidth = 2;
		welcomeC.weighty = 5;
		welcomeC.gridy = 0;
		this.add(welcomeLabel, welcomeC);

		Action submit = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitSearch(searchBox.getText());
			}
		};

		searchBox = new JTextField();
		searchBox.addActionListener(submit);
		GridBagConstraints sboxC = new GridBagConstraints();
		sboxC.fill = GridBagConstraints.HORIZONTAL;
		sboxC.weightx = 4;
		sboxC.weighty = 1;
		sboxC.gridx = 0;
		sboxC.gridy = 1;
		sboxC.insets = new Insets(0, 20, 0, 10);
		this.add(searchBox, sboxC);

		JButton submitButton = new JButton("Search");
		submitButton.addActionListener(submit);
		GridBagConstraints submitC = new GridBagConstraints();
		submitC.weightx = 0;
		submitC.gridx = 1;
		submitC.gridy = 1;
		submitC.anchor = GridBagConstraints.EAST;
		submitC.insets = new Insets(0, 0, 0, 30);
		this.add(submitButton, submitC);

		result = new JLabel(resultContent);
		result.setFont(new Font("", Font.ITALIC, 14));
		GridBagConstraints resultC = new GridBagConstraints();
		resultC.fill = GridBagConstraints.BOTH;
		resultC.weighty = 5;
		resultC.gridy = 8;
		resultC.gridwidth = 3;
		resultC.anchor = GridBagConstraints.WEST;
		resultC.insets = new Insets(0, 40, 0, 0);
		this.add(result, resultC);

		this.repaint();

	}

	/**
	 * Displays the components to allow the user to modify a word and its
	 * definitions
	 * 
	 * @param oldWord     the word the modify
	 * @param definitions the definitions of the specified word
	 */
	@SuppressWarnings("unchecked")
	public void enterEditMode(String oldWord, JSONArray definitions) {

		this.removeAll();

		Action submit = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSONObject newData = new JSONObject();

				String newWord = ((JTextField) panel.getComponent(2)).getText();

				if (!validWord(newWord)) {
					result.setText("Error: The word you entered is not valid");
					return;
				}

				newData.put("WORD", newWord);

				JSONArray newDefinitions = new JSONArray();
				ArrayList<Component> defComponents = new ArrayList<Component>(Arrays.asList(panel.getComponents()));

				for (Component c : defComponents.subList(4, defComponents.size() - 3)) {
					String def = ((JTextField) c).getText();
					if (!validDefinition(def)) {
						result.setText("Error: One of your definitions contains unvalid characters");
						return;
					} else if (!def.equals("")) {
						newDefinitions.add(def);
					}
				}

				if (newDefinitions.size() == 0) {
					result.setText("Error: You must enter at least one valid definition");
					return;
				}
				newData.put("DEFINITIONS", newDefinitions);

				String resultEdit = controller.editWord(oldWord, newData);
				if (resultEdit.equals("Error: this word does not exist in the dictionary")) {
					enterSearchMode("The entry has been successfully edited.");
				} else if (resultEdit.contains("Error")) {
					result.setText(resultEdit);
					return;
				}
				enterSearchMode("The entry has been successfully edited.");
			}
		};

		JLabel welcomeLabel = new JLabel("Modify the word or the definitions.");
		welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
		GridBagConstraints welcomeC = new GridBagConstraints();
		welcomeC.anchor = GridBagConstraints.CENTER;
		welcomeC.gridwidth = 3;
		welcomeC.weighty = 5;
		welcomeC.gridy = 0;
		this.add(welcomeLabel, welcomeC);

		JLabel wordLabel = new JLabel("Word :");
		GridBagConstraints wordC = new GridBagConstraints();
		wordC.weightx = 0;
		wordC.weighty = 1;
		wordC.gridx = 0;
		wordC.gridy = 1;
		wordC.anchor = GridBagConstraints.WEST;
		wordC.insets = new Insets(0, 10, 0, 20);
		this.add(wordLabel, wordC);

		JTextField wordBox = new JTextField(oldWord);
		wordBox.addActionListener(submit);
		GridBagConstraints wboxC = new GridBagConstraints();
		wboxC.fill = GridBagConstraints.HORIZONTAL;
		wboxC.weightx = 7;
		wboxC.weighty = 1;
		wboxC.gridwidth = 2;
		wboxC.gridx = 1;
		wboxC.gridy = 1;
		wboxC.insets = new Insets(0, 0, 0, 20);
		this.add(wordBox, wboxC);

		JLabel definitionsLabel = new JLabel("Definitions :");
		GridBagConstraints defLabelC = new GridBagConstraints();
		defLabelC.weightx = 0;
		defLabelC.weighty = 1;
		defLabelC.gridx = 0;
		defLabelC.gridy = 2;
		defLabelC.gridwidth = 2;
		defLabelC.anchor = GridBagConstraints.WEST;
		defLabelC.insets = new Insets(0, 10, 0, 20);
		this.add(definitionsLabel, defLabelC);

		definitions.forEach(definition -> {
			JTextField defBox = new JTextField((String) definition);
			defBox.addActionListener(submit);
			GridBagConstraints defC = new GridBagConstraints();
			defC.anchor = GridBagConstraints.WEST;
			defC.fill = GridBagConstraints.HORIZONTAL;
			defC.gridx = 2;
			defC.weightx = 4;
			defC.weighty = 1;
			defC.insets = new Insets(0, 0, 0, 20);
			this.add(defBox, defC);
		});

		JButton submitButton = new JButton("Submit");
		GridBagConstraints submitC = new GridBagConstraints();
		submitC.anchor = GridBagConstraints.CENTER;
		submitC.gridy = 7;
		submitC.weighty = 1;
		submitC.gridwidth = 3;
		submitButton.addActionListener(submit);
		this.add(submitButton, submitC);

		JButton cancelButton = new JButton("Cancel");
		GridBagConstraints cancelC = new GridBagConstraints();
		cancelC.anchor = GridBagConstraints.CENTER;
		cancelC.gridy = 8;
		cancelC.weighty = 1;
		cancelC.gridwidth = 3;
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enterSearchMode("");

			}

		});
		this.add(cancelButton, cancelC);

		result = new JLabel("");
		result.setFont(new Font("", Font.ITALIC, 14));
		GridBagConstraints resultC = new GridBagConstraints();
		resultC.fill = GridBagConstraints.BOTH;
		resultC.weighty = 5;
		resultC.gridy = 9;
		resultC.gridwidth = 3;
		resultC.gridheight = GridBagConstraints.REMAINDER;
		resultC.anchor = GridBagConstraints.WEST;
		resultC.insets = new Insets(0, 40, 0, 0);
		this.add(result, resultC);

	}

	/**
	 * Checks if a definition is valid (no special characters)
	 * 
	 * @param definition a String containing the definition
	 * @return true if the definition is valid, false otherwise
	 */
	private boolean validDefinition(String definition) {
		Pattern p = Pattern.compile("[@#$%&*_+=|<>{}\\[\\]~изг{}]");
		Matcher m = p.matcher(definition);
		return !m.find();
	}

	/**
	 * Checks if a word is valid (no special characters)
	 * 
	 * @param word a String containing the word
	 * @return true if the word is valid, false otherwise
	 */
	private boolean validWord(String word) {
		Pattern p = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~,;:/з.г/{}и\"]");
		Matcher m = p.matcher(word);
		return !m.find();
	}

	/**
	 * Tries to search the word in the dictionary, and enter into the edit mode if
	 * an entry is found. If no entry is found, an error message is simply displayed
	 * 
	 * @param word the word to search
	 */
	public void submitSearch(String word) {

		JSONArray definitions = this.controller.searchWord(word);

		if (definitions == null) {
			this.result.setText("Error: Something went wrong (check the console).");
		} else if (definitions.get(0).equals("NOT_FOUND")) {
			this.result.setText("Error: this word does not exist in the dictionary.");
		} else {
			enterEditMode(word, definitions);
		}

	}

}

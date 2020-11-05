package client.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;

import client.controller.ControllerClient;

/**
 * AddPanel is in charge of the tab offering the possibility to add an entry to
 * the dictionary
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
@SuppressWarnings("serial")
public class AddPanel extends JPanel {

	private ControllerClient controllerClient;
	private JTextField wordBox;
	private JTextField definition1;
	private JTextField definition2;
	private JTextField definition3;
	private JTextField definition4;
	private JTextField definition5;
	private JLabel result;

	/**
	 * AddPanel is in charge of the tab offering the possibility to add an entry to
	 * the dictionary
	 * 
	 * @param controllerClient the controller to use to communicate with the model
	 *                         classes
	 */
	public AddPanel(ControllerClient controllerClient) {

		this.controllerClient = controllerClient;

		this.setLayout(new GridBagLayout());

		JLabel welcomeLabel = new JLabel("Here, you can add a word to the dictionary");
		welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
		GridBagConstraints welcomeC = new GridBagConstraints();
		welcomeC.anchor = GridBagConstraints.CENTER;
		welcomeC.gridwidth = 3;
		welcomeC.weighty = 5;
		welcomeC.gridy = 0;
		this.add(welcomeLabel, welcomeC);

		Action submit = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitAdd();
			}
		};

		JLabel word = new JLabel("Word :");
		GridBagConstraints wordC = new GridBagConstraints();
		wordC.weightx = 0;
		wordC.weighty = 1;
		wordC.gridx = 0;
		wordC.gridy = 1;
		wordC.anchor = GridBagConstraints.WEST;
		wordC.insets = new Insets(0, 10, 0, 20);
		this.add(word, wordC);

		wordBox = new JTextField();
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
		GridBagConstraints defC = new GridBagConstraints();
		defC.weightx = 0;
		defC.weighty = 1;
		defC.gridx = 0;
		defC.gridy = 2;
		defC.gridwidth = 2;
		defC.anchor = GridBagConstraints.WEST;
		defC.insets = new Insets(0, 10, 0, 20);
		this.add(definitionsLabel, defC);

		definition1 = new JTextField();
		definition1.addActionListener(submit);
		GridBagConstraints def1C = new GridBagConstraints();
		def1C.fill = GridBagConstraints.HORIZONTAL;
		def1C.weightx = 4;
		def1C.weighty = 1;
		def1C.gridx = 2;
		def1C.gridy = 2;
		def1C.insets = new Insets(0, 0, 0, 20);
		this.add(definition1, def1C);

		definition2 = new JTextField();
		definition2.addActionListener(submit);
		GridBagConstraints def2C = new GridBagConstraints();
		def2C.fill = GridBagConstraints.HORIZONTAL;
		def2C.weightx = 4;
		def2C.weighty = 1;
		def2C.gridx = 2;
		def2C.gridy = 3;
		def2C.insets = new Insets(0, 0, 0, 20);
		this.add(definition2, def2C);

		definition3 = new JTextField();
		definition3.addActionListener(submit);
		GridBagConstraints def3C = new GridBagConstraints();
		def3C.fill = GridBagConstraints.HORIZONTAL;
		def3C.weightx = 4;
		def3C.weighty = 1;
		def3C.gridx = 2;
		def3C.gridy = 4;
		def3C.insets = new Insets(0, 0, 0, 20);
		this.add(definition3, def3C);

		definition4 = new JTextField();
		definition4.addActionListener(submit);
		GridBagConstraints def4C = new GridBagConstraints();
		def4C.fill = GridBagConstraints.HORIZONTAL;
		def4C.weightx = 4;
		def4C.weighty = 1;
		def4C.gridx = 2;
		def4C.gridy = 5;
		def4C.insets = new Insets(0, 0, 0, 20);
		this.add(definition4, def4C);

		definition5 = new JTextField();
		definition5.addActionListener(submit);
		GridBagConstraints def5C = new GridBagConstraints();
		def5C.fill = GridBagConstraints.HORIZONTAL;
		def5C.weightx = 4;
		def5C.weighty = 1;
		def5C.gridx = 2;
		def5C.gridy = 6;
		def5C.insets = new Insets(0, 0, 0, 20);
		this.add(definition5, def5C);

		JButton submitButton = new JButton(" Add ");
		GridBagConstraints submitC = new GridBagConstraints();
		submitC.anchor = GridBagConstraints.CENTER;
		submitC.gridy = 7;
		submitC.weighty = 1;
		submitC.gridwidth = 3;
		submitButton.addActionListener(submit);
		this.add(submitButton, submitC);

		result = new JLabel("");
		result.setFont(new Font("", Font.ITALIC, 14));
		GridBagConstraints resultC = new GridBagConstraints();
		resultC.fill = GridBagConstraints.BOTH;
		resultC.weighty = 5;
		resultC.gridy = 8;
		resultC.gridwidth = 3;
		resultC.anchor = GridBagConstraints.WEST;
		resultC.insets = new Insets(0, 40, 0, 0);
		this.add(result, resultC);

	}

	/**
	 * Gets the word and the definitions from the text fields, checks there
	 * validity, and sends them to add them to the dictionary
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void submitAdd() {

		String word = this.wordBox.getText();

		if (word.equals("")) {
			this.result.setText("Error: You didn't enter a word");
		} else if (!validWord(word)) {
			this.result.setText("Error: The word you entered is not valid");
		} else {
			JSONArray definitions = new JSONArray();

			String def1 = this.definition1.getText();
			if (!def1.equals("")) {
				if (!validDefinition(def1)) {
					this.result.setText("Error: One of your definitions contains unvalid characters");
					return;
				} else {
					definitions.add(def1);
				}
			}

			String def2 = this.definition2.getText();
			if (!def2.equals("")) {
				if (!validDefinition(def2)) {
					this.result.setText("Error: One of your definitions contains unvalid characters");
					return;
				} else {
					definitions.add(def2);
				}
			}

			String def3 = this.definition3.getText();
			if (!def3.equals("")) {
				if (!validDefinition(def3)) {
					this.result.setText("Error: One of your definitions contains unvalid characters");
					return;
				} else {
					definitions.add(def3);
				}
			}

			String def4 = this.definition4.getText();
			if (!def4.equals("")) {
				if (!validDefinition(def4)) {
					this.result.setText("Error: One of your definitions contains unvalid characters");
					return;
				} else {
					definitions.add(def4);
				}
			}

			String def5 = this.definition5.getText();
			if (!def5.equals("")) {
				if (!validDefinition(def5)) {
					this.result.setText("Error: One of your definitions contains unvalid characters");
					return;
				} else {
					definitions.add(def5);
				}
			}

			if (definitions.isEmpty()) {
				this.result.setText("You didn't enter any definition");
			} else {
				this.result.setText(this.controllerClient.addWord(word, definitions));
				this.wordBox.setText("");
				this.definition1.setText("");
				this.definition2.setText("");
				this.definition3.setText("");
				this.definition4.setText("");
				this.definition5.setText("");
			}
		}

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

}

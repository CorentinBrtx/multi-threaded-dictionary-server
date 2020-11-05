package client.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;

import client.controller.ControllerClient;

/**
 * SearchPanel is in charge of the tab offering the possibility to search the
 * definitions of a word in the dictionary
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
@SuppressWarnings("serial")
public class SearchPanel extends JPanel {

	private JTextField searchBox;
	private JLabel definitionsLabel;
	private ControllerClient controllerClient;

	/**
	 * SearchPanel is in charge of the tab offering the possibility to search the
	 * definitions of a word in the dictionary
	 * 
	 * @param controllerClient the controller to use to communicate with the model
	 *                         classes
	 */
	public SearchPanel(ControllerClient controllerClient) {

		this.controllerClient = controllerClient;

		this.setLayout(new GridBagLayout());

		JLabel welcomeLabel = new JLabel("Welcome. Feel free to ask for the meaning of any word.");
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

		this.definitionsLabel = new JLabel("");
		definitionsLabel.setFont(new Font("", Font.ITALIC, 14));
		GridBagConstraints defC = new GridBagConstraints();
		defC.fill = GridBagConstraints.BOTH;
		defC.weighty = 5;
		defC.gridy = 2;
		defC.gridwidth = 2;
		defC.anchor = GridBagConstraints.WEST;
		defC.insets = new Insets(0, 40, 0, 0);
		this.add(this.definitionsLabel, defC);

	}

	/**
	 * Submits the search to the controller and displays the result (it can be a
	 * list of definitions or a message of error)
	 * 
	 * @param word the word to search
	 */
	@SuppressWarnings("unchecked")
	public void submitSearch(String word) {

		if (word.equals("")) {
			this.definitionsLabel.setText("Error: You didn't enter a word");
			this.repaint();
		}

		else {
			JSONArray definitions = this.controllerClient.searchWord(word);

			this.definitionsLabel.setText("<html><ul>");

			if (definitions == null) {
				this.definitionsLabel.setText("Error: Something went wrong (check the console).");
			} else if (definitions.get(0).equals("NOT_FOUND")) {
				this.definitionsLabel.setText("Error: this word does not exist in the dictionary.");
			} else {
				definitions.forEach(definition -> this.definitionsLabel
						.setText(this.definitionsLabel.getText() + "<li>" + definition + "</li>"));

				this.definitionsLabel.setText(this.definitionsLabel.getText() + "</ul></html>");
				this.repaint();
			}

		}
	}
}

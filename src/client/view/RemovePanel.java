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

import client.controller.ControllerClient;

/**
 * RemovePanel is in charge of the tab offering the possibility to remove an
 * entry from the dictionary
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
@SuppressWarnings("serial")
public class RemovePanel extends JPanel {

	private ControllerClient controllerClient;
	private JTextField wordBox;
	private JLabel result;

	/**
	 * RemovePanel is in charge of the tab offering the possibility to remove an
	 * entry from the dictionary
	 * 
	 * @param controllerClient the controller to use to communicate with the model
	 *                         classes
	 */
	public RemovePanel(ControllerClient controllerClient) {

		this.controllerClient = controllerClient;

		this.setLayout(new GridBagLayout());

		JLabel welcomeLabel = new JLabel("Enter a word to remove it from the dictionary");
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
				submitRemove();
			}
		};

		wordBox = new JTextField();
		wordBox.addActionListener(submit);
		GridBagConstraints wboxC = new GridBagConstraints();
		wboxC.fill = GridBagConstraints.HORIZONTAL;
		wboxC.weightx = 7;
		wboxC.weighty = 1;
		wboxC.gridx = 0;
		wboxC.gridy = 1;
		wboxC.insets = new Insets(0, 20, 0, 10);
		this.add(wordBox, wboxC);

		JButton submitButton = new JButton("Remove");
		submitButton.addActionListener(submit);
		GridBagConstraints submitC = new GridBagConstraints();
		submitC.weightx = 0;
		submitC.gridx = 1;
		submitC.gridy = 1;
		submitC.anchor = GridBagConstraints.EAST;
		submitC.insets = new Insets(0, 0, 0, 30);
		this.add(submitButton, submitC);

		result = new JLabel("");
		result.setFont(new Font("", Font.ITALIC, 14));
		GridBagConstraints resultC = new GridBagConstraints();
		resultC.fill = GridBagConstraints.BOTH;
		resultC.weighty = 5;
		resultC.gridy = 2;
		resultC.gridwidth = 3;
		resultC.anchor = GridBagConstraints.WEST;
		resultC.insets = new Insets(0, 40, 0, 0);
		this.add(result, resultC);

	}

	/**
	 * Gets the word written in the text field and display the result of the removal
	 * on the screen
	 * 
	 */
	protected void submitRemove() {

		String word = this.wordBox.getText();

		if (word.equals("")) {
			this.result.setText("Error : you didn't enter a word");
			return;
		} else {
			this.result.setText(this.controllerClient.removeWord(word));
		}

	}

}

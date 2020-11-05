package server.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import server.controller.ControllerServer;

/**
 * DictionaryPanel is in charge of the tab displaying the list of all the
 * entries of the dictionary.
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
@SuppressWarnings("serial")
public class DictionaryPanel extends JPanel {

	private ControllerServer controller;
	private JPanel mainList;

	/**
	 * DictionaryPanel is in charge of the tab displaying the list of all the
	 * entries of the dictionary.
	 * 
	 * @param controller the controller to use to communicate with the model classes
	 */
	@SuppressWarnings("unchecked")
	public DictionaryPanel(ControllerServer controller) {

		this.controller = controller;

		setLayout(new BorderLayout());

		mainList = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		mainList.add(new JPanel(), gbc);

		JSONObject dictionary = controller.getDictionary();

		dictionary.forEach((word, definitions) -> addPanel((String) word, (JSONArray) definitions));

		add(new JScrollPane(mainList));

	}

	/**
	 * Creates a new panel for an entry of the dictionary. This panel is designed to
	 * be displayed as a list in the Dictionary tab, each panel corresponding to a
	 * word in the dictionary.
	 * <p>
	 * The panel also offers two buttons: one to modify the word and the
	 * definitions, and one to remove the word from the dictionary.
	 * 
	 * @param word        the word in the dictionary
	 * @param definitions the definitions of the specified word
	 */
	@SuppressWarnings({ "unchecked" })
	private void addPanel(String word, JSONArray definitions) {

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		JLabel wordLabel = new JLabel(word) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(120, 20);
			}
		};
		GridBagConstraints wordC = new GridBagConstraints();
		wordC.anchor = GridBagConstraints.WEST;
		wordC.gridx = 0;
		wordC.gridheight = GridBagConstraints.REMAINDER;
		wordC.weighty = 1;
		wordC.weightx = 0;
		wordC.insets = new Insets(0, 5, 0, 5);
		panel.add(wordLabel, wordC);

		definitions.forEach(definition -> {
			JLabel defLabel = new JLabel((String) definition);
			GridBagConstraints defC = new GridBagConstraints();
			defC.anchor = GridBagConstraints.WEST;
			defC.gridx = 1;
			defC.weightx = 0;
			defC.weighty = 1;
			panel.add(defLabel, defC);
		});

		JButton edit = new JButton("Edit");
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editWord(panel, word, definitions);
			}
		});
		GridBagConstraints editC = new GridBagConstraints();
		editC.gridx = 3;
		editC.weightx = 0;
		editC.gridheight = GridBagConstraints.REMAINDER;
		editC.anchor = GridBagConstraints.WEST;
		editC.insets = new Insets(5, 30, 5, 0);
		panel.add(edit, editC);

		JButton remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.removeWord(word);
			}
		});
		GridBagConstraints removeC = new GridBagConstraints();
		removeC.gridx = 4;
		removeC.weightx = 1;
		removeC.gridheight = GridBagConstraints.REMAINDER;
		removeC.anchor = GridBagConstraints.WEST;
		removeC.insets = new Insets(5, 10, 5, 20);
		panel.add(remove, removeC);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainList.add(panel, gbc, 0);
	}

	/**
	 * editWord turns a panel into the edit mode, which means that all the JLabel
	 * are replaced by JTextField to let the user modify the word and its
	 * definitions.
	 * 
	 * @param panel       the panel to be modified. The panel must respect the
	 *                    format of the panels created by
	 *                    {@link #addPanel(String, JSONArray) addPanel}
	 * @param oldWord     the word corresponding to the panel
	 * @param definitions the definitions of the word
	 */
	@SuppressWarnings({ "unchecked" })
	protected void editWord(JPanel panel, String oldWord, JSONArray definitions) {

		panel.removeAll();
		panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		Action submitAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSONObject newData = new JSONObject();

				String newWord = ((JTextField) panel.getComponent(0)).getText();

				newData.put("WORD", newWord);

				JSONArray newDefinitions = new JSONArray();
				ArrayList<Component> defComponents = new ArrayList<Component>(Arrays.asList(panel.getComponents()));

				for (Component c : defComponents.subList(1, defComponents.size() - 1)) {
					newDefinitions.add(((JTextField) c).getText());
				}

				newData.put("DEFINITIONS", newDefinitions);

				controller.editWord(oldWord, newData);
			}
		};

		JTextField wordBox = new JTextField(oldWord) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(120, 20);
			}
		};
		wordBox.addActionListener(submitAction);
		GridBagConstraints wordC = new GridBagConstraints();
		wordC.anchor = GridBagConstraints.WEST;
		wordC.gridx = 0;
		wordC.gridheight = GridBagConstraints.REMAINDER;
		wordC.weighty = 1;
		wordC.weightx = 0;
		wordC.insets = new Insets(0, 5, 0, 5);
		panel.add(wordBox, wordC);

		definitions.forEach(definition -> {
			JTextField defBox = new JTextField((String) definition);
			defBox.addActionListener(submitAction);
			GridBagConstraints defC = new GridBagConstraints();
			defC.anchor = GridBagConstraints.WEST;
			defC.fill = GridBagConstraints.HORIZONTAL;
			defC.gridx = 1;
			defC.weightx = 1;
			defC.weighty = 1;
			panel.add(defBox, defC);
		});

		JButton submit = new JButton("Submit");
		submit.addActionListener(submitAction);
		GridBagConstraints submitC = new GridBagConstraints();
		submitC.gridx = 3;
		submitC.weightx = 0;
		submitC.gridheight = GridBagConstraints.REMAINDER;
		submitC.anchor = GridBagConstraints.WEST;
		submitC.insets = new Insets(5, 20, 5, 10);
		panel.add(submit, submitC);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;

	}

	/**
	 * This method must be called when there has been a change in the dictionary. It
	 * clears the entire tab and build all the panels again to display the updated
	 * data.
	 */
	@SuppressWarnings("unchecked")
	public void updateDictionary() {

		this.mainList.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		mainList.add(new JPanel(), gbc);

		JSONObject dictionary = controller.getDictionary();

		dictionary.forEach((word, definitions) -> addPanel((String) word, (JSONArray) definitions));

	}

}

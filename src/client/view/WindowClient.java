package client.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import client.controller.ControllerClient;
import client.model.DictionaryClient;

/**
 * WindowClient creates the GUI and calls the different other classes to create
 * each tab
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
@SuppressWarnings("serial")
public class WindowClient extends JFrame {

	private ControllerClient controllerClient;

	/**
	 * WindowClient creates the GUI and calls the different other classes to create
	 * each tab
	 * 
	 * @param client the DictionaryClient class linked to the GUI
	 */
	public WindowClient(DictionaryClient client) {

		this.controllerClient = new ControllerClient(client, this);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

		this.setTitle("Distributed Dictionary");

		JTabbedPane tp = new JTabbedPane();

		JPanel searchPanel = new SearchPanel(this.controllerClient);
		tp.add("Search", searchPanel);

		JPanel addPanel = new AddPanel(this.controllerClient);
		tp.add("Add a word", addPanel);

		JPanel removePanel = new RemovePanel(this.controllerClient);
		tp.add("Remove a word", removePanel);

		JPanel editPanel = new EditPanel(this.controllerClient);
		tp.add("Modify an entry", editPanel);

		this.add(tp);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				controllerClient.closeClient();
				super.windowClosing(e);
				System.exit(0);
			}

		});

		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}

package server.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import server.controller.ControllerServer;
import server.model.ClientConnection;
import server.model.DictionaryServer;

/**
 * WindowServer creates the GUI to manage the server and calls the different
 * other classes to create each tab
 * 
 * @author Corentin Berteaux - Student ID: 1144624
 *
 */
@SuppressWarnings("serial")
public class WindowServer extends JFrame {

	private ControllerServer controllerServer;
	private DictionaryPanel dictionaryPanel;
	private ClientsPanel clientsPanel;

	/**
	 * WindowServer creates the GUI to manage the server and calls the different
	 * other classes to create each tab
	 * 
	 * @param server the corresponding DictionaryServer instance
	 */
	public WindowServer(DictionaryServer server) {

		this.controllerServer = new ControllerServer(server, this);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

		this.setTitle("Distributed Dictionary - Server");

		JTabbedPane tp = new JTabbedPane();

		dictionaryPanel = new DictionaryPanel(this.controllerServer);
		tp.add("Dictionary", dictionaryPanel);

		clientsPanel = new ClientsPanel(this.controllerServer);
		tp.add("Clients", clientsPanel);

		this.add(tp);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showOptionDialog(null, "What do you want to do?", "Exit confirmation",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
						new String[] { "Close window and server", "Close only the window", "Cancel" }, null);

				if (confirmed == JOptionPane.YES_OPTION) {
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else if (confirmed == JOptionPane.NO_OPTION) {
					dispose();
				} else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public ControllerServer getControllerServer() {
		return controllerServer;
	}

	public void updateDictionary() {
		this.dictionaryPanel.updateDictionary();
		this.repaint();

	}

	public void displayNewClient(Socket s1, ClientConnection newClient, Date creationDate) {
		this.clientsPanel.displayNewClient(s1, newClient, creationDate);
		this.repaint();

	}

	public void closeClient(ClientConnection client) {
		this.clientsPanel.closeClient(client);
		this.repaint();
	}

}

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
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import server.controller.ControllerServer;
import server.model.ClientConnection;

/**
 * ClientsPanel is in charge of the tab displaying the list of all the clients
 * currently connected.
 * 
 * @author Corentin Berteaux - Student ID : 1144624
 *
 */
@SuppressWarnings("serial")
public class ClientsPanel extends JPanel {

	@SuppressWarnings("unused")
	private ControllerServer controller;
	private JPanel mainList;

	/**
	 * ClientsPanel is in charge of the tab displaying the list of all the clients
	 * currently connected.
	 * 
	 * @param controller the controller to use to communicate with the model classes
	 */
	public ClientsPanel(ControllerServer controller) {

		this.controller = controller;

		setLayout(new BorderLayout());

		mainList = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		mainList.add(new JPanel(), gbc);

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		JLabel threadNameLabel = new JLabel("Thread name") {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints nameC = new GridBagConstraints();
		nameC.anchor = GridBagConstraints.WEST;
		nameC.fill = GridBagConstraints.BOTH;
		nameC.gridx = 0;
		nameC.weightx = 3;
		nameC.insets = new Insets(0, 5, 0, 5);
		panel.add(threadNameLabel, nameC);

		JLabel threadIDLabel = new JLabel("Thread ID") {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints idC = new GridBagConstraints();
		idC.anchor = GridBagConstraints.WEST;
		idC.fill = GridBagConstraints.BOTH;
		idC.gridx = 1;
		idC.weightx = 3;
		idC.insets = new Insets(0, 5, 0, 5);
		panel.add(threadIDLabel, idC);

		JLabel addressLabel = new JLabel("Client's address") {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints addressC = new GridBagConstraints();
		addressC.anchor = GridBagConstraints.WEST;
		addressC.fill = GridBagConstraints.BOTH;
		addressC.gridx = 2;
		addressC.weightx = 5;
		addressC.insets = new Insets(0, 5, 0, 5);
		panel.add(addressLabel, addressC);

		JLabel dateLabel = new JLabel("Date of connection") {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints dateC = new GridBagConstraints();
		dateC.anchor = GridBagConstraints.WEST;
		dateC.fill = GridBagConstraints.BOTH;
		dateC.gridx = 3;
		dateC.weightx = 5;
		dateC.insets = new Insets(0, 5, 0, 5);
		panel.add(dateLabel, dateC);

		JLabel disconnectLabel = new JLabel("") {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(80, 20);
			}
		};
		GridBagConstraints disconnectC = new GridBagConstraints();
		disconnectC.anchor = GridBagConstraints.WEST;
		disconnectC.fill = GridBagConstraints.BOTH;
		disconnectC.gridx = 4;
		disconnectC.weightx = 0;
		disconnectC.insets = new Insets(0, 5, 0, 5);
		panel.add(disconnectLabel, disconnectC);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridwidth = GridBagConstraints.REMAINDER;
		gbc2.weightx = 1;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		mainList.add(panel, gbc2, 0);

		add(new JScrollPane(mainList));

	}

	/**
	 * Creates a new panel for a new client. This panel is designed to be displayed
	 * in a list in the Clients tab , each panel corresponding to a specific client.
	 * <p>
	 * The panel also offers a button to end the connection with the client.
	 * 
	 * @param socket       the socket corresponding to the connection with the
	 *                     client
	 * @param newClient    the ClientConnection instance in charge of the connection
	 *                     with the client
	 * @param creationDate the start time of the connection
	 */
	public void displayNewClient(Socket socket, ClientConnection newClient, Date creationDate) {

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		JLabel threadNameLabel = new JLabel(newClient.getName().toString()) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints nameC = new GridBagConstraints();
		nameC.anchor = GridBagConstraints.WEST;
		nameC.fill = GridBagConstraints.BOTH;
		nameC.gridx = 0;
		nameC.weightx = 3;
		nameC.insets = new Insets(0, 5, 0, 5);
		panel.add(threadNameLabel, nameC);

		JLabel threadIDLabel = new JLabel("" + newClient.getId()) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints idC = new GridBagConstraints();
		idC.anchor = GridBagConstraints.WEST;
		idC.fill = GridBagConstraints.BOTH;
		idC.gridx = 1;
		idC.weightx = 3;
		idC.insets = new Insets(0, 5, 0, 5);
		panel.add(threadIDLabel, idC);

		JLabel addressLabel = new JLabel(socket.getRemoteSocketAddress().toString()) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints addressC = new GridBagConstraints();
		addressC.anchor = GridBagConstraints.WEST;
		addressC.fill = GridBagConstraints.BOTH;
		addressC.gridx = 2;
		addressC.weightx = 5;
		addressC.insets = new Insets(0, 5, 0, 5);
		panel.add(addressLabel, addressC);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		JLabel dateLabel = new JLabel(formatter.format(creationDate)) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 20);
			}
		};
		GridBagConstraints dateC = new GridBagConstraints();
		dateC.anchor = GridBagConstraints.WEST;
		dateC.fill = GridBagConstraints.BOTH;
		dateC.gridx = 3;
		dateC.weightx = 5;
		dateC.insets = new Insets(0, 5, 0, 5);
		panel.add(dateLabel, dateC);

		JButton disconnect = new JButton("Disconnect");
		disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newClient.stopConnection();
			}
		});
		GridBagConstraints disconnectC = new GridBagConstraints();
		disconnectC.gridx = 4;
		disconnectC.weightx = 0;
		disconnectC.gridheight = GridBagConstraints.REMAINDER;
		disconnectC.anchor = GridBagConstraints.WEST;
		disconnectC.insets = new Insets(5, 5, 5, 10);
		panel.add(disconnect, disconnectC);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainList.add(panel, gbc, 1);

	}

	/**
	 * Removes a client from the list displayed.
	 * 
	 * @param client the ClientConnection instance corresponding to the client to
	 *               remove
	 */
	public void closeClient(ClientConnection client) {

		for (Component c : mainList.getComponents()) {
			JPanel p = (JPanel) c;
			if (p.getComponents().length > 2) {
				if (((JLabel) p.getComponent(1)).getText().equals("" + client.getId())) {
					mainList.remove(c);
				}
			}
		}

	}

}

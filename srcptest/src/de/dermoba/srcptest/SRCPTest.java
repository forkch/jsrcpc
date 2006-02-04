/*
 * Created on 08.07.2005
 *
 */
package de.dermoba.srcptest;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.dermoba.srcp.client.InfoDataListener;
import de.dermoba.srcp.client.SRCPSession;
import de.dermoba.srcp.common.exception.SRCPException;

public class SRCPTest extends JFrame implements ActionListener, InfoDataListener {

	protected String[] allVerbs = {"INIT", "SET", "GET", "TERM", "WAIT"};
	protected Integer[] allBusses = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	protected String[] allDevGroups = {"GL", "SM", "GA", "FB", "TIME", "POWER", "SERVER", "SESSION", "LOCK", "DESCRIPTION"};

	protected JPanel fenster = null;
	protected JTextArea protokoll = null;
	protected JTextArea info = null;
	protected JPanel combos = null;
	protected JComboBox verbs = null;
	protected JComboBox busses = null;
	protected JComboBox devGroups = null;
	protected JTextField params = null;

	protected JButton send = null;
	
	protected SRCPSession session = null;
	
	public SRCPTest() {
		super("SRCP Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		protokoll = new JTextArea(20,40);
		info = new JTextArea(20,40);
		combos = new JPanel();
		verbs = new JComboBox(allVerbs);
		busses = new JComboBox(allBusses);
		devGroups = new JComboBox(allDevGroups);
		params = new JTextField(30);
		combos.add(verbs);
		combos.add(busses);
		combos.add(devGroups);
		combos.add(params);
		getContentPane().add(combos, BorderLayout.NORTH);
		JScrollPane commandScroll = new JScrollPane(protokoll);
		JScrollPane infoScroll = new JScrollPane(info);
		fenster = new JPanel(new BorderLayout());
		fenster.add(commandScroll, BorderLayout.NORTH);
		fenster.add(infoScroll, BorderLayout.SOUTH);
		getContentPane().add(fenster, BorderLayout.SOUTH);
		send = new JButton("SEND");
		send.setDefaultCapable(true);
		getRootPane().setDefaultButton(send);
		send.addActionListener(this);
		getContentPane().add(send, BorderLayout.CENTER);
		pack();
		setVisible(true);
		try {
			session = new SRCPSession("127.0.0.1", 12345);
			session.getInfoChannel().addInfoDataListener(this);
		} catch (SRCPException e) {
			protokoll.insert(e.getMessage() + "\n", 0);
		}
	}

	public void actionPerformed(ActionEvent pArg0) {
		sendReceive(verbs.getSelectedItem() + " " + busses.getSelectedItem() + " " + devGroups.getSelectedItem() + " " + params.getText() + "\r\n");
	}
	
	public void infoDataReceived(String pInfoData) {
		info.insert(pInfoData + "\n", 0);
		protokoll.setCaretPosition(0);
		protokoll.scrollRectToVisible(new Rectangle(0,0,1,1));
	}

	private void sendReceive(String o) {
		protokoll.insert(">>>" + o, 0);
		try {
			protokoll.insert(session.getCommandChannel().send(o) + "\n", 0);
		} catch (SRCPException e) {
			protokoll.insert(e.getMessage() + "\n", 0);
		}
		protokoll.setCaretPosition(0);
		protokoll.scrollRectToVisible(new Rectangle(0,0,1,1));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SRCPTest();
	}
}

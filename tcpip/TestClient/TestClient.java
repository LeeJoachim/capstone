package tcpip.TestClient;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

class ButtonPanel extends JPanel {
    ClientPanel clientPanel;
    JTextField ip;
    private JButton connectButton;
    private JButton closeButton;
    public ButtonPanel(ClientPanel clientPanel) {
        this.clientPanel = clientPanel;
        ip = new JTextField(15);
        ip.setText("localhost");
        add(ip);

        connectButton = new JButton("Connect");
        add(connectButton);
		closeButton = new JButton("Close");
        add(closeButton);

        connectButton.addActionListener(clientPanel);
        closeButton.addActionListener(clientPanel);
        
    }
    public String getIpAddress() {
        return ip.getText();
    }
    
}

class InputListener extends KeyAdapter {

    JTextField textInput; 
    ClientPanel clientPanel;

    public InputListener(JTextField textInput, ClientPanel clientPanel) {
        this.textInput = textInput;
        this.clientPanel = clientPanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			String msg = textInput.getText().trim();
			clientPanel.writeText("[I say] " + msg);
			clientPanel.sendLine(msg);
			textInput.setText("");
		}
    }

    
    
}

class ReportPanel extends JPanel
{
	ClientPanel clientPanel;
	JTextField reportText;
	JButton reportButton;
	JButton queryButton;
	ReportPanel(ClientPanel clientPanel) {
		this.clientPanel = clientPanel;

		reportText = new JTextField(15);
		reportButton = new JButton("Report");
		queryButton = new JButton("Query");

		add(reportText);
		add(reportButton);
		add(queryButton);

		reportButton.addActionListener(clientPanel);
		queryButton.addActionListener(clientPanel);
	}
	String getReportText() {
		return reportText.getText();
	}
}


class InputPanel extends JPanel {

    ClientPanel clientPanel;
    private JTextField textInput;
    private ReportPanel reportPanel;

    public InputPanel(ClientPanel clientPanel) {
        setLayout(new GridLayout(2,1));
        this.clientPanel = clientPanel;

        textInput = new JTextField();
        add(textInput);
		textInput.addKeyListener(new InputListener(textInput,clientPanel));

        reportPanel = new ReportPanel(clientPanel);
        add(reportPanel);

    }
    /* delegaiton */
    public String getReportText() {
        return reportPanel.getReportText();
    }
    
}

class ClientPanel extends JPanel implements ActionListener {

    String ipAddress = "localhost";
    private ButtonPanel buttonPanel;
    private JTextArea textBox;
    private JScrollPane textPane;
    private InputPanel inputPanel;
    
    /* for chatting */
    private Socket chatClient;
    private BufferedReader fromChatServer;
    private PrintWriter toChatServer;
    private Thread chatThread;


    public ClientPanel() {
        setLayout(new BorderLayout());
		buttonPanel = new ButtonPanel(this);
		add(buttonPanel,BorderLayout.NORTH);

        textBox = new JTextArea();
        textBox.setBorder(BorderFactory.createLoweredBevelBorder());
		textPane = new JScrollPane(textBox);
        add(textPane,BorderLayout.CENTER);

        inputPanel = new InputPanel(this);
		add(inputPanel,BorderLayout.SOUTH);



    }

    public void sendLine(String msg) {
        toChatServer.println(msg);
        toChatServer.flush();
    }

    public void writeText(String string) {
        textBox.append(string + "\r\n");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();
        String ipAddress = buttonPanel.getIpAddress();

        if (command.equals("Report")) {
            try {
                writeText("Reporting...");
                Socket client = new Socket(ipAddress, 7000);
                
                BufferedReader fromServer = 
                    new BufferedReader(new InputStreamReader(client.getInputStream())); // decorator
                PrintWriter toServer = 
                    new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

                toServer.println("1"); // report mode
                toServer.flush();

                String msg = inputPanel.getReportText();
                toServer.println(msg);
                toServer.flush();

                msg = fromServer.readLine();
                writeText("[Server says] " + msg);

                toServer.close();
                fromServer.close();
                client.close();
                return;

            } catch (IOException e) {
                System.out.println(e);
            }
        } else if (command.equals("Query")) {
            try {
                writeText("Asking...");
                Socket client = new Socket(ipAddress, 7000);
                
                BufferedReader fromServer = 
                    new BufferedReader(new InputStreamReader(client.getInputStream())); // decorator
                PrintWriter toServer = 
                    new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

                toServer.println("2"); // query mode
                toServer.flush();

                String msg = inputPanel.getReportText();
                toServer.println(msg);
                toServer.flush();
                
                msg = fromServer.readLine();
                writeText("[Server says] " + msg);

                toServer.close();
                fromServer.close();
                client.close();
                return;

            } catch (IOException e) {
                System.out.println(e);
            }

        } else if (command.equals("Connect")) {
            try {
                writeText("Connected...");
                chatClient = new Socket(ipAddress, 7000);
                fromChatServer = new BufferedReader(new InputStreamReader(chatClient.getInputStream()));
				toChatServer = new PrintWriter(chatClient.getOutputStream());
                
                toChatServer.println("0");
				toChatServer.flush();

                chatThread = new Thread() {
                    public void run() {
                        try {
                            String msg;
                            while (true) {
                                msg = fromChatServer.readLine();
								writeText("[Server says] " + msg);                                
                            }
                        } catch (IOException e) {
							System.out.println(e);
                        }
                    }
                };
                chatThread.start();

            } catch (IOException e) {
                System.out.println(e);
            }
        } else if (command.equals("Close")) {
            try {
                if (toChatServer != null) {
                    sendLine("-1"); // end of chatting
                    toChatServer.close();
                }
                if (fromChatServer != null) fromChatServer.close();				
				if (chatClient != null) chatClient.close();
				if (chatThread != null) chatThread.interrupt();
                writeText("Connection closed");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    
}

class ClientFrame extends JFrame {

    public ClientFrame() {
        setTitle("Client");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.add(new ClientPanel());
    }
    
}

public class TestClient {
    public static void main(String[] args) {
        ClientFrame clientFrame = new ClientFrame();
        clientFrame.setVisible(true);
    }
}

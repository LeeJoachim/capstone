package tcpip.TestServer;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;

class ButtonPanel extends JPanel {
    ServerPanel serverPanel;
    JButton startButton;
    JButton stopButton;
    JButton salesRecordButton;

    ButtonPanel(ServerPanel serverPanel) {
        this.serverPanel = serverPanel;

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        salesRecordButton = new JButton("Sales Record");

        add(startButton);
        add(stopButton);
        add(salesRecordButton);

        startButton.addActionListener(serverPanel);
        stopButton.addActionListener(serverPanel);
        salesRecordButton.addActionListener(serverPanel);

    }
}

class InputListener extends KeyAdapter {
    JTextField textInput;
    ServerPanel serverPanel;
    InputListener(JTextField textInput, ServerPanel serverPanel) {
        this.textInput = textInput;
        this.serverPanel = serverPanel;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            /* This method may be used to trim space from the beginning and end of a string. */
            String msg = textInput.getText().trim();
            serverPanel.writeText("[I say ] " + msg);
            String clientIP = serverPanel.getSelectedIP();
			serverPanel.sendLine(clientIP,msg);
			textInput.setText("");
        }
    }
    
}

class ClientService extends Thread {
    Socket client;
    HashMap<String, PrintWriter> writers;
    ServerPanel serverPanel;

    public ClientService(Socket client, HashMap<String, PrintWriter> writers, ServerPanel serverPanel) {
        this.client = client;
        this.writers = writers;
        this.serverPanel = serverPanel;
    }
    @Override
    public void run() {
        try {
            BufferedReader fromClient = 
                new BufferedReader(new InputStreamReader(client.getInputStream())); // decorator
            PrintWriter toClient = 
                new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

            String clientIP = client.getInetAddress().getHostAddress();
            String flag = fromClient.readLine();
            int n = Integer.parseInt(flag);

            if (n == 1) { // report mode
                serverPanel.writeText("[" + clientIP + "] " + "reported his earning today.");
				String todayEarning = fromClient.readLine();
				serverPanel.writeText("[" + clientIP + "] " +  todayEarning + " Won.");
				toClient.println("complete");
				toClient.flush();

				fromClient.close();
				toClient.close();
				client.close();

                int parseTodayEarning = Integer.parseInt(todayEarning);
                Integer recordedIncome = ServerFrame.salesRecord.get(clientIP);
                if (recordedIncome == null) { 
                    ServerFrame.salesRecord.put(clientIP, parseTodayEarning);
                } else {
                    ServerFrame.salesRecord.put(clientIP, recordedIncome+parseTodayEarning);
                }

                serverPanel.writeText("[" + clientIP + "] [Total Income] : " +  ServerFrame.salesRecord.get(clientIP) + " Won.");
                
                return;
            } else if (n == 2) { // query mode
                Integer recordedIncome = ServerFrame.salesRecord.get(clientIP);
                if (recordedIncome == null) {
                    recordedIncome = 0;
                }
                toClient.println(recordedIncome);
                toClient.flush();

                serverPanel.writeText("[" + clientIP + "] " + "asked his total selling.");

                fromClient.close();
                toClient.close();
                client.close();
                return;
            } else { // chatting mode
                writers.put(clientIP, toClient);
                String msg;

                while (true) {
                    msg = fromClient.readLine();
                    if (msg.equals("-1")) { // end of chatting
                        break;
                    }
                    serverPanel.writeText("[" + clientIP + " says] " + msg);
                }

                writers.remove(clientIP);
                serverPanel.writeText("Connection with " + clientIP + " closed.");


                fromClient.close();
                toClient.close();
                client.close();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
}

/* convention : port number above 4000 */
class ServerRole extends Thread {

    ServerPanel serverPanel;
    private ServerSocket listenerSocket;
    private HashMap<String, PrintWriter> writers; // key : ip address

    public ServerRole(ServerPanel serverPanel) {
        this.serverPanel = serverPanel;
        writers = new HashMap<String, PrintWriter>();
    }
    public void run() {
        try {
            listenerSocket = new ServerSocket(7000); // create port
            serverPanel.writeText("Server started ...");
            serverPanel.writeText(serverPanel.myIp + " on port: 7000");
            while (true) {
                Socket client = listenerSocket.accept(); // waiting...
                String clientIp = client.getInetAddress().getHostAddress();
                serverPanel.writeText(clientIp + " is connected...");
                serverPanel.addComboBoxItem(clientIp);

                ClientService connection = new ClientService(client, writers, serverPanel);
                connection.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendLine(String clientIP, String msg) {
        PrintWriter toClient = writers.get(clientIP);
        toClient.println(msg);
        toClient.flush();
    }

    public void closeSocket() {
        try {
            listenerSocket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
}

class InputPanel extends JPanel {

    ServerPanel serverPanel;
    JComboBox<String> ips;
    JTextField textInput;

    InputPanel(ServerPanel serverPanel) {
        setLayout(new GridLayout(2,1));
        this.serverPanel = serverPanel;
        ips = new JComboBox<String>();
        add(ips);
		textInput = new JTextField();
		add(textInput);
		textInput.addKeyListener(new InputListener(textInput, serverPanel));
    }
    void addComboBoxItem(String ip) {
		
		ips.removeItem(ip);
		ips.insertItemAt(ip,0);
		ips.setSelectedIndex(0);
	}
    String getSelectedIP() {
		return ips.getItemAt(ips.getSelectedIndex());
	}
}

class ServerPanel extends JPanel implements ActionListener {

    ButtonPanel buttonPanel;

    JTextArea textBox;
    JScrollPane textPane;

    InputPanel inputPanel; // has combobox with ips

    String myIp;
    ServerRole server;

    ServerPanel() {
        setLayout(new BorderLayout());

        buttonPanel = new ButtonPanel(this);
        add(buttonPanel, BorderLayout.NORTH);

        textBox = new JTextArea();
        textPane = new JScrollPane(textBox);
        add(textPane, BorderLayout.CENTER);

        inputPanel = new InputPanel(this);
        add(inputPanel, BorderLayout.SOUTH);

        writeText("Please press start button");

        try {
            myIp = InetAddress.getLocalHost().getHostAddress();
            System.out.println(myIp);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    
    /* delegation */
    public void addComboBoxItem(String clientIp) {
        inputPanel.addComboBoxItem(clientIp);
    }
    public void sendLine(String clientIP, String msg) {
        server.sendLine(clientIP, msg);
    }
    public String getSelectedIP() {
        return inputPanel.getSelectedIP();
    }
    /**/

    void writeText(String msg) {
		textBox.append(msg + "\r\n");
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        String command  = e.getActionCommand();
        if (command.equals("Start")) {
            server = new ServerRole(this);
            server.start();
        } else if (command.equals("Stop")) {
            server.closeSocket();
            server.interrupt();
        } else if (command.equals("Sales Record")) {
            Iterator<Integer> i = ServerFrame.salesRecord.values().iterator();
            
            int total = 0;
            while(i.hasNext()) {
                int income = i.next();
                total += income;
            }

            JOptionPane.showMessageDialog(this, "Total Income: " + total + " Won");
        }
    }   
}

class ServerFrame extends JFrame {

    static HashMap<String, Integer> salesRecord
        = new HashMap<String, Integer>();
    
    ServerFrame() {
        setTitle("Server");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.add(new ServerPanel());
    }
}

class TestServer {
    public static void main(String[] args) {
        ServerFrame serverFrame = new ServerFrame();
        serverFrame.setVisible(true);
    }
}

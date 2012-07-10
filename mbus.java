import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.io.*;

import gnu.io.*;

class serialListener implements SerialPortEventListener{
	String str;
	char esc = 27, soh = 1;
	byte[] byteArray = new byte[150];
	
	@Override
    public void serialEvent(SerialPortEvent event){
        switch(event.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
            	//System.out.println("oha");
                dataAvailable(event);
                break;
        }
    }

    protected void dataAvailable(SerialPortEvent event) {
    	str = "";
    	int x=0;
    	try {
			while(serial.is.available() > 0) {
				byteArray[x] = (byte)serial.is.read();
				x++;
			}
			if(byteArray[0]==(byte)esc&&mbus.cbrespond.isSelected())serial.respond(byteArray,x);
			if(byteArray[0]==(byte)soh)serial.interpret(byteArray,x);
			str = "";
		} catch (IOException e) {
			System.out.println("Fehler beim Lesen empfangener Daten");
		}
    }
}


class serial
{
	
	static SerialPort port = null;
	static InputStream is;
	static PrintStream os;
	
	static boolean connect(String portname){
		System.setProperty("gnu.io.rxtx.SerialPorts", portname);
		@SuppressWarnings("rawtypes")
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portId = null;  // will be set if port found
		while (portIdentifiers.hasMoreElements())
		{
		    CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
		    if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL &&
		       pid.getName().equals(portname)) 
		    {
		        portId = pid;
		        break;
		    }
		}
		if(portId == null)
		{
		    mbus.taConsole.append("Could not find serial port " + portname + "\n");
		}
		if(portId != null)try {
		    port = (SerialPort) portId.open(
		        "name",
		        10000
		    );
		} catch(PortInUseException e) {
			mbus.taConsole.append("Port already in use: " + e + "\n");
		}
		if(portId != null)try{
			port.setSerialPortParams(
		    9600,
		    SerialPort.DATABITS_8,
		    SerialPort.STOPBITS_1,
		    SerialPort.PARITY_EVEN);
		}catch(Exception e){
			System.err.println(e);
		}
		if(portId != null)try{
			port.addEventListener(new serialListener());
		}catch(TooManyListenersException e){
			System.err.println(e);
		}
		if(portId != null){
			port.notifyOnDataAvailable(true);
		}
		
		if(portId != null)try{
			is = port.getInputStream();
		}catch(Exception e){}
		if(portId != null)try{
			os = new PrintStream(port.getOutputStream());
		}catch(Exception e){}
		if(portId != null){
			if(!mbus.portOpen)mbus.taConsole.append(portname+" verbunden\n");
			return true;
		}else{
			return false;
		}
	}
	
	static boolean disconnect(){
		if(is != null)try{
			is.close();
		}catch(Exception e){}
		if (os != null)os.close();
		if (port != null) port.close();
		if (mbus.portOpen)mbus.taConsole.append("Verbindung getrennt\n");
		return false;
	}

	static void respond(byte[] com, int length){
		int checksumInt=0;
		boolean corrC = false;
		String comm, checksum;
		
		for(int x=0;x<length-3;x++)checksumInt += (int) com[x];
		checksum=checksum(checksumInt);
		if(checksum.charAt(0)==(char)com[length-3]&&checksum.charAt(1)==(char)com[length-2])corrC = true;
		comm = Integer.toHexString((int)com[1]);
		comm = comm.toUpperCase();
		
		if(!corrC)sendR(CodeTab.NAK[0]);
		
		if(comm.equals(CodeTab.ICC[0])&&corrC)sendR(CodeTab.ICC[0]);
		if(comm.equals(CodeTab.GET_ID[0])&&corrC)sendID();
	}
	static void interpret(byte[] resp, int length){
		String commEcho, checksum;
		int checksumInt = 0;
		boolean corrR = false;
		
		for(int x=0;x<length-3;x++)checksumInt += (int) resp[x];
		checksum=checksum(checksumInt);
		if(checksum.charAt(0)==(char)resp[length-3]&&checksum.charAt(1)==(char)resp[length-2])corrR = true;
		commEcho = Integer.toHexString((int)resp[1]);
		
		if(commEcho.equals(CodeTab.GET_ID[0])&&corrR)getID(resp, length);
		if(commEcho.equals(CodeTab.GET_DATA[0])&&corrR)getData(resp, length);
		if(corrR){
			for(int x=0;x<CodeTab.CODE.length;x++){
				if(commEcho.equals(CodeTab.CODE[x][0]))System.out.println(CodeTab.CODE[x][1]);
			}
		}
	}
	static void sendC(String comm){
		int checksumInt = 0;
		checksumInt += send(27);
		checksumInt += send(Integer.parseInt(comm, 16));
		send(checksum(checksumInt));
	}
	static void sendR(String comm){
		int commInt = Integer.parseInt(comm, 16);
		int checksumInt = 0;
		
		checksumInt += send(1);
		checksumInt += send(commInt);
		send(checksum(checksumInt));
	}
	static void sendID(){
		int checksumInt=0;
		
		checksumInt += send(1);
		checksumInt += send(82);
		checksumInt += send(mbus.IDNO);
		checksumInt += send(mbus.ID);
		checksumInt += send(mbus.REV);
		send(checksum(checksumInt));
	}
	static void getID(byte[] id, int length){
		if(!mbus.GotHostID)mbus.HostIDno = mbus.HostIDno+(char)id[2]+(char)id[3]+(char)id[4]+(char)id[5];
		if(!mbus.GotHostID)for(int x=6;x<length-14;x++)mbus.HostID = mbus.HostID+(char)id[x];
		if(!mbus.GotHostID)for(int x=length-14;x<length-3;x++)mbus.HostRev = mbus.HostRev+(char)id[x];
		mbus.lHostIDNo.setText("ID-No.: "+mbus.HostIDno);
		mbus.lHostID.setText("ID: "+mbus.HostID);
		mbus.lHostRev.setText("Rev: "+mbus.HostRev);
		if(!mbus.HostIDno.equals("")&&!mbus.HostID.equals("")&&!mbus.HostRev.equals(""))mbus.GotHostID = true;
	}
	static void getData(byte[] data, int length){
		
	}
	static int send(int sendInt){
		try{
			os.write((byte)sendInt);
		}catch(Exception e){}
		
		return sendInt;
	}
	static int send(char sendChar){
		try{
			os.write((byte)sendChar);
		}catch(Exception e){}
		return (int) sendChar;
	}
	static int send(String sendString){
		int checksumInt=0;
		for(int x=0;x<sendString.length();x++)checksumInt+=send(sendString.charAt(x));
		return checksumInt;
	}
	static String checksum(int checksumInt){
		String checksumString;
		char cr = 13;
		
		checksumString = Integer.toHexString(checksumInt);
		checksumString = checksumString.toUpperCase();
		return ""+checksumString.charAt(checksumString.length()-2)+checksumString.charAt(checksumString.length()-1)+cr;
	}
}

class UIListener implements ActionListener
{
    public void actionPerformed(ActionEvent e) 
    {
    	String event = e.getActionCommand();
    	if(event.equals("Verbinden")){
    		 if(!mbus.portOpen)mbus.taConsole.append("Öffne "+mbus.tfDevice.getText()+"...\n");
             if(!mbus.portOpen)mbus.portOpen = serial.connect(mbus.tfDevice.getText());
    	}
    	if(event.equals("Trennen")){
    		if(mbus.portOpen)mbus.taConsole.append("Schließe...\n");
            if(mbus.portOpen)mbus.portOpen = serial.disconnect();
    	}
       if(event.equals("textConnect")){
    	   if(!mbus.portOpen)mbus.taConsole.append("Öffne "+mbus.tfDevice.getText()+"...\n");
    	   if(!mbus.portOpen)mbus.portOpen = serial.connect(mbus.tfDevice.getText());
       }
       if(event.equals("initialisieren")&&mbus.portOpen){
    	   serial.sendC(CodeTab.GET_ID[0]);
       }
       if(event.equals("sendCommand")&&mbus.portOpen)serial.sendC(mbus.tfCommand.getText());
    }           
}

class windowListener extends WindowAdapter{
	public void windowClosing(WindowEvent e){
		if(mbus.portOpen)serial.disconnect();
	}
}

public class mbus extends JFrame 
{
	private static final long serialVersionUID = 2L;
	static String IDNO = "0161";
	static String ID = "'MBUS (C)F. TOEMBOEL'";
	static String REV = "00.20:04.01";
	
	static String HostIDno = "";
	static String HostID = "";
	static String HostRev = "";
	
	static boolean portOpen = false;
	static boolean GotHostID = false;
	
	static JPanel ui = new JPanel();
	static JPanel debug = new JPanel();
		
	static JScrollPane scroll;
	
	static JButton bConnect = new JButton("Verbinden");
	static JButton bDisconnect = new JButton("Trennen");
	static JButton bReqID = new JButton("initialisieren");
    static JLabel lHostIDNo = new JLabel("ID-No.: nicht verbunden");
    static JLabel lHostID = new JLabel("ID: nicht verbunden");
    static JLabel lHostRev = new JLabel("Rev: nicht verbunden");
    static JTextArea taConsole = new JTextArea();
    static JTextField tfDevice = new JTextField("/dev/ttyUSB0");
    static JTextField tfCommand = new JTextField("52");
    static JMenuBar menu = new JMenuBar();
    static JMenu mDatei = new JMenu("Datei");
    static JMenuItem mOeffnen = new JMenuItem("Öffnen");
    static JCheckBox cbrespond = new JCheckBox("respond");
    
    public mbus(){
        //initMenu();
        initComponents();         
    }
 
    private void initComponents(){
        setTitle(ID+", Revision "+REV);
        
        setLayout(new GridLayout(1,2));
                
        ui.setLayout(new GridLayout(9,1));
        ui.add(bConnect);
        ui.add(tfDevice);
        ui.add(bDisconnect);
        ui.add(bReqID);
        ui.add(cbrespond);
        ui.add(tfCommand);
        ui.add(lHostIDNo);
        ui.add(lHostID);
        ui.add(lHostRev);
        
        taConsole.setColumns(30);
        taConsole.setLineWrap(true);
        taConsole.setRows(30);
        taConsole.setWrapStyleWord(true);
        taConsole.setEditable(false);
        
        scroll = new JScrollPane(taConsole);
        
        debug.add(scroll);
        
        bConnect.addActionListener(new UIListener());
        bDisconnect.addActionListener(new UIListener());
        bReqID.addActionListener(new UIListener());
        tfCommand.addActionListener(new UIListener());
        tfCommand.setActionCommand("sendCommand");
        tfDevice.addActionListener(new UIListener());
        tfDevice.setActionCommand("textConnect");

        
        getContentPane().setLayout(new GridLayout(1,2));
        getContentPane().add(ui);
        getContentPane().add(debug);
        pack();
        addWindowListener(new windowListener());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void initMenu(){
    	mDatei.add(mOeffnen);
    	menu.add(mDatei);
    	setJMenuBar(menu);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new mbus();
            }
        });
    }
}
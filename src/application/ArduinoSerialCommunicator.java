/*
 * Source:
 * https://arduino.stackexchange.com/questions/16750/reading-arduino-serial-stream-in-java-using-jserialcomm
 * uses jSerialComm:
 * https://github.com/Fazecast/jSerialComm/wiki/Event-Based-Reading-Usage-Example
 */
package application;

import java.io.InputStream;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class ArduinoSerialCommunicator extends DataSource {
	public static SerialPort userPort;
	static InputStream in;

	public ArduinoSerialCommunicator(DataDisplay dataDisplay) {
		super(dataDisplay);

//		Scanner input = new Scanner(System.in);
		/*
		 * This returns an array of commport addresses, not useful for the client but
		 * useful for iterating through to get an actual list of com parts available
		 */
		SerialPort ports[] = SerialPort.getCommPorts();
		SerialPort userPort = SerialPort.getCommPort(ports[0].getSystemPortName());
//		SerialPort userPort = null;
//		if (ports.length > 1) {
//			int i = 1;
//			// User port selection
//			System.out.println("COM Ports available on machine");
//			for (SerialPort port : ports) {
//				// iterator to pass through port array
//				System.out.println(i++ + ": " + port.getSystemPortName()); // print windows com ports
//			}
//			System.out.println("Please select COM PORT: 'COM#'");
//			userPort = SerialPort.getCommPort(input.nextLine());
//		} else if (ports.length == 0) {
//			System.out.println("no ports available");
//		} else {
//			userPort = SerialPort.getCommPort(ports[0].getSystemPortName());
//		}
//		input.close();

		// Initializing port
		userPort.openPort();
		if (userPort.isOpen()) {
			System.out.println("Port initialized!");
			// timeout not needed for event based reading
			// userPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
		} else {
			System.out.println("Port not available");
			return;
		}

		DataSource currentSource = this;

		userPort.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return;
				byte[] newData = new byte[userPort.bytesAvailable()];
				int numRead = userPort.readBytes(newData, newData.length);
//				System.out.println("Read " + numRead + " bytes.");
//				System.out.println(Arrays.toString(newData));
				// prevents from reading blank bits
//				System.out.println("\"" + data + "\"");
				if (newData.length > 0) {
//					String data = (new String(newData)).trim();
					long ticks = Long.parseLong(new String(newData));
					currentSource.add(DataDisplay.getCurrentTime(), ticks);
				}
			}
		});
	}
}

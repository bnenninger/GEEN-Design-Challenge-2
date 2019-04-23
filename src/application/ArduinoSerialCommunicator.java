/*
 * Source:
 * https://arduino.stackexchange.com/questions/16750/reading-arduino-serial-stream-in-java-using-jserialcomm
 * uses jSerialComm:
 * https://github.com/Fazecast/jSerialComm/wiki/Event-Based-Reading-Usage-Example
 */
package application;

import com.fazecast.jSerialComm.*;
import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;

public class JSerialCommTestWorking {
	public static SerialPort userPort;
	static InputStream in;

	public static void main(String args[]) {
		Scanner input = new Scanner(System.in);
		/*
		 * This returns an array of commport addresses, not useful for the client but
		 * useful for iterating through to get an actual list of com parts available
		 */
		SerialPort ports[] = SerialPort.getCommPorts();
		int i = 1;
		// User port selection
		System.out.println("COM Ports available on machine");
		for (SerialPort port : ports) {
			// iterator to pass through port array
			System.out.println(i++ + ": " + port.getSystemPortName()); // print windows com ports
		}
		System.out.println("Please select COM PORT: 'COM#'");
		SerialPort userPort = SerialPort.getCommPort(input.nextLine());

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
//				float data = ByteBuffer.wrap(newData).getFloat();
				//this method gets 50% blank input, idk why, this prevents a blank byte array from being turned into a double
				if (newData.length > 0) {
					System.out.println(Double.parseDouble(new String(newData)));
					System.out.print(new String(newData));
				}
			}
		});
	}
}

package teste;

/* Ping.java
Exemplo de Ping em java sobre UDP */

import java.io.*;
import java.net.*;

public class Ping {
static final int DEFAULT_PORT = 7;
static final int UDP_HEADER = 20 + 8;
static final int BACKSPACE = 8;

public static void main (String[] args) throws IOException {
parseArgs (args);
init ();
for (int i = 0; (i < count) || (count == 0); ++ i) {
  long past = System.currentTimeMillis ();
  ping (i, past);
  try {
    pong (i, past);
  } catch (InterruptedIOException ignored) {
  }
}
socket.close ();
printStats ();
}

static String host = null;
static int port, count = 32, delay = 1000, size = 64;
static boolean flood = false;

static void parseArgs (String args[]) {
for (int i = 0; i < args.length; ++ i) {
  if (args[i].startsWith ("-")) {
    if (args[i].equals ("-c") && (i < args.length - 1))
      count = Integer.parseInt (args[++ i]);
    else if (args[i].equals ("-i") && (i < args.length - 1))
      delay = Math.max (10, Integer.parseInt (args[++ i]));
    else if (args[i].equals ("-s") && (i < args.length - 1))
      size = Integer.parseInt (args[++ i]);
    else if (args[i].equals ("-f"))
      flood = true;
    else
      syntaxError ();
  } else {
    if (host != null)
      syntaxError ();
    int colon = args[i].indexOf (":");
    host = (colon > -1) ? args[i].substring (0, colon) : args[i];
    port = ((colon > -1) && (colon < args[i].length () - 1)) ?
      Integer.parseInt (args[i].substring (colon + 1)) : DEFAULT_PORT;
  }
}
if (host == null)
  syntaxError ();
}

static void syntaxError () {
throw new IllegalArgumentException
  ("Ping [-c count] [-i wait] [-s packetsize] [-f] <hostname>[:<port>]");
}

static DatagramSocket socket;
static byte[] outBuffer, inBuffer;
static DatagramPacket outPacket, inPacket;

static void init () throws IOException {
socket = new DatagramSocket ();
outBuffer = new byte[Math.max (12, size - UDP_HEADER)];
outPacket = new DatagramPacket (outBuffer, outBuffer.length,
                                InetAddress.getByName (host), port);
inBuffer = new byte[outBuffer.length];
inPacket = new DatagramPacket (inBuffer, inBuffer.length);
}

static int sent = 0;

static void ping (int seq, long past) throws IOException {
writeInt (seq, outBuffer, 0);
writeLong (past, outBuffer, 4);
socket.send (outPacket);
++ sent;
if (flood) {
  System.out.write ('.');
  System.out.flush ();
}
}

static final void writeInt (int datum, byte[] dst, int offset) {
dst[offset] = (byte) (datum >> 24);
dst[offset + 1] = (byte) (datum >> 16);
dst[offset + 2] = (byte) (datum >> 8);
dst[offset + 3] = (byte) datum;
}

static final void writeLong (long datum, byte[] dst, int offset) {
writeInt ((int) (datum >> 32), dst, offset);
writeInt ((int) datum, dst, offset + 4);
}

static int received = 0;

static void pong (int seq, long past) throws IOException {
long present = System.currentTimeMillis ();
int tmpRTT = (maxRTT == 0) ? 500 : (int) maxRTT * 2;
int wait = Math.max (delay, (seq == count - 1) ? tmpRTT : 0);
do {
  socket.setSoTimeout (Math.max (1, wait - (int) (present - past)));
  socket.receive (inPacket);
  ++ received;
  present = System.currentTimeMillis ();
  processPong (present);
} while ((present - past < wait) && !flood);
}

static long minRTT = 100000, maxRTT = 0, totRTT = 0;

static void processPong (long present) {
int seq = readInt (inBuffer, 0);
long when = readLong (inBuffer, 4);
long rtt = present - when;
if (!flood) {
  System.out.println
    ((inPacket.getLength () + UDP_HEADER) +
     " bytes from " + inPacket.getAddress ().getHostName () +
     ": seq no " + seq + " time=" + rtt + " ms");
} else {
  System.out.write (BACKSPACE);
  System.out.flush ();
}
if (rtt < minRTT) minRTT = rtt;
if (rtt > maxRTT) maxRTT = rtt;
totRTT += rtt;
}

static final int readInt (byte[] src, int offset) {
return (src[offset] << 24) | ((src[offset + 1] & 0xff) << 16) |
  ((src[offset + 2] & 0xff) << 8) | (src[offset + 3] & 0xff);
}

static final long readLong (byte[] src, int offset) {
return ((long) readInt (src, offset) << 32) |
  ((long) readInt (src, offset + 4) & 0xffffffffL);
}

static void printStats () {
System.out.println
  (sent + " packets transmitted, " + received + " packets received, " +
   (100 * (sent - received) / sent) + "% packet loss");
if (received > 0)
  System.out.println ("round-trip min/avg/max = " + minRTT + '/' +
                      ((float) totRTT / received) + '/' + maxRTT + "ms");
}
}
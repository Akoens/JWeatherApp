package app.net;

import org.mindrot.jbcrypt.BCrypt;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class WeatherDataReceiver {

    private int port;
    private Thread thread;
    private SSLServerSocket serverSocket;
    private ArrayList<WeatherClientListener> listeners;

    public WeatherDataReceiver(int port) {
        this.port = port;
        thread = new Thread(this::run);
        listeners = new ArrayList<>();
    }

    private SSLServerSocket initServerSocket() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
            //Trust store
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream tin = getClass().getClassLoader().getResourceAsStream("cert/servercert.p12");
            trustStore.load(tin, "yeetus".toCharArray());
            tin.close();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            //Key store
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream kin = getClass().getClassLoader().getResourceAsStream("cert/servercert.p12");
            keyStore.load(kin, "yeetus".toCharArray());
            kin.close();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "yeetus".toCharArray());

            //SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), SecureRandom.getInstanceStrong());
            SSLServerSocket serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(port);
            serverSocket.setNeedClientAuth(true);
            serverSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
            return serverSocket;
    }

    public void addWeatherServerListener(WeatherClientListener listener) {
        listeners.add(listener);
    }

    public void listen() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        serverSocket = initServerSocket();
        thread.start();
        System.out.println("WeatherData listening on :" + port);
    }

    public void interrupt() {
        thread.interrupt();
    }

    private void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                WeatherClient client = new WeatherClient(socket);
                raiseOnConnect(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void raiseOnConnect(WeatherClient client) {
        for (WeatherClientListener listener : listeners) {
            listener.onConnect(client);
        }
    }

}

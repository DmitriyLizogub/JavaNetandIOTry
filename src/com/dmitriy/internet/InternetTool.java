package com.dmitriy.internet;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.*;
import java.nio.channels.FileChannel;

public class InternetTool{
    private static final String GET_BY_NAME_ADDRESS = "i.ua";
    private static final String GET_ALL_BY_NAME_ADDRESS = "google.com";
    private static final String INET_FILE_URL = "http://edge.quantserve.com/quant.js"; //can be html page, can't be image
    private static final String INET_IMAGE_URL = "http://www.stihi.ru/pics/2012/03/05/11334.jpg"; //return BufferedImage in text representation
    private static final String LOCAL_IMAGE_SRC_URL = "C:\\Users\\dlyzogub\\Downloads\\strange_alert.png";
    private static final String LOCAL_IMAGE_DIST_URL = "C:\\Users\\dlyzogub\\Downloads\\trololo.png";
    private static final String LOCAL_FILE_PROTOCOL_URL = "file:///C:/Users/dlyzogub/Downloads/trololo.png";
    private static final String GOOGLE_IP = "173.194.113.201";
    private static final String WHOIS_HOST = "whois.arin.net";
    private static final int WHOIS_PORT = 43;
    private static final int WHOIS_SOCKET_TIMEOUT = 10000;





    public static void main(String[] args) throws UnknownHostException{
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println(inetAddress);
        inetAddress = InetAddress.getByName(GET_BY_NAME_ADDRESS);
        System.out.println(inetAddress);
        InetAddress []ads = InetAddress.getAllByName(GET_ALL_BY_NAME_ADDRESS);
        for (InetAddress ad : ads) {
            System.out.println(ad);
        }

        try {
            getFileFromURL(INET_FILE_URL); //if simple URL - return html page source
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            getImageFromURL(INET_IMAGE_URL); //return BufferedImage in text representation
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileCopy(LOCAL_IMAGE_SRC_URL,LOCAL_IMAGE_DIST_URL); // on local machine

        try {
            getImageFromURL(LOCAL_FILE_PROTOCOL_URL);  //local URL
        } catch (IOException e) {
            e.printStackTrace();
        }

        whoIs(GOOGLE_IP);
    }

    private static void getFileFromURL(String address) throws MalformedURLException {
        URL page = new URL(address);
        StringBuffer text = new StringBuffer();
        try{
            HttpURLConnection connection = (HttpURLConnection) page.openConnection();
            connection.connect();
            System.out.println("");
            System.out.println(connection.getRequestMethod()+ "\n" + connection.getPermission() + "\n"  + connection.usingProxy() + "\n");
            InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
            BufferedReader buff = new BufferedReader(in);

            String line, key, header;
            int i = 0; //displays headers too
            do {
                key = connection.getHeaderFieldKey(i);
                header = connection.getHeaderField(i);
                if (key == null) {
                    key = "";
                } else {
                    key = key + ": ";
                }
                if (header != null) text.append(key + header + "\n");
                i++;
            } while (header != null);
            text.append("\n");
            do {
                line = buff.readLine();
                text.append(line + "\n");
            } while (line != null);
            System.out.println("");
            System.out.println(text);

        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private static void getImageFromURL(String address) throws IOException {
        URL image = new URL(address);
        RenderedImage img = ImageIO.read(image);
        System.out.println(img);
    }


    private static void fileCopy(String src, String dist) {
        try {
            FileChannel srcChannel = new FileInputStream(src).getChannel();
            FileChannel distChannel = new FileOutputStream(dist).getChannel();
            distChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            distChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void whoIs(String hostIP){
        byte []buff;
        int c = -1;
        String hostName = "";
        hostName ="n " + hostIP + "\n";
        try {
            Socket socket = new Socket(WHOIS_HOST, WHOIS_PORT);
            socket.setSoTimeout(WHOIS_SOCKET_TIMEOUT);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            buff = hostName.getBytes();
            out.write(buff);
            while ((c = in.read()) != -1) {
                System.out.print((char) c);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

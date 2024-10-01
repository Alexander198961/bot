package com.trading.ftp;

public class FtpDownloader {
    /*
    private static final String FTP_SERVER = "ftp.nasdaqtrader.com";
    private static final String FILE_PATH = "/SymbolDirectory/nasdaqlisted.txt";
    private static final String LOCAL_FILE_PATH = "nasdaqlisted.txt";

    public static void main(String[] args) {
        FTPClient ftpClient = new FTPClient();

        try {
            // Connect and login to the FTP server
            ftpClient.connect(FTP_SERVER);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login("anonymous", "guest");

            // Set file type to ASCII (for text files)
            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);

            // Download the file
            try (InputStream inputStream = ftpClient.retrieveFileStream(FILE_PATH);
                 OutputStream outputStream = new FileOutputStream(LOCAL_FILE_PATH)) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // Logout from the FTP server
            ftpClient.logout();

            // Now read the downloaded file
            readNasdaqListedFile(LOCAL_FILE_PATH);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void readNasdaqListedFile(String filePath) {
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
}

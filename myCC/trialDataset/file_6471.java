    public void viewFile(int file_underscorenx) {
        FTPClient ftp = new FTPClient();
        boolean error = false;
        try {
            int reply;
            ftp.connect("tgftp.nws.noaa.gov");
            ftp.login("anonymous", "");
            Log.d("WXDroid", "Connected to tgftp.nws.noaa.gov.");
            Log.d("WXDroid", ftp.getReplyString());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            ftp.changeWorkingDirectory("fax");
            Log.d("WXDroid", "working directory: " + ftp.printWorkingDirectory());
            ftp.setFileType(FTPClient.BINARY_underscoreFILE_underscoreTYPE);
            InputStream img_underscorefile = ftp.retrieveFileStream("PYAA10.gif");
            String storage_underscorestate = Environment.getExternalStorageState();
            if (storage_underscorestate.contains("mounted")) {
                String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NOAAWX/";
                File imageDirectory = new File(filepath);
                File local_underscorefile = new File(filepath + "PYAA10.gif");
                OutputStream out = new FileOutputStream(local_underscorefile);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = img_underscorefile.read(buffer)) != -1) {
                    if (Thread.interrupted() == true) {
                        String functionName = Thread.currentThread().getStackTrace()[2].getMethodName() + "()";
                        throw new InterruptedException("The function " + functionName + " was interrupted.");
                    }
                    out.write(buffer, 0, count);
                }
                wxDroid.showImage();
                out.flush();
                out.close();
                img_underscorefile.close();
                Log.d("WXDroid", "file saved: " + filepath + " " + local_underscorefile);
            } else {
                Log.d("WXDroid", "The SD card is not mounted");
            }
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }


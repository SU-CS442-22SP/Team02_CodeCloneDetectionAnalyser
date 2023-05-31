    public void copyURLToFile(TmpFile p_underscoreTmpFile) {
        byte[] l_underscoreBuffer;
        URLConnection l_underscoreConnection = null;
        DataInputStream l_underscoreIN = null;
        DataOutputStream l_underscoreOut = null;
        FileOutputStream l_underscoreFileOutStream = null;
        try {
            System.gc();
            if (error.compareTo(noError) == 0) {
                l_underscoreConnection = urlHome.openConnection();
                l_underscoreFileOutStream = new FileOutputStream(p_underscoreTmpFile.getAbsolutePath());
                l_underscoreOut = new DataOutputStream(l_underscoreFileOutStream);
                l_underscoreIN = new DataInputStream(l_underscoreConnection.getInputStream());
                l_underscoreBuffer = new byte[8192];
                int bytes = 0;
                while ((bytes = l_underscoreIN.read(l_underscoreBuffer)) > 0) {
                    l_underscoreOut.write(l_underscoreBuffer, 0, bytes);
                }
            }
        } catch (MalformedURLException mue) {
            error = "MalformedURLException in connecting url was " + mue.getMessage();
        } catch (IOException io) {
            error = "IOException in connecting url was " + io.getMessage();
        } catch (Exception e) {
            error = "Exception in connecting url was " + e.getMessage();
        } finally {
            try {
                l_underscoreIN.close();
                l_underscoreOut.flush();
                l_underscoreFileOutStream.flush();
                l_underscoreFileOutStream.close();
                l_underscoreOut.close();
            } catch (Exception e) {
                error = "Exception in connecting url was " + e.getMessage();
            }
        }
    }


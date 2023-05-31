    public static int getFilesize(String theURL) {
        URL url;
        URLConnection conn;
        int size = -1;
        try {
            url = new URL(theURL);
            conn = url.openConnection();
            conn.setRequestProperty("User-Agent", Main.USER_underscoreAGENT);
            conn.setConnectTimeout(Main.CONNECT_underscoreTIMEOUT * 2);
            conn.setReadTimeout(Main.READ_underscoreTIMEOUT * 2);
            size = conn.getContentLength();
            conn.getInputStream().close();
        } catch (FileNotFoundException fnfe) {
            return -2;
        } catch (ConnectException ce) {
            return -3;
        } catch (ProtocolException fpe) {
            return -2;
        } catch (IOException ioe) {
            return -2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


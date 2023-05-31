    public void run() {
        try {
            URL read = null;
            if (_underscorereadURL.indexOf("?") >= 0) {
                read = new URL(_underscorereadURL + "&id=" + _underscoreid);
            } else {
                read = new URL(_underscorereadURL + "?id=" + _underscoreid);
            }
            while (_underscorekeepGoing) {
                String line;
                while ((line = _underscorein.readLine()) != null) {
                    ConnectionHandlerLocal.DEBUG("< " + line);
                    _underscorelinesRead++;
                    _underscorelistener.incomingMessage(line);
                }
                if (_underscorelinesRead == 0) {
                    shutdown(true);
                    return;
                }
                if (_underscorekeepGoing) {
                    URLConnection urlConn = read.openConnection();
                    urlConn.setUseCaches(false);
                    _underscorein = new DataInputStream(urlConn.getInputStream());
                    _underscorelinesRead = 0;
                }
            }
            System.err.println("HttpReaderThread: stopping gracefully.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            shutdown(true);
        }
    }


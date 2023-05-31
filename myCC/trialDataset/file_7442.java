    public void run() {
        m_underscorestats.setRunning();
        URL url = m_underscorestats.url;
        if (url != null) {
            try {
                URLConnection connection = url.openConnection();
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    handleHTTPConnection(httpConnection, m_underscorestats);
                } else {
                    System.out.println("Unknown URL Connection Type " + url);
                }
            } catch (java.io.IOException ioe) {
                m_underscorestats.setStatus(m_underscorestats.IOError);
                m_underscorestats.setErrorString("Error making or reading from connection" + ioe.toString());
            }
        }
        m_underscorestats.setDone();
        m_underscoremanager.threadFinished(this);
    }


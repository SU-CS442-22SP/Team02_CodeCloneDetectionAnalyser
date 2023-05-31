    public void init() {
        System.out.println("Init applet...");
        int port = Integer.parseInt(getParameter("port"));
        int useUDP = Integer.parseInt(getParameter("udp"));
        boolean bUseUDP = false;
        if (useUDP > 0) bUseUDP = true;
        m_underscorestrWorld = getParameter("world");
        m_underscorestrHost = this.getCodeBase().getHost();
        try {
            new EnvironmentMap(getParameter("vrwmap"));
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        URL urlExperiment = null;
        InputStream expStream = null;
        try {
            String strPathExperiment = getParameter("experiment");
            if (strPathExperiment.length() > 0) {
                urlExperiment = new URL(getCodeBase(), strPathExperiment);
                expStream = urlExperiment.openStream();
            }
        } catch (java.net.MalformedURLException e) {
            System.out.println("Couldn't open url experiment: badly specified URL " + e.getMessage());
        } catch (Throwable t) {
            System.out.println("Couldn't open url experiment: " + t.getMessage());
        }
        try {
            System.out.println("Creating client, logging to " + m_underscorestrWorld);
            m_underscoreVRWClient = new VRWClient(m_underscorestrHost, port, true, bUseUDP);
            m_underscoreVRWClient.setInApplet(true);
            m_underscoreVRWClient.login(m_underscorestrWorld);
        } catch (java.io.IOException e) {
            System.out.println("IOException creating the VRWClient");
        }
        try {
            jsobj = JSObject.getWindow(this);
        } catch (Throwable t) {
            System.out.println("Exception getting Java Script Interface: " + t.getMessage());
        }
        refApplet = this;
        m_underscorefrmVRWConsole = new VRWConsoleFrame();
        m_underscorefrmVRWConsole.setTitle("VRW Client Console");
        m_underscorefrmVRWConsole.pack();
        m_underscorefrmVRWConsole.setSize(Math.max(300, m_underscorefrmVRWConsole.getSize().width), Math.max(200, m_underscorefrmVRWConsole.getSize().height));
        if (expStream != null) {
            System.out.println("Passing experiment stream to VRWConsoleFrame");
            m_underscorefrmVRWConsole.loadExperiment(expStream);
        }
        m_underscorefrmVRWConsole.setVisible(true);
    }


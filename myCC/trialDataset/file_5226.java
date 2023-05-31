    public static Dictionary getDefaultConfig(BundleContext bc) {
        final Dictionary config = new Hashtable();
        config.put(HttpConfig.HTTP_underscoreENABLED_underscoreKEY, getPropertyAsBoolean(bc, "org.knopflerfish.http.enabled", "true"));
        config.put(HttpConfig.HTTPS_underscoreENABLED_underscoreKEY, getPropertyAsBoolean(bc, "org.knopflerfish.http.secure.enabled", "true"));
        config.put(HttpConfig.HTTP_underscorePORT_underscoreKEY, getPropertyAsInteger(bc, "org.osgi.service.http.port", HTTP_underscorePORT_underscoreDEFAULT));
        config.put(HttpConfig.HTTPS_underscorePORT_underscoreKEY, getPropertyAsInteger(bc, "org.osgi.service.http.secure.port", HTTPS_underscorePORT_underscoreDEFAULT));
        config.put(HttpConfig.HOST_underscoreKEY, getPropertyAsString(bc, "org.osgi.service.http.hostname", ""));
        Properties mimeProps = new Properties();
        try {
            mimeProps.load(HttpConfig.class.getResourceAsStream("/mime.default"));
            String propurl = getPropertyAsString(bc, "org.knopflerfish.http.mime.props", "");
            if (propurl.length() > 0) {
                URL url = new URL(propurl);
                Properties userMimeProps = new Properties();
                userMimeProps.load(url.openStream());
                Enumeration e = userMimeProps.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    mimeProps.put(key, userMimeProps.getProperty(key));
                }
            }
        } catch (MalformedURLException ignore) {
        } catch (IOException ignore) {
        }
        Vector mimeVector = new Vector(mimeProps.size());
        Enumeration e = mimeProps.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            mimeVector.addElement(new String[] { key, mimeProps.getProperty(key) });
        }
        config.put(HttpConfig.MIME_underscorePROPS_underscoreKEY, mimeVector);
        config.put(HttpConfig.SESSION_underscoreTIMEOUT_underscoreKEY, getPropertyAsInteger(bc, "org.knopflerfish.http.session.timeout.default", 1200));
        config.put(HttpConfig.CONNECTION_underscoreTIMEOUT_underscoreKEY, getPropertyAsInteger(bc, "org.knopflerfish.http.connection.timeout", 30));
        config.put(HttpConfig.CONNECTION_underscoreMAX_underscoreKEY, getPropertyAsInteger(bc, "org.knopflerfish.http.connection.max", 50));
        config.put(HttpConfig.DNS_underscoreLOOKUP_underscoreKEY, getPropertyAsBoolean(bc, "org.knopflerfish.http.dnslookup", "false"));
        config.put(HttpConfig.RESPONSE_underscoreBUFFER_underscoreSIZE_underscoreDEFAULT_underscoreKEY, getPropertyAsInteger(bc, "org.knopflerfish.http.response.buffer.size.default", 16384));
        config.put(HttpConfig.DEFAULT_underscoreCHAR_underscoreENCODING_underscoreKEY, getPropertyAsString(bc, HttpConfig.DEFAULT_underscoreCHAR_underscoreENCODING_underscoreKEY, "ISO-8859-1"));
        config.put(HttpConfig.REQ_underscoreCLIENT_underscoreAUTH_underscoreKEY, getPropertyAsBoolean(bc, "org.knopflerfish.http.req.client.auth", "false"));
        return config;
    }


    protected void initializeFromURL(URL url, AVList params) throws IOException {
        URLConnection connection = url.openConnection();
        String message = this.validateURLConnection(connection, SHAPE_underscoreCONTENT_underscoreTYPES);
        if (message != null) {
            throw new IOException(message);
        }
        this.shpChannel = Channels.newChannel(WWIO.getBufferedInputStream(connection.getInputStream()));
        URLConnection shxConnection = this.getURLConnection(WWIO.replaceSuffix(url.toString(), INDEX_underscoreFILE_underscoreSUFFIX));
        if (shxConnection != null) {
            message = this.validateURLConnection(shxConnection, INDEX_underscoreCONTENT_underscoreTYPES);
            if (message != null) Logging.logger().warning(message); else {
                InputStream shxStream = this.getURLStream(shxConnection);
                if (shxStream != null) this.shxChannel = Channels.newChannel(WWIO.getBufferedInputStream(shxStream));
            }
        }
        URLConnection prjConnection = this.getURLConnection(WWIO.replaceSuffix(url.toString(), PROJECTION_underscoreFILE_underscoreSUFFIX));
        if (prjConnection != null) {
            message = this.validateURLConnection(prjConnection, PROJECTION_underscoreCONTENT_underscoreTYPES);
            if (message != null) Logging.logger().warning(message); else {
                InputStream prjStream = this.getURLStream(prjConnection);
                if (prjStream != null) this.prjChannel = Channels.newChannel(WWIO.getBufferedInputStream(prjStream));
            }
        }
        this.setValue(AVKey.DISPLAY_underscoreNAME, url.toString());
        this.initialize(params);
        URL dbfURL = WWIO.makeURL(WWIO.replaceSuffix(url.toString(), ATTRIBUTE_underscoreFILE_underscoreSUFFIX));
        if (dbfURL != null) {
            try {
                this.attributeFile = new DBaseFile(dbfURL);
            } catch (Exception e) {
            }
        }
    }


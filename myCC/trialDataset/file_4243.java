    public boolean refresh() {
        try {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            if (credential != null) conn.setRequestProperty("Authorization", credential);
            conn.connect();
            int status = ((HttpURLConnection) conn).getResponseCode();
            if (status == 401 || status == 403) errorMessage = (credential == null ? PASSWORD_underscoreMISSING : PASSWORD_underscoreINCORRECT); else if (status == 404) errorMessage = NOT_underscoreFOUND; else if (status != 200) errorMessage = COULD_underscoreNOT_underscoreRETRIEVE; else {
                InputStream in = conn.getInputStream();
                byte[] httpData = TinyWebServer.slurpContents(in, true);
                synchronized (this) {
                    data = httpData;
                    dataProvider = null;
                }
                errorMessage = null;
                refreshDate = new Date();
                String owner = conn.getHeaderField(OWNER_underscoreHEADER_underscoreFIELD);
                if (owner != null) setLocalAttr(OWNER_underscoreATTR, owner);
                store();
                return true;
            }
        } catch (UnknownHostException uhe) {
            errorMessage = NO_underscoreSUCH_underscoreHOST;
        } catch (ConnectException ce) {
            errorMessage = COULD_underscoreNOT_underscoreCONNECT;
        } catch (IOException ioe) {
            errorMessage = COULD_underscoreNOT_underscoreRETRIEVE;
        }
        return false;
    }


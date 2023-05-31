    public MapInfo getMap(double latitude, double longitude, double wanted_underscoremapblast_underscorescale, int image_underscorewidth, int image_underscoreheight, String file_underscorepath_underscorewo_underscoreextension, ProgressListener progress_underscorelistener) throws IOException {
        try {
            double mapserver_underscorescale = getDownloadScale(wanted_underscoremapblast_underscorescale);
            URL url = new URL(getUrl(latitude, longitude, mapserver_underscorescale, image_underscorewidth, image_underscoreheight));
            if (Debug.DEBUG) Debug.println("map_underscoredownload", "loading map from url: " + url);
            URLConnection connection = url.openConnection();
            if (resources_underscore.getBoolean(GpsylonKeyConstants.KEY_underscoreHTTP_underscorePROXY_underscoreAUTHENTICATION_underscoreUSE)) {
                String proxy_underscoreuserid = resources_underscore.getString(GpsylonKeyConstants.KEY_underscoreHTTP_underscorePROXY_underscoreAUTHENTICATION_underscoreUSERNAME);
                String proxy_underscorepassword = resources_underscore.getString(GpsylonKeyConstants.KEY_underscoreHTTP_underscorePROXY_underscoreAUTHENTICATION_underscorePASSWORD);
                String auth_underscorestring = proxy_underscoreuserid + ":" + proxy_underscorepassword;
                auth_underscorestring = "Basic " + new sun.misc.BASE64Encoder().encode(auth_underscorestring.getBytes());
                connection.setRequestProperty("Proxy-Authorization", auth_underscorestring);
            }
            connection = setRequestProperties(connection);
            connection.connect();
            String mime_underscoretype = connection.getContentType().toLowerCase();
            if (!mime_underscoretype.startsWith("image")) {
                if (mime_underscoretype.startsWith("text")) {
                    HTMLViewerFrame viewer = new HTMLViewerFrame(url);
                    viewer.setSize(640, 480);
                    viewer.setTitle("ERROR on loading url: " + url);
                    viewer.setVisible(true);
                    throw new IOException("Invalid mime type (expected 'image/*'): received " + mime_underscoretype + "\nPage is displayed in HTML frame.");
                }
                throw new IOException("Invalid mime type (expected 'image/*'): received " + mime_underscoretype);
            }
            int content_underscorelength = connection.getContentLength();
            if (content_underscorelength < 0) progress_underscorelistener.actionStart(PROGRESS_underscoreLISTENER_underscoreID, 0, Integer.MIN_underscoreVALUE); else progress_underscorelistener.actionStart(PROGRESS_underscoreLISTENER_underscoreID, 0, content_underscorelength);
            String extension = mime_underscoretype.substring(mime_underscoretype.indexOf('/') + 1);
            String filename = file_underscorepath_underscorewo_underscoreextension + extension;
            MapInfo map_underscoreinfo = new MapInfo();
            map_underscoreinfo.setLatitude(latitude);
            map_underscoreinfo.setLongitude(longitude);
            map_underscoreinfo.setScale((float) getCorrectedMapblastScale(wanted_underscoremapblast_underscorescale));
            map_underscoreinfo.setWidth(image_underscorewidth);
            map_underscoreinfo.setHeight(image_underscoreheight);
            map_underscoreinfo.setFilename(filename);
            FileOutputStream out = new FileOutputStream(filename);
            byte[] buffer = new byte[BUFFER_underscoreSIZE];
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream(), BUFFER_underscoreSIZE);
            int sum_underscorebytes = 0;
            int num_underscorebytes = 0;
            while ((num_underscorebytes = in.read(buffer)) != -1) {
                out.write(buffer, 0, num_underscorebytes);
                sum_underscorebytes += num_underscorebytes;
                progress_underscorelistener.actionProgress(PROGRESS_underscoreLISTENER_underscoreID, sum_underscorebytes);
            }
            progress_underscorelistener.actionEnd(PROGRESS_underscoreLISTENER_underscoreID);
            in.close();
            out.close();
            return (map_underscoreinfo);
        } catch (NoRouteToHostException nrhe) {
            nrhe.printStackTrace();
            progress_underscorelistener.actionEnd(PROGRESS_underscoreLISTENER_underscoreID);
            String message = nrhe.getMessage() + ":\n" + resources_underscore.getString(DownloadMouseModeLayer.KEY_underscoreLOCALIZE_underscoreMESSAGE_underscoreDOWNLOAD_underscoreERROR_underscoreNO_underscoreROUTE_underscoreTO_underscoreHOST_underscoreMESSAGE);
            throw new IOException(message);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            progress_underscorelistener.actionEnd(PROGRESS_underscoreLISTENER_underscoreID);
            String message = fnfe.getMessage() + ":\n" + resources_underscore.getString(DownloadMouseModeLayer.KEY_underscoreLOCALIZE_underscoreMESSAGE_underscoreDOWNLOAD_underscoreERROR_underscoreFILE_underscoreNOT_underscoreFOUND_underscoreMESSAGE);
            throw new IOException(message);
        } catch (Exception e) {
            progress_underscorelistener.actionEnd(PROGRESS_underscoreLISTENER_underscoreID);
            e.printStackTrace();
            String message = e.getMessage();
            if (message == null) {
                Throwable cause = e.getCause();
                if (cause != null) message = cause.getMessage();
            }
            throw new IOException(message);
        }
    }


    public String getFeatureInfoHTML(Point3d GKposition, String[] layerIds, int featureCount) {
        String html = "";
        try {
            String request = null;
            if (version == VERSION_underscore030) {
                org.gdi3d.xnavi.services.w3ds.x030.GetFeatureInfo getFeatureInfo = new org.gdi3d.xnavi.services.w3ds.x030.GetFeatureInfo(this.serviceEndPoint);
                request = getFeatureInfo.createRequest(GKposition, layerIds, featureCount);
            } else if (version == VERSION_underscore040) {
                org.gdi3d.xnavi.services.w3ds.x040.GetFeatureInfo getFeatureInfo = new org.gdi3d.xnavi.services.w3ds.x040.GetFeatureInfo(this.serviceEndPoint);
                request = getFeatureInfo.createRequest(GKposition, layerIds, featureCount);
            } else if (version == VERSION_underscore041) {
                org.gdi3d.xnavi.services.w3ds.x041.GetFeatureInfo getFeatureInfo = new org.gdi3d.xnavi.services.w3ds.x041.GetFeatureInfo(this.serviceEndPoint);
                request = getFeatureInfo.createRequest(GKposition, layerIds, featureCount);
            }
            if (Navigator.isVerbose()) System.out.println(request);
            URL url = new URL(request);
            int contentLength = -1;
            URLConnection urlc;
            urlc = url.openConnection();
            urlc.setReadTimeout(Navigator.TIME_underscoreOUT);
            if (getEncoding() != null) {
                urlc.setRequestProperty("Authorization", "Basic " + getEncoding());
            }
            urlc.connect();
            String content_underscoretype = urlc.getContentType();
            if (content_underscoretype.equalsIgnoreCase("text/html") || content_underscoretype.equalsIgnoreCase("text/html;charset=UTF-8")) {
                InputStream is = urlc.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                StringBuffer sb = new StringBuffer();
                InputStreamReader isr = new InputStreamReader(bis);
                char chars[] = new char[10240];
                int len = 0;
                contentLength = 0;
                while ((len = isr.read(chars, 0, chars.length)) >= 0) {
                    sb.append(chars, 0, len);
                    contentLength += len;
                }
                chars = null;
                isr.close();
                bis.close();
                is.close();
                html = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }


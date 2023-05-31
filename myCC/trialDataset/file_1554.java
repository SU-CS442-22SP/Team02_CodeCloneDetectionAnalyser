    public Resource get(URL serviceUrl, String resourceId) throws Exception {
        Resource resource = new Resource();
        String openurl = serviceUrl.toString() + "?url_underscorever=Z39.88-2004" + "&rft_underscoreid=" + URLEncoder.encode(resourceId, "UTF-8") + "&svc_underscoreid=" + SVCID_underscoreADORE4;
        log.debug("OpenURL Request: " + openurl);
        URL url;
        try {
            url = new URL(openurl);
            HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
            int code = huc.getResponseCode();
            if (code == 200) {
                InputStream is = huc.getInputStream();
                resource.setBytes(StreamUtil.getByteArray(is));
                resource.setContentType(huc.getContentType());
            } else {
                log.error("An error of type " + code + " occurred for " + url.toString());
                throw new Exception("Cannot get " + url.toString());
            }
        } catch (MalformedURLException e) {
            throw new Exception("A MalformedURLException occurred for " + openurl);
        } catch (IOException e) {
            throw new Exception("An IOException occurred attempting to connect to " + openurl);
        }
        return resource;
    }


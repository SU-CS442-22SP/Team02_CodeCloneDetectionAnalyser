    @SuppressWarnings("unchecked")
    public HttpResponse putFile(String root, String to_underscorepath, File file_underscoreobj) throws DropboxException {
        String path = "/files/" + root + to_underscorepath;
        try {
            Path targetPath = new Path(path);
            String target = buildFullURL(secureProtocol, content_underscorehost, port, buildURL(targetPath.removeLastSegments(1).addTrailingSeparator().toString(), API_underscoreVERSION, null));
            HttpClient client = getClient(target);
            HttpPost req = new HttpPost(target);
            List nvps = new ArrayList();
            nvps.add(new BasicNameValuePair("file", targetPath.lastSegment()));
            req.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_underscore8));
            auth.sign(req);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_underscoreCOMPATIBLE);
            FileBody bin = new FileBody(file_underscoreobj, targetPath.lastSegment(), "application/octet-stream", null);
            entity.addPart("file", bin);
            req.setEntity(entity);
            HttpResponse resp = client.execute(req);
            resp.getEntity().consumeContent();
            return resp;
        } catch (Exception e) {
            throw new DropboxException(e);
        }
    }


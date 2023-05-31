    private Element returnAnnoBody(final String url) {
        DOMParser parser = new DOMParser();
        try {
            URL bodyURL = new URL(url);
            URLConnection url_underscorecon = bodyURL.openConnection();
            if (useAuthorization()) {
                url_underscorecon.setRequestProperty("Authorization", "Basic " + getBasicAuthorizationString());
            }
            InputStream content = url_underscorecon.getInputStream();
            InputSource insource = new InputSource(content);
            parser.parse(insource);
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Document annodoc = parser.getDocument();
        return annodoc.getDocumentElement();
    }


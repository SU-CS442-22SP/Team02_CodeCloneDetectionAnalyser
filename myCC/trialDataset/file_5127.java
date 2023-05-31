    public Service findServiceFor(final int serviceID) throws JAXBException, IOException, BadResponseException {
        final String USER_underscoreAGENT = "SBSIVisual (CSBE, University of Edinburgh)";
        String urlToConnectTo = "http://www.biocatalogue.org/services/" + serviceID;
        URL url = new URL(urlToConnectTo);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_underscoreAGENT);
        conn.setRequestProperty("Accept", "application/xml");
        int iResponseCode = conn.getResponseCode();
        InputStream serverResponse = null;
        switch(iResponseCode) {
            case HttpURLConnection.HTTP_underscoreOK:
                serverResponse = conn.getInputStream();
                break;
            case HttpURLConnection.HTTP_underscoreBAD_underscoreREQUEST:
                throw new BadResponseException("Received BadResponse from server:" + HttpURLConnection.HTTP_underscoreBAD_underscoreREQUEST);
        }
        Service service = new ResponseParser<Service>().getObjectFor(serverResponse, Service.class);
        return service;
    }


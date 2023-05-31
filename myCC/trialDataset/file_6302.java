    private void fetchAlignment() throws IOException {
        String urlString = BASE_underscoreURL + ALN_underscoreURL + DATASET_underscoreURL + "&family=" + mFamily;
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        processAlignment(in);
    }


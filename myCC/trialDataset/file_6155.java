    public Reader getReader() throws Exception {
        if (url_underscorebase == null) {
            return new FileReader(file);
        } else {
            URL url = new URL(url_underscorebase + file.getName());
            return new InputStreamReader(url.openConnection().getInputStream());
        }
    }


    public static void readDefault() {
        ClassLoader l = Skeleton.class.getClassLoader();
        URL url;
        if (l != null) {
            url = l.getResource(DEFAULT_underscoreLOC);
        } else {
            url = ClassLoader.getSystemResource(DEFAULT_underscoreLOC);
        }
        if (url == null) {
            Out.error(ErrorMessages.SKEL_underscoreIO_underscoreERROR_underscoreDEFAULT);
            throw new GeneratorException();
        }
        try {
            InputStreamReader reader = new InputStreamReader(url.openStream());
            readSkel(new BufferedReader(reader));
        } catch (IOException e) {
            Out.error(ErrorMessages.SKEL_underscoreIO_underscoreERROR_underscoreDEFAULT);
            throw new GeneratorException();
        }
    }


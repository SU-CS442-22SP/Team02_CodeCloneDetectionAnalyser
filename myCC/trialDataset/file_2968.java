    public String readBaseLib() throws Exception {
        if (_underscoreBASE_underscoreLIB_underscoreJS == null) {
            StringBuffer js = new StringBuffer();
            try {
                URL url = AbstractRunner.class.getResource(_underscoreBASELIB_underscoreFILENAME);
                if (url != null) {
                    InputStream is = url.openStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader bfReader = new BufferedReader(reader);
                    String tmp = null;
                    do {
                        tmp = bfReader.readLine();
                        if (tmp != null) {
                            js.append(tmp).append('\n');
                        }
                    } while (tmp != null);
                    bfReader.close();
                    reader.close();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            _underscoreBASE_underscoreLIB_underscoreJS = js.toString();
        }
        return _underscoreBASE_underscoreLIB_underscoreJS;
    }


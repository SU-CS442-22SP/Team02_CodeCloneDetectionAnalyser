    private void loadOperatorsXML() {
        startwindow.setMessage("Loading Operators...");
        try {
            URL url = Application.class.getClassLoader().getResource(Resources.getString("OPERATORS_underscoreXML"));
            InputStream input = url.openStream();
            OperatorsReader.registerOperators(Resources.getString("OPERATORS_underscoreXML"), input);
        } catch (FileNotFoundException e) {
            Logger.logException("File '" + Resources.getString("OPERATORS_underscoreXML") + "' not found.", e);
        } catch (IOException error) {
            Logger.logException(error.getMessage(), error);
        }
    }


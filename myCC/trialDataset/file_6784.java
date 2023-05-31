    public void schema(final Row row, TestResults testResults) throws Exception {
        String urlString = row.text(1);
        String schemaBase = null;
        if (row.cellExists(2)) {
            schemaBase = row.text(2);
        }
        try {
            StreamSource schemaSource;
            if (urlString.startsWith(CLASS_underscorePREFIX)) {
                InputStream schema = XmlValidator.class.getClassLoader().getResourceAsStream(urlString.substring(CLASS_underscorePREFIX.length()));
                schemaSource = new StreamSource(schema);
            } else {
                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                schemaSource = new StreamSource(inputStream);
            }
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_underscoreXML_underscoreSCHEMA_underscoreNS_underscoreURI);
            if (schemaBase != null) {
                DefaultLSResourceResolver resolver = new DefaultLSResourceResolver(schemaBase);
                factory.setResourceResolver(resolver);
            }
            factory.newSchema(new URL(urlString));
            Validator validator = factory.newSchema(schemaSource).newValidator();
            StreamSource source = new StreamSource(new StringReader(xml));
            validator.validate(source);
            row.pass(testResults);
        } catch (SAXException e) {
            Loggers.SERVICE_underscoreLOG.warn("schema error", e);
            throw new FitFailureException(e.getMessage());
        } catch (IOException e) {
            Loggers.SERVICE_underscoreLOG.warn("schema error", e);
            throw new FitFailureException(e.getMessage());
        }
    }


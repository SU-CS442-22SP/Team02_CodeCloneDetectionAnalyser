    private void loadInitialDbState() throws IOException {
        InputStream in = SchemaAndDataPopulator.class.getClassLoader().getResourceAsStream(resourceName);
        StringWriter writer = new StringWriter();
        IOUtils.copy(in, writer);
        for (String statement : writer.toString().split(SQL_underscoreSTATEMENT_underscoreDELIMITER)) {
            logger.info("Executing SQL Statement {}", statement);
            template.execute(statement);
        }
    }


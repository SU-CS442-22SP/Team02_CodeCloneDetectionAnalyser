    public boolean parseResults(URL url, String analysis_underscoretype, CurationI curation, Date analysis_underscoredate, String regexp) throws OutputMalFormatException {
        boolean parsed = false;
        try {
            InputStream data_underscorestream = url.openStream();
            parsed = parseResults(data_underscorestream, analysis_underscoretype, curation, analysis_underscoredate, regexp);
        } catch (OutputMalFormatException ex) {
            throw new OutputMalFormatException(ex.getMessage(), ex);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return parsed;
    }


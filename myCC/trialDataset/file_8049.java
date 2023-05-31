    @Override
    public DataTable generateDataTable(Query query, HttpServletRequest request) throws DataSourceException {
        String url = request.getParameter(URL_underscorePARAM_underscoreNAME);
        if (StringUtils.isEmpty(url)) {
            log.error("url parameter not provided.");
            throw new DataSourceException(ReasonType.INVALID_underscoreREQUEST, "url parameter not provided");
        }
        Reader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        } catch (MalformedURLException e) {
            log.error("url is malformed: " + url);
            throw new DataSourceException(ReasonType.INVALID_underscoreREQUEST, "url is malformed: " + url);
        } catch (IOException e) {
            log.error("Couldn't read from url: " + url, e);
            throw new DataSourceException(ReasonType.INVALID_underscoreREQUEST, "Couldn't read from url: " + url);
        }
        DataTable dataTable = null;
        ULocale requestLocale = DataSourceHelper.getLocaleFromRequest(request);
        try {
            dataTable = CsvDataSourceHelper.read(reader, null, true, requestLocale);
        } catch (IOException e) {
            log.error("Couldn't read from url: " + url, e);
            throw new DataSourceException(ReasonType.INVALID_underscoreREQUEST, "Couldn't read from url: " + url);
        }
        return dataTable;
    }


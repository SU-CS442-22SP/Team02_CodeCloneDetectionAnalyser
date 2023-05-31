    private static List retrieveQuotes(Report report, Symbol symbol, String prefix, TradingDate startDate, TradingDate endDate) throws ImportExportException {
        List quotes = new ArrayList();
        String URLString = constructURL(symbol, prefix, startDate, endDate);
        EODQuoteFilter filter = new GoogleEODQuoteFilter(symbol);
        PreferencesManager.ProxyPreferences proxyPreferences = PreferencesManager.getProxySettings();
        try {
            URL url = new URL(URLString);
            InputStreamReader input = new InputStreamReader(url.openStream());
            BufferedReader bufferedInput = new BufferedReader(input);
            String line = bufferedInput.readLine();
            while (line != null) {
                line = bufferedInput.readLine();
                if (line != null) {
                    try {
                        EODQuote quote = filter.toEODQuote(line);
                        quotes.add(quote);
                        verify(report, quote);
                    } catch (QuoteFormatException e) {
                        report.addError(Locale.getString("GOOGLE_underscoreDISPLAY_underscoreURL") + ":" + symbol + ":" + Locale.getString("ERROR") + ": " + e.getMessage());
                    }
                }
            }
            bufferedInput.close();
        } catch (BindException e) {
            throw new ImportExportException(Locale.getString("UNABLE_underscoreTO_underscoreCONNECT_underscoreERROR", e.getMessage()));
        } catch (ConnectException e) {
            throw new ImportExportException(Locale.getString("UNABLE_underscoreTO_underscoreCONNECT_underscoreERROR", e.getMessage()));
        } catch (UnknownHostException e) {
            throw new ImportExportException(Locale.getString("UNKNOWN_underscoreHOST_underscoreERROR", e.getMessage()));
        } catch (NoRouteToHostException e) {
            throw new ImportExportException(Locale.getString("DESTINATION_underscoreUNREACHABLE_underscoreERROR", e.getMessage()));
        } catch (MalformedURLException e) {
            throw new ImportExportException(Locale.getString("INVALID_underscorePROXY_underscoreERROR", proxyPreferences.host, proxyPreferences.port));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new ImportExportException(Locale.getString("ERROR_underscoreDOWNLOADING_underscoreQUOTES"));
        }
        return quotes;
    }


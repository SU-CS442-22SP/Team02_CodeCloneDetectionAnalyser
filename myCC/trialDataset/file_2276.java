    public static List importSymbols(List symbols) throws ImportExportException {
        List quotes = new ArrayList();
        String URLString = constructURL(symbols);
        IDQuoteFilter filter = new YahooIDQuoteFilter();
        PreferencesManager.ProxyPreferences proxyPreferences = PreferencesManager.loadProxySettings();
        try {
            URL url = new URL(URLString);
            InputStreamReader input = new InputStreamReader(url.openStream());
            BufferedReader bufferedInput = new BufferedReader(input);
            String line;
            do {
                line = bufferedInput.readLine();
                if (line != null) {
                    try {
                        IDQuote quote = filter.toIDQuote(line);
                        quote.verify();
                        quotes.add(quote);
                    } catch (QuoteFormatException e) {
                    }
                }
            } while (line != null);
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
            throw new ImportExportException(Locale.getString("ERROR_underscoreDOWNLOADING_underscoreQUOTES"));
        } catch (IOException e) {
            throw new ImportExportException(Locale.getString("ERROR_underscoreDOWNLOADING_underscoreQUOTES"));
        }
        return quotes;
    }


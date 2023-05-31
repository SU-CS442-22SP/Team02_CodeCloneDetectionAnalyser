    public static List importDate(Report report, TradingDate date) throws ImportExportException {
        List quotes = new ArrayList();
        String urlString = constructURL(date);
        EODQuoteFilter filter = new MetaStockQuoteFilter();
        PreferencesManager.ProxyPreferences proxyPreferences = PreferencesManager.getProxySettings();
        try {
            URL url = new URL(urlString);
            InputStreamReader input = new InputStreamReader(url.openStream());
            BufferedReader bufferedInput = new BufferedReader(input);
            String line = null;
            do {
                line = bufferedInput.readLine();
                if (line != null) {
                    try {
                        EODQuote quote = filter.toEODQuote(line);
                        quotes.add(quote);
                        verify(report, quote);
                    } catch (QuoteFormatException e) {
                        report.addError(Locale.getString("DFLOAT_underscoreDISPLAY_underscoreURL") + ":" + date + ":" + Locale.getString("ERROR") + ": " + e.getMessage());
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
            report.addError(Locale.getString("FLOAT_underscoreDISPLAY_underscoreURL") + ":" + date + ":" + Locale.getString("ERROR") + ": " + Locale.getString("NO_underscoreQUOTES_underscoreFOUND"));
        } catch (IOException e) {
            throw new ImportExportException(Locale.getString("ERROR_underscoreDOWNLOADING_underscoreQUOTES"));
        }
        return quotes;
    }


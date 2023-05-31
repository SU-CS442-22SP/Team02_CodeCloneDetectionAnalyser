    public static List importSymbol(Report report, Symbol symbol, TradingDate startDate, TradingDate endDate) throws IOException {
        List quotes = new ArrayList();
        String URLString = constructURL(symbol, startDate, endDate);
        QuoteFilter filter = new YahooQuoteFilter(symbol);
        PreferencesManager.ProxyPreferences proxyPreferences = PreferencesManager.loadProxySettings();
        try {
            URL url = new URL(URLString);
            InputStreamReader input = new InputStreamReader(url.openStream());
            BufferedReader bufferedInput = new BufferedReader(input);
            String line = bufferedInput.readLine();
            while (line != null) {
                line = bufferedInput.readLine();
                if (line != null) {
                    try {
                        Quote quote = filter.toQuote(line);
                        quotes.add(quote);
                        verify(report, quote);
                    } catch (QuoteFormatException e) {
                        report.addError(Locale.getString("YAHOO") + ":" + symbol + ":" + Locale.getString("ERROR") + ": " + e.getMessage());
                    }
                }
            }
            bufferedInput.close();
        } catch (BindException e) {
            DesktopManager.showErrorMessage(Locale.getString("UNABLE_underscoreTO_underscoreCONNECT_underscoreERROR", e.getMessage()));
            throw new IOException();
        } catch (ConnectException e) {
            DesktopManager.showErrorMessage(Locale.getString("UNABLE_underscoreTO_underscoreCONNECT_underscoreERROR", e.getMessage()));
            throw new IOException();
        } catch (UnknownHostException e) {
            DesktopManager.showErrorMessage(Locale.getString("UNKNOWN_underscoreHOST_underscoreERROR", e.getMessage()));
            throw new IOException();
        } catch (NoRouteToHostException e) {
            DesktopManager.showErrorMessage(Locale.getString("DESTINATION_underscoreUNREACHABLE_underscoreERROR", e.getMessage()));
            throw new IOException();
        } catch (MalformedURLException e) {
            DesktopManager.showErrorMessage(Locale.getString("INVALID_underscorePROXY_underscoreERROR", proxyPreferences.host, proxyPreferences.port));
            throw new IOException();
        } catch (FileNotFoundException e) {
            report.addError(Locale.getString("YAHOO") + ":" + symbol + ":" + Locale.getString("ERROR") + ": " + Locale.getString("NO_underscoreQUOTES_underscoreFOUND"));
        } catch (IOException e) {
            DesktopManager.showErrorMessage(Locale.getString("ERROR_underscoreDOWNLOADING_underscoreQUOTES"));
            throw new IOException();
        }
        return quotes;
    }


    public String getScenarioData(String urlForSalesData) throws IOException, Exception {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        DataInputStream input = null;
        StringBuffer sBuf = new StringBuffer();
        Proxy proxy;
        if (httpWebProxyServer != null && Integer.toString(httpWebProxyPort) != null) {
            SocketAddress address = new InetSocketAddress(httpWebProxyServer, httpWebProxyPort);
            proxy = new Proxy(Proxy.Type.HTTP, address);
        } else {
            proxy = null;
        }
        proxy = null;
        URL url;
        try {
            url = new URL(urlForSalesData);
            HttpURLConnection httpUrlConnection;
            if (proxy != null) httpUrlConnection = (HttpURLConnection) url.openConnection(proxy); else httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("GET");
            String name = rb.getString("WRAP_underscoreNAME");
            String password = rb.getString("WRAP_underscorePASSWORD");
            Credentials simpleAuthCrentials = new Credentials(TOKEN_underscoreTYPE.SimpleApiAuthToken, name, password);
            ACSTokenProvider tokenProvider = new ACSTokenProvider(httpWebProxyServer, httpWebProxyPort, simpleAuthCrentials);
            String requestUriStr1 = "https://" + solutionName + "." + acmHostName + "/" + serviceName;
            String appliesTo1 = rb.getString("SIMPLEAPI_underscoreAPPLIES_underscoreTO");
            String token = tokenProvider.getACSToken(requestUriStr1, appliesTo1);
            httpUrlConnection.addRequestProperty("token", "WRAPv0.9 " + token);
            httpUrlConnection.addRequestProperty("solutionName", solutionName);
            httpUrlConnection.connect();
            if (httpUrlConnection.getResponseCode() == HttpServletResponse.SC_underscoreUNAUTHORIZED) {
                List<TestSalesOrderService> salesOrderServiceBean = new ArrayList<TestSalesOrderService>();
                TestSalesOrderService response = new TestSalesOrderService();
                response.setResponseCode(HttpServletResponse.SC_underscoreUNAUTHORIZED);
                response.setResponseMessage(httpUrlConnection.getResponseMessage());
                salesOrderServiceBean.add(response);
            }
            inputStream = httpUrlConnection.getInputStream();
            input = new DataInputStream(inputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(input));
            String str;
            while (null != ((str = bufferedReader.readLine()))) {
                sBuf.append(str);
            }
            String responseString = sBuf.toString();
            return responseString;
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }


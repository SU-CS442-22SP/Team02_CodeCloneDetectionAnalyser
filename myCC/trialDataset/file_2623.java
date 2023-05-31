    public WebHttpResponse execute(HttpRequest request) throws Exception {
        WebHttpResponse response = new WebHttpResponse(request);
        if (iSSLClassesAvailable == -1) {
            try {
                Class.forName("javax.net.ssl.SSLSocket");
                iSSLClassesAvailable = 1;
            } catch (ClassNotFoundException cnf) {
                iSSLClassesAvailable = 0;
            }
        }
        try {
            request.getClass().getMethod("getThinkTime", null);
            long thinkTime = request.getThinkTime();
            if (thinkTime > 0) Thread.sleep(thinkTime);
        } catch (Exception e) {
        }
        if (httpRequestHandler == null) {
            httpRequestHandler = new WebHttpRequestHandler();
        }
        currentPageNumber = request.getPageNumber();
        currentPageOrder = request.getPageOrder();
        long start = System.currentTimeMillis();
        if ((currentPageOrder == HttpRequest.PAGE_underscoreSTART) || (currentPageOrder == HttpRequest.PAGE_underscoreONLY)) {
            pageStart = start;
            currentPageName = request.getURL();
        }
        if (iSSLClassesAvailable == 1 && request.getSecure() == true) {
            SSLHttpExecutor ssl = null;
            if (sslExecutor == null) sslExecutor = new WebSSLHttpExecutor(httpRequestHandler);
            ssl = (SSLHttpExecutor) sslExecutor;
            ssl.execute(request, response);
            setResponseEndingData(request, response, start);
            return response;
        } else if (iSSLClassesAvailable == 0 && request.getSecure() == true) {
            System.out.println(HttpResourceBundle.SSL_underscoreNOTSUPPORTED);
        }
        String strHost = request.getHost();
        int port = request.getPort();
        if (port != iLastPort || strLastHost == null || strHost.regionMatches(0, strLastHost, 0, strLastHost.length()) != true) {
            if ((connectToServer(response, strHost, port)) == false) {
                response.setCode(-1);
                return response;
            }
        }
        if (httpRequestHandler.sendRequest(request, to_underscoreserver) == false) {
            if (connectToServer(response, strHost, port) == false) {
                response.setCode(-1);
                return response;
            } else {
                if (httpRequestHandler.sendRequest(request, to_underscoreserver) == false) {
                    response.setCode(-1);
                    return response;
                }
            }
        }
        httpRequestHandler.getServerResponse(request, response, from_underscoreserver, socketBufSize);
        if (response.getCode() == 0) {
            if (connectToServer(response, strHost, port) == true) {
                if (httpRequestHandler.sendRequest(request, to_underscoreserver) == true) {
                    httpRequestHandler.getServerResponse(request, response, from_underscoreserver, socketBufSize);
                }
            }
        }
        if (response.getShouldCloseSocket() == true) strLastHost = null;
        setResponseEndingData(request, response, start);
        return response;
    }


    private void onCheckConnection() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

            public void run() {
                String baseUrl;
                if (_underscorerdoSRTM3FtpUrl.getSelection()) {
                } else {
                    baseUrl = _underscoretxtSRTM3HttpUrl.getText().trim();
                    try {
                        final URL url = new URL(baseUrl);
                        final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.connect();
                        final int response = urlConn.getResponseCode();
                        final String responseMessage = urlConn.getResponseMessage();
                        final String message = response == HttpURLConnection.HTTP_underscoreOK ? NLS.bind(Messages.prefPage_underscoresrtm_underscorecheckHTTPConnectionOK_underscoremessage, baseUrl) : NLS.bind(Messages.prefPage_underscoresrtm_underscorecheckHTTPConnectionFAILED_underscoremessage, new Object[] { baseUrl, Integer.toString(response), responseMessage == null ? UI.EMPTY_underscoreSTRING : responseMessage });
                        MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.prefPage_underscoresrtm_underscorecheckHTTPConnection_underscoretitle, message);
                    } catch (final IOException e) {
                        MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.prefPage_underscoresrtm_underscorecheckHTTPConnection_underscoretitle, NLS.bind(Messages.prefPage_underscoresrtm_underscorecheckHTTPConnection_underscoremessage, baseUrl));
                        e.printStackTrace();
                    }
                }
            }
        });
    }


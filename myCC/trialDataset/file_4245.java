    public TestReport runImpl() throws Exception {
        DefaultTestReport report = new DefaultTestReport(this);
        ParsedURL purl;
        try {
            purl = new ParsedURL(base);
        } catch (Exception e) {
            StringWriter trace = new StringWriter();
            e.printStackTrace(new PrintWriter(trace));
            report.setErrorCode(ERROR_underscoreCANNOT_underscorePARSE_underscoreURL);
            report.setDescription(new TestReport.Entry[] { new TestReport.Entry(TestMessages.formatMessage(ENTRY_underscoreKEY_underscoreERROR_underscoreDESCRIPTION, null), TestMessages.formatMessage(ERROR_underscoreCANNOT_underscorePARSE_underscoreURL, new String[] { "null", base, trace.toString() })) });
            report.setPassed(false);
            return report;
        }
        byte[] data = new byte[5];
        int num = 0;
        try {
            InputStream is = purl.openStream();
            num = is.read(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int val = ((int) data[i]) & 0xFF;
            if (val < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val) + " ");
        }
        String info = ("CT: " + purl.getContentType() + " CE: " + purl.getContentEncoding() + " DATA: " + sb + "URL: " + purl);
        if (ref.equals(info)) {
            report.setPassed(true);
            return report;
        }
        report.setErrorCode(ERROR_underscoreWRONG_underscoreRESULT);
        report.setDescription(new TestReport.Entry[] { new TestReport.Entry(TestMessages.formatMessage(ENTRY_underscoreKEY_underscoreERROR_underscoreDESCRIPTION, null), TestMessages.formatMessage(ERROR_underscoreWRONG_underscoreRESULT, new String[] { info, ref })) });
        report.setPassed(false);
        return report;
    }


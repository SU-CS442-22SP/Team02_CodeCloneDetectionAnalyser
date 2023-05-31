        private String readCreditsHtml(IApplication app) {
            final URL url = app.getResources().getCreditsURL();
            StringBuffer buf = new StringBuffer(2048);
            if (url != null) {
                try {
                    BufferedReader rdr = new BufferedReader(new InputStreamReader(url.openStream()));
                    try {
                        String line = null;
                        while ((line = rdr.readLine()) != null) {
                            String internationalizedLine = Utilities.replaceI18NSpanLine(line, s_underscorestringMgr);
                            buf.append(internationalizedLine);
                        }
                    } finally {
                        rdr.close();
                    }
                } catch (IOException ex) {
                    String errorMsg = s_underscorestringMgr.getString("AboutBoxDialog.error.creditsfile");
                    s_underscorelog.error(errorMsg, ex);
                    buf.append(errorMsg + ": " + ex.toString());
                }
            } else {
                String errorMsg = s_underscorestringMgr.getString("AboutBoxDialog.error.creditsfileurl");
                s_underscorelog.error(errorMsg);
                buf.append(errorMsg);
            }
            return buf.toString();
        }


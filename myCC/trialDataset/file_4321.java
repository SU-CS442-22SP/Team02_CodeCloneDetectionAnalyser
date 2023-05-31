    private String createHTML(PAGE_underscoreTYPE requestPage) {
        String result = "<html><head>";
        URL url = getClass().getClassLoader().getResource("org/compiere/images/PAPanel.css");
        InputStreamReader ins;
        try {
            ins = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(ins);
            String cssLine;
            while ((cssLine = bufferedReader.readLine()) != null) result += cssLine + "\n";
        } catch (IOException e1) {
            log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
        }
        switch(requestPage) {
            case PAGE_underscoreLOGO:
                result += "</head><body class=\"header\">" + "<table width=\"100%\"><tr><td>" + "<img src=\"res:org/compiere/images/logo_underscoread.png\">" + "</td><td></td><td width=\"290\">" + "</td></tr></table>" + "</body></html>";
                break;
            case PAGE_underscoreHOME:
                result += "</head><body><div class=\"content\">\n";
                queryZoom = null;
                queryZoom = new ArrayList<MQuery>();
                String appendToHome = null;
                String sql = " SELECT x.AD_underscoreCLIENT_underscoreID, x.NAME, x.DESCRIPTION, x.AD_underscoreWINDOW_underscoreID, x.PA_underscoreGOAL_underscoreID, x.LINE, x.HTML, m.AD_underscoreMENU_underscoreID" + " FROM PA_underscoreDASHBOARDCONTENT x" + " LEFT OUTER JOIN AD_underscoreMENU m ON x.ad_underscorewindow_underscoreid=m.ad_underscorewindow_underscoreid" + " WHERE (x.AD_underscoreClient_underscoreID=0 OR x.AD_underscoreClient_underscoreID=?) AND x.IsActive='Y'" + " ORDER BY LINE";
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                try {
                    pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, Env.getAD_underscoreClient_underscoreID(Env.getCtx()));
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        appendToHome = rs.getString("HTML");
                        if (appendToHome != null) {
                            if (rs.getString("DESCRIPTION") != null) result += "<H2>" + rs.getString("DESCRIPTION") + "</H2>\n";
                            result += stripHtml(appendToHome, false) + "<br>\n";
                        }
                        if (rs.getInt("AD_underscoreMENU_underscoreID") > 0) {
                            result += "<a class=\"hrefNode\" href=\"http:///window/node#" + String.valueOf(rs.getInt("AD_underscoreWINDOW_underscoreID") + "\">" + rs.getString("DESCRIPTION") + "</a><br>\n");
                        }
                        result += "<br>\n";
                        if (rs.getInt("PA_underscoreGOAL_underscoreID") > 0) result += goalsDetail(rs.getInt("PA_underscoreGOAL_underscoreID"));
                    }
                } catch (SQLException e) {
                    log.log(Level.SEVERE, sql, e);
                } finally {
                    DB.close(rs, pstmt);
                    rs = null;
                    pstmt = null;
                }
                result += "<br><br><br>\n" + "</div>\n</body>\n</html>\n";
                break;
            default:
                log.warning("Unknown option - " + requestPage);
        }
        return result;
    }


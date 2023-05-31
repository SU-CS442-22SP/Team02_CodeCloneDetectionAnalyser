    protected InputStream createIconType(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        JavaliController.debug(JavaliController.LG_underscoreVERBOSE, "Creating iconType");
        String cHash = PRM_underscoreTYPE + "=" + TP_underscoreICON;
        String iconName = req.getParameter("iconName");
        if (iconName == null) {
            res.sendError(res.SC_underscoreNOT_underscoreFOUND);
            return null;
        }
        Locale loc = null;
        HttpSession sess = req.getSession(false);
        JavaliSession jsess = null;
        int menuType = -1;
        String menuTypeString = req.getParameter(PRM_underscoreMENU_underscoreTYPE);
        try {
            menuType = new Integer(menuTypeString).intValue();
        } catch (Exception e) {
        }
        if (sess != null) jsess = (JavaliSession) sess.getAttribute(FormConstants.SESSION_underscoreBINDING);
        if (jsess != null && jsess.getUser() != null) loc = jsess.getUser().getLocale(); else if (sess != null) loc = (Locale) sess.getAttribute(FormConstants.LOCALE_underscoreBINDING);
        if (loc == null) loc = Locale.getDefault();
        if (menuType == -1) menuType = MENU_underscoreTYPE_underscoreTEXTICON;
        String iconText = JavaliResource.getString("icon." + iconName, loc);
        if (iconText == null) {
            iconText = req.getParameter(PRM_underscoreMENU_underscoreNAME);
            if (iconText == null) iconText = "";
        }
        cHash += ", " + PRM_underscoreICON_underscoreNAME + "=" + iconName + ", text=" + iconText + ", menuType=" + menuType;
        String iconFileName = null;
        String fontName = req.getParameter(PRM_underscoreFONT_underscoreNAME);
        if (fontName == null) {
            fontName = "Helvetica";
        }
        cHash += "," + PRM_underscoreFONT_underscoreNAME + "=" + fontName;
        String fontSizeString = req.getParameter(PRM_underscoreFONT_underscoreSIZE);
        int fontSize;
        try {
            fontSize = Integer.parseInt(fontSizeString);
        } catch (NumberFormatException nfe) {
            fontSize = 12;
        }
        cHash += "," + PRM_underscoreFONT_underscoreSIZE + "=" + fontSize;
        String fontTypeString = req.getParameter(PRM_underscoreFONT_underscoreTYPE);
        int fontType = Font.BOLD;
        if ("PLAIN".equalsIgnoreCase(fontTypeString)) fontType = Font.PLAIN;
        if ("BOLD".equalsIgnoreCase(fontTypeString)) fontType = Font.BOLD;
        if ("ITALIC".equalsIgnoreCase(fontTypeString)) fontType = Font.ITALIC;
        if ("ITALICBOLD".equalsIgnoreCase(fontTypeString) || "BOLDITALIC".equalsIgnoreCase(fontTypeString) || "BOLD_underscoreITALIC".equalsIgnoreCase(fontTypeString) || "ITALIC_underscoreBOLD".equalsIgnoreCase(fontTypeString)) {
            fontType = Font.ITALIC | Font.BOLD;
        }
        cHash += "," + PRM_underscoreFONT_underscoreTYPE + "=" + fontType;
        String fontColor = req.getParameter(PRM_underscoreFONT_underscoreCOLOR);
        if (fontColor == null || fontColor.equals("")) fontColor = "0x000000";
        cHash += "," + PRM_underscoreFONT_underscoreCOLOR + "=" + fontColor;
        String fName = cacheInfo.file(cHash);
        JavaliController.debug(JavaliController.LG_underscoreVERBOSE, "Called for: " + fName);
        if (fName == null) {
            JavaliController.debug(JavaliController.LG_underscoreVERBOSE, "No cache found for: " + cHash);
            if (getServletConfig() != null && getServletConfig().getServletContext() != null) {
                if (iconName != null && iconName.startsWith("/")) iconFileName = getServletConfig().getServletContext().getRealPath(iconName + ".gif"); else iconFileName = getServletConfig().getServletContext().getRealPath("/icons/" + iconName + ".gif");
                File iconFile = new File(iconFileName);
                if (!iconFile.exists()) {
                    JavaliController.debug(JavaliController.LG_underscoreVERBOSE, "Could not find: " + iconFileName);
                    res.sendError(res.SC_underscoreNOT_underscoreFOUND);
                    return null;
                }
                iconFileName = iconFile.getAbsolutePath();
                JavaliController.debug(JavaliController.LG_underscoreVERBOSE, "file: " + iconFileName + " and cHash=" + cHash);
            } else {
                JavaliController.debug(JavaliController.LG_underscoreVERBOSE, "No ServletConfig=" + getServletConfig() + " or servletContext");
                res.sendError(res.SC_underscoreNOT_underscoreFOUND);
                return null;
            }
            File tmp = File.createTempFile(PREFIX, SUFIX, cacheDir);
            OutputStream out = new FileOutputStream(tmp);
            if (menuType == MENU_underscoreTYPE_underscoreICON) {
                FileInputStream in = new FileInputStream(iconFileName);
                byte buf[] = new byte[2048];
                int read = -1;
                while ((read = in.read(buf)) != -1) out.write(buf, 0, read);
            } else if (menuType == MENU_underscoreTYPE_underscoreTEXT) MessageImage.sendAsGIF(MessageImage.makeMessageImage(iconText, fontName, fontSize, fontType, fontColor, false, "0x000000", true), out); else MessageImage.sendAsGIF(MessageImage.makeIconImage(iconFileName, iconText, fontName, fontColor, fontSize, fontType), out);
            out.close();
            cacheInfo.putFile(cHash, tmp);
            fName = cacheInfo.file(cHash);
        }
        return new FileInputStream(new File(cacheDir, fName));
    }


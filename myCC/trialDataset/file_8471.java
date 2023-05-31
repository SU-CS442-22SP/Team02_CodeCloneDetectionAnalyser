    boolean isTextPage(URL url) {
        try {
            String ct = url.openConnection().getContentType().toLowerCase();
            String s = url.toString();
            Loro.log("LoroEDI: " + "  content-type: " + ct);
            if (!ct.startsWith("text/") || s.endsWith(".jar") || s.endsWith(".lar")) {
                javax.swing.JOptionPane.showOptionDialog(null, Str.get("gui.1_underscorebrowser_underscorecannot_underscoreshow_underscorelink", s), "", javax.swing.JOptionPane.DEFAULT_underscoreOPTION, javax.swing.JOptionPane.WARNING_underscoreMESSAGE, null, null, null);
                Loro.log("LoroEDI: " + "  unable to display");
                return false;
            }
        } catch (Exception ex) {
            Loro.log("LoroEDI: " + "  Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }


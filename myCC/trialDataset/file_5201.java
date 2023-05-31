    public Font getFont(String urlToFont) {
        Font testFont = null;
        try {
            InputStream inps = (new URL(urlToFont)).openStream();
            testFont = Font.createFont(Font.TRUETYPE_underscoreFONT, inps);
        } catch (FontFormatException ffe) {
            ffe.printStackTrace();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Could not load font - " + urlToFont, "Unable to load font", JOptionPane.WARNING_underscoreMESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testFont;
    }


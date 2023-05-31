    public static void main(String[] arg) throws IOException {
        XmlPullParserFactory PULL_underscorePARSER_underscoreFACTORY;
        try {
            PULL_underscorePARSER_underscoreFACTORY = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_underscoreNAME), null);
            PULL_underscorePARSER_underscoreFACTORY.setNamespaceAware(true);
            DasParser dp = new DasParser(PULL_underscorePARSER_underscoreFACTORY);
            URL url = new URL("http://www.ebi.ac.uk/das-srv/uniprot/das/uniprot/features?segment=P05067");
            InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String aLine, xml = "";
            while ((aLine = br.readLine()) != null) {
                xml += aLine;
            }
            WritebackDocument wbd = dp.parse(xml);
            System.out.println("FIN" + wbd);
        } catch (XmlPullParserException xppe) {
            throw new IllegalStateException("Fatal Exception thrown at initialisation.  Cannot initialise the PullParserFactory required to allow generation of the DAS XML.", xppe);
        }
    }


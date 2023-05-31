    public void Load(String fname) throws Exception {
        File f = null;
        try {
            if ("".equals(fname) || fname == null) throw new Exception();
            System.out.println("Loading mapfile " + fname);
        } catch (Exception e) {
            throw new Exception("File not found");
        }
        aType = null;
        fieldtype.clear();
        creatures.clear();
        aElement = new String("");
        content = null;
        Ax = -1;
        Ay = -1;
        aTemplate = -1;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        data_underscoreread = 0;
        URL url = this.game.mainClass.getClassLoader().getResource(fname);
        if (url == null) {
            throw new Exception("Can't load map from : " + fname);
        }
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(url.openStream(), this);
        } catch (Exception e) {
            System.out.println("Can't open XML : " + e);
        }
        for (int i = 0; i < fieldtype.size(); i++) {
            System.out.println((MapField) fieldtype.get(i));
        }
        game.player.setpos(start_underscorex, start_underscorey);
        System.out.println("Player starting position set");
        start_underscorex = -1;
        start_underscorey = -1;
        System.out.println("Map \"" + fname + "\" loaded");
    }


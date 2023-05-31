    public static void translateTableAttributes(String baseDir, String tableName, NameSpaceDefinition nsDefinition) throws Exception {
        setVosiNS(baseDir, "table_underscoreatt", nsDefinition);
        String filename = baseDir + "table_underscoreatt.xsl";
        Scanner s = new Scanner(new File(filename));
        PrintWriter fw = new PrintWriter(new File(baseDir + tableName + "_underscoreatt.xsl"));
        while (s.hasNextLine()) {
            fw.println(s.nextLine().replaceAll("TABLENAME", tableName));
        }
        s.close();
        fw.close();
        applyStyle(baseDir + "tables.xml", baseDir + tableName + "_underscoreatt.json", baseDir + tableName + "_underscoreatt.xsl");
    }


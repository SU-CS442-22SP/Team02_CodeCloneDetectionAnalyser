    static void getGroups(String username) {
        try {
            Gui.getBalken().setValue(85);
            Gui.getBalken().setString("crawling Groups");
            Gui.getBalken().paint(Gui.getBalken().getGraphics());
            URL url = new URL("http://www.lastfm.de/user/" + username + "/groups/");
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            HTMLEditorKit editorKit = new HTMLEditorKit();
            HTMLDocument htmlDoc = new HTMLDocument();
            htmlDoc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
            editorKit.read(br, htmlDoc, 0);
            Vector a_underscoretags = new Vector();
            HTMLDocument.Iterator iter1 = htmlDoc.getIterator(HTML.Tag.A);
            while (iter1.isValid()) {
                a_underscoretags.add((String) iter1.getAttributes().getAttribute(HTML.Attribute.HREF));
                iter1.next();
            }
            Vector gruppen = new Vector();
            for (int i = 0; i < a_underscoretags.size(); i++) {
                String element = (String) a_underscoretags.get(i);
                if (!gruppen.contains(element)) {
                    if (element.contains("/group/")) gruppen.add(element);
                }
            }
            for (int a = 0; a < gruppen.size(); a++) {
                String gruppe = gruppen.elementAt(a).toString().substring(7);
                if (gruppe.contains("Last.fm+auf+Deutsch")) {
                    System.out.println("Auschalten Last.fm.auf.Deutsch");
                } else {
                    System.out.println(gruppe + " gruppe ");
                    if (!DB_underscoreGroups.checkGroup(gruppe)) {
                        System.out.println(gruppe);
                        if (!DB_underscoreGroups.checkGroup(gruppe)) {
                            DB_underscoreGroups.addGroup(gruppe);
                            getGroupsImage(username);
                            getGroupMember(gruppe);
                        }
                        DB_underscoreGroups.addGroupRelation(username, gruppe);
                        getGroupsImage(username);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


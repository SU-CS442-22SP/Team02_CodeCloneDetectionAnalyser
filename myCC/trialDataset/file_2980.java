    public GEItem lookup(final int itemID) {
        try {
            URL url = new URL(GrandExchange.HOST + GrandExchange.GET + itemID);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String input;
            boolean exists = false;
            int i = 0;
            double[] values = new double[4];
            String name = "", examine = "";
            while ((input = br.readLine()) != null) {
                if (input.contains("<div class=\"brown_underscorebox main_underscorege_underscorepage") && !exists) {
                    if (!input.contains("vertically_underscorespaced")) {
                        return null;
                    }
                    exists = true;
                    br.readLine();
                    br.readLine();
                    name = br.readLine();
                } else if (input.contains("<img id=\"item_underscoreimage\" src=\"")) {
                    examine = br.readLine();
                } else if (input.matches("(?i).+ (price|days):</b> .+")) {
                    values[i] = parse(input);
                    i++;
                } else if (input.matches("<div id=\"legend\">")) break;
            }
            return new GEItem(name, examine, itemID, values);
        } catch (IOException ignore) {
        }
        return null;
    }


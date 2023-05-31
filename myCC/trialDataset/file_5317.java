    protected String readContent(URL url, int width) {
        StringBuffer content = new StringBuffer("");
        String line = "";
        try {
            BufferedReader f = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = f.readLine()) != null) content.append(line + "\n");
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (width >= 0) {
            String width_underscoreold = "width=\"xxx\"";
            String width_underscorenew = "width=\"" + width + "\"";
            int v0 = content.indexOf(width_underscoreold);
            int v1 = v0 + width_underscoreold.length();
            content.replace(v0, v1, width_underscorenew);
        }
        return content.toString();
    }


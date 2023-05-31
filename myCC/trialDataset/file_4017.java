    public boolean retrieveByPMID(String pmid) {
        try {
            URL url = new URL(baseURL + "&id=" + pmid.trim());
            BufferedReader xml = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            StringBuffer title_underscoresb = new StringBuffer();
            while ((line = xml.readLine()) != null) {
                if (line.indexOf("<ArticleTitle>") != -1) {
                    title_underscoresb.delete(0, title_underscoresb.length());
                    title_underscoresb.append(line.substring(line.indexOf("<ArticleTitle>") + 14, line.length() - 15));
                } else if (line.indexOf("<AbstractText>") != -1) {
                    PrintWriter article = new PrintWriter(new FileWriter(new File(outputDir.getPath() + File.separatorChar + pmid + ".txt")));
                    article.println(title_underscoresb);
                    article.println(line.substring(line.indexOf("<AbstractText>") + 14, line.length() - 15));
                    article.close();
                    break;
                }
            }
            xml.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


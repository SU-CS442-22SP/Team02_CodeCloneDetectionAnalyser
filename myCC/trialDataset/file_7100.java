    public void issue(String licenseId, Map answers, String lang) throws IOException {
        String issueUrl = this.rest_underscoreroot + "/license/" + licenseId + "/issue";
        String answer_underscoredoc = "<answers>\n<license-" + licenseId + ">";
        Iterator keys = answers.keySet().iterator();
        try {
            String current = (String) keys.next();
            while (true) {
                answer_underscoredoc += "<" + current + ">" + (String) answers.get(current) + "</" + current + ">\n";
                current = (String) keys.next();
            }
        } catch (NoSuchElementException e) {
        }
        answer_underscoredoc += "</license-" + licenseId + ">\n</answers>\n";
        String post_underscoredata;
        try {
            post_underscoredata = URLEncoder.encode("answers", "UTF-8") + "=" + URLEncoder.encode(answer_underscoredoc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return;
        }
        URL post_underscoreurl;
        try {
            post_underscoreurl = new URL(issueUrl);
        } catch (MalformedURLException e) {
            return;
        }
        URLConnection conn = post_underscoreurl.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(post_underscoredata);
        wr.flush();
        try {
            this.license_underscoredoc = this.parser.build(conn.getInputStream());
        } catch (JDOMException e) {
            System.out.print("Danger Will Robinson, Danger!");
        }
        return;
    }


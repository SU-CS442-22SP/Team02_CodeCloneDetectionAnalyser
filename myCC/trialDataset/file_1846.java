    public List<T_underscorenew> executeGet(HttpTransport transport, String targetUrl) throws HttpResponseException, IOException {
        HttpRequest req = transport.buildGetRequest();
        req.setUrl(targetUrl);
        NotifyFeed feed = req.execute().parseAs(NotifyFeed.class);
        if (feed.entry == null) {
            return Collections.emptyList();
        }
        List<T_underscorenew> results = new ArrayList<T_underscorenew>();
        for (NotifyEntry e : feed.entry) {
            StringBuilder buffer = new StringBuilder();
            if (e.id != null) {
                buffer.append(e.id);
            }
            buffer.append("@");
            if (e.updated != null) {
                buffer.append(e.updated.toStringRfc3339().substring(0, 19) + "Z");
            }
            Key key = Datastore.createKey(T_underscorenew.class, buffer.toString());
            T_underscorenew news = new T_underscorenew();
            news.setTitle(e.title);
            if (e.content != null) {
                news.setNewText(e.content.substring(0, Math.min(e.content.length(), 500)));
            }
            if (e.status != null && e.content == null) {
                news.setNewText(e.status);
            }
            if (e.updated != null) {
                news.setCreatedAt(new Date(e.updated.value));
            }
            news.setContentUrl(e.getAlternate());
            if (e.author != null) {
                news.setAuthor(e.author.name);
            }
            news.setKey(key);
            results.add(news);
        }
        return results;
    }


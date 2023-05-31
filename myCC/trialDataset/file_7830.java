    @Override
    protected void parseContent(StreamLimiter streamLimiter, LanguageEnum forcedLang) throws IOException {
        String charset = null;
        IndexDocument sourceDocument = getSourceDocument();
        if (sourceDocument != null && urlItemFieldEnum != null) {
            FieldValueItem fieldValueItem = sourceDocument.getFieldValue(urlItemFieldEnum.contentTypeCharset.getName(), 0);
            if (fieldValueItem != null) charset = fieldValueItem.getValue();
            if (charset == null) {
                fieldValueItem = sourceDocument.getFieldValue(urlItemFieldEnum.contentEncoding.getName(), 0);
                if (fieldValueItem != null) charset = fieldValueItem.getValue();
            }
        }
        boolean charsetWasNull = charset == null;
        if (charsetWasNull) charset = getProperty(ClassPropertyEnum.DEFAULT_underscoreCHARSET).getValue();
        StringWriter writer = new StringWriter();
        IOUtils.copy(streamLimiter.getNewInputStream(), writer, charset);
        addField(ParserFieldEnum.htmlSource, writer.toString());
        writer.close();
        HtmlDocumentProvider htmlProvider = findBestProvider(charset, streamLimiter);
        if (htmlProvider == null) return;
        addField(ParserFieldEnum.htmlProvider, htmlProvider.getName());
        String contentType = htmlProvider.getMetaHttpEquiv("content-type");
        String contentTypeCharset = null;
        if (contentType != null) {
            contentTypeCharset = MimeUtils.extractContentTypeCharset(contentType);
            if (contentTypeCharset != null && !contentTypeCharset.equals(charset)) charsetWasNull = true;
        }
        if (charsetWasNull) {
            if (contentTypeCharset != null) charset = contentTypeCharset; else charset = htmlProvider.getMetaCharset();
            if (charset != null) htmlProvider = findBestProvider(charset, streamLimiter);
        }
        HtmlNodeAbstract<?> rootNode = htmlProvider.getRootNode();
        if (rootNode == null) return;
        for (HtmlNodeAbstract<?> metaNode : htmlProvider.getMetas()) {
            String metaName = metaNode.getAttributeText("name");
            if (metaName != null && metaName.startsWith(OPENSEARCHSERVER_underscoreFIELD)) {
                String field = metaName.substring(OPENSEARCHSERVER_underscoreFIELD_underscoreLENGTH);
                String[] fields = field.split("\\.");
                if (fields != null) {
                    String content = metaNode.getAttributeText("content");
                    addDirectFields(fields, content);
                }
            }
        }
        addField(ParserFieldEnum.charset, charset);
        addFieldTitle(htmlProvider.getTitle());
        String metaRobots = null;
        String metaDcLanguage = null;
        String metaContentLanguage = null;
        for (HtmlNodeAbstract<?> node : htmlProvider.getMetas()) {
            String attr_underscorename = node.getAttributeText("name");
            String attr_underscorehttp_underscoreequiv = node.getAttributeText("http-equiv");
            if ("keywords".equalsIgnoreCase(attr_underscorename)) addField(ParserFieldEnum.meta_underscorekeywords, HtmlDocumentProvider.getMetaContent(node)); else if ("description".equalsIgnoreCase(attr_underscorename)) addField(ParserFieldEnum.meta_underscoredescription, HtmlDocumentProvider.getMetaContent(node)); else if ("robots".equalsIgnoreCase(attr_underscorename)) metaRobots = HtmlDocumentProvider.getMetaContent(node); else if ("dc.language".equalsIgnoreCase(attr_underscorename)) metaDcLanguage = HtmlDocumentProvider.getMetaContent(node); else if ("content-language".equalsIgnoreCase(attr_underscorehttp_underscoreequiv)) metaContentLanguage = HtmlDocumentProvider.getMetaContent(node);
        }
        boolean metaRobotsFollow = true;
        boolean metaRobotsNoIndex = false;
        if (metaRobots != null) {
            metaRobots = metaRobots.toLowerCase();
            if (metaRobots.contains("noindex")) {
                metaRobotsNoIndex = true;
                addField(ParserFieldEnum.meta_underscorerobots, "noindex");
            }
            if (metaRobots.contains("nofollow")) {
                metaRobotsFollow = false;
                addField(ParserFieldEnum.meta_underscorerobots, "nofollow");
            }
        }
        UrlFilterItem[] urlFilterList = getUrlFilterList();
        List<HtmlNodeAbstract<?>> nodes = rootNode.getAllNodes("a", "frame");
        IndexDocument srcDoc = getSourceDocument();
        if (srcDoc != null && nodes != null && metaRobotsFollow) {
            URL currentURL = htmlProvider.getBaseHref();
            if (currentURL == null && urlItemFieldEnum != null) {
                FieldValueItem fvi = srcDoc.getFieldValue(urlItemFieldEnum.url.getName(), 0);
                if (fvi != null) currentURL = new URL(fvi.getValue());
            }
            for (HtmlNodeAbstract<?> node : nodes) {
                String href = null;
                String rel = null;
                String nodeName = node.getNodeName();
                if ("a".equals(nodeName)) {
                    href = node.getAttributeText("href");
                    rel = node.getAttributeText("rel");
                } else if ("frame".equals(nodeName)) {
                    href = node.getAttributeText("src");
                }
                boolean follow = true;
                if (rel != null) if (rel.contains("nofollow")) follow = false;
                URL newUrl = null;
                if (href != null) if (!href.startsWith("javascript:")) if (currentURL != null) newUrl = LinkUtils.getLink(currentURL, href, urlFilterList);
                if (newUrl != null) {
                    ParserFieldEnum field = null;
                    if (newUrl.getHost().equalsIgnoreCase(currentURL.getHost())) {
                        if (follow) field = ParserFieldEnum.internal_underscorelink; else field = ParserFieldEnum.internal_underscorelink_underscorenofollow;
                    } else {
                        if (follow) field = ParserFieldEnum.external_underscorelink; else field = ParserFieldEnum.external_underscorelink_underscorenofollow;
                    }
                    addField(field, newUrl.toExternalForm());
                }
            }
        }
        if (!metaRobotsNoIndex) {
            nodes = rootNode.getNodes("html", "body");
            if (nodes == null || nodes.size() == 0) nodes = rootNode.getNodes("html");
            if (nodes != null && nodes.size() > 0) {
                StringBuffer sb = new StringBuffer();
                getBodyTextContent(sb, nodes.get(0), true, null);
                addField(ParserFieldEnum.body, sb);
            }
        }
        Locale lang = null;
        String langMethod = null;
        String[] pathHtml = { "html" };
        nodes = rootNode.getNodes(pathHtml);
        if (nodes != null && nodes.size() > 0) {
            langMethod = "html lang attribute";
            String l = nodes.get(0).getAttributeText("lang");
            if (l != null) lang = Lang.findLocaleISO639(l);
        }
        if (lang == null && metaContentLanguage != null) {
            langMethod = "meta http-equiv content-language";
            lang = Lang.findLocaleISO639(metaContentLanguage);
        }
        if (lang == null && metaDcLanguage != null) {
            langMethod = "meta dc.language";
            lang = Lang.findLocaleISO639(metaDcLanguage);
        }
        if (lang != null) {
            addField(ParserFieldEnum.lang, lang.getLanguage());
            addField(ParserFieldEnum.lang_underscoremethod, langMethod);
        } else if (!metaRobotsNoIndex) lang = langDetection(10000, ParserFieldEnum.body);
    }


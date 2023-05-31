    public QueryResult doSearch(String searchTerm, Integer searchInReceivedItems, Integer searchInSentItems, Integer searchInSupervisedItems, Integer startRow, Integer resultCount, Boolean searchArchived, Boolean searchInItemsNeededAttentionOnly) throws UnsupportedEncodingException, IOException {
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_underscoreID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        QueryResult queryResult = new QueryResult();
        SearchRequest request = new SearchRequest();
        SearchItemsQuery query = new SearchItemsQuery();
        query.setArchiveIncluded(searchArchived);
        log(INFO, "searchTerm=" + searchTerm);
        log(INFO, "search in received=" + searchInReceivedItems);
        log(INFO, "search in sent=" + searchInSentItems);
        log(INFO, "search in supervised=" + searchInSupervisedItems);
        List<String> filters = new ArrayList<String>();
        if (searchInItemsNeededAttentionOnly == false) {
            if (searchInReceivedItems != null) {
                filters.add("ALL_underscoreRECEIVED_underscoreITEMS");
            }
            if (searchInSentItems != null) {
                filters.add("ALL_underscoreSENT_underscoreITEMS");
            }
            if (searchInSupervisedItems != null) {
                filters.add("ALL_underscoreSUPERVISED_underscoreITEMS");
            }
        } else {
            if (searchInReceivedItems != null) {
                filters.add("RECEIVED_underscoreITEMS_underscoreNEEDED_underscoreATTENTION");
            }
            if (searchInSentItems != null) {
                filters.add("SENT_underscoreITEMS_underscoreNEEDED_underscoreATTENTION");
            }
        }
        query.setFilters(filters);
        query.setId("1234");
        query.setOwner(sessionId);
        query.setReferenceOnly(false);
        query.setSearchTerm(searchTerm);
        query.setUseOR(false);
        request.setStartRow(startRow);
        request.setResultCount(resultCount);
        request.setQuery(query);
        request.setSessionId(sessionId);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_underscoreABSOLUTE_underscoreREFERENCES);
        writer.alias("SearchRequest", SearchRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_underscoreABSOLUTE_underscoreREFERENCES);
        reader.alias("SearchResponse", SearchResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpGet httpget = new HttpGet(MewitProperties.getMewitUrl() + "/resources/search?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            SearchResponse searchResponse = (SearchResponse) reader.fromXML(result);
            List<Item> items = searchResponse.getItems();
            queryResult.setItems(items);
            queryResult.setTotal(searchResponse.getTotalResultCount());
            queryResult.setStartRow(searchResponse.getStartRow());
        }
        return queryResult;
    }


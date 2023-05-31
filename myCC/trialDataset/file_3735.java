    @Test
    public void test_underscorelookupResourceType_underscoreFullSearch_underscoreTwoWords() throws Exception {
        URL url = new URL(baseUrl + "/lookupResourceType/alloyed+tritanium");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":25595,\"itemCategoryID\":4,\"name\":\"Alloyed Tritanium Bar\",\"icon\":\"69_underscore11\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
    }


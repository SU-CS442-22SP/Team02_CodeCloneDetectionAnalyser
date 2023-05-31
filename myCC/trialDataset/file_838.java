    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cautaprodus);
        HttpGet request = new HttpGet(SERVICE_underscoreURI + "/json/getproducts");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String theString = new String("");
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            InputStream stream = responseEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Vector<String> vectorOfStrings = new Vector<String>();
            String tempString = new String();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            stream.close();
            theString = builder.toString();
            JSONObject json = new JSONObject(theString);
            Log.i("_underscoreGetPerson_underscore", "<jsonobject>\n" + json.toString() + "\n</jsonobject>");
            JSONArray nameArray = json.getJSONArray("getProductsResult");
            for (int i = 0; i < nameArray.length(); i++) {
                Log.i("_underscoreGetProducts_underscore", "<ID" + i + ">" + nameArray.getJSONObject(i).getString("ID") + "</ID" + i + ">\n");
                Log.i("_underscoreGetProducts_underscore", "<Name" + i + ">" + nameArray.getJSONObject(i).getString("Name") + "</Name" + i + ">\n");
                Log.i("_underscoreGetProducts_underscore", "<Price" + i + ">" + nameArray.getJSONObject(i).getString("Price") + "</Price" + i + ">\n");
                Log.i("_underscoreGetProducts_underscore", "<Symbol" + i + ">" + nameArray.getJSONObject(i).getString("Symbol") + "</Symbol" + i + ">\n");
                tempString = nameArray.getJSONObject(i).getString("Name") + "\n" + nameArray.getJSONObject(i).getString("Price") + "\n" + nameArray.getJSONObject(i).getString("Symbol");
                vectorOfStrings.add(new String(tempString));
            }
            int orderCount = vectorOfStrings.size();
            String[] orderTimeStamps = new String[orderCount];
            vectorOfStrings.copyInto(orderTimeStamps);
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_underscorelist_underscoreitem_underscore1, orderTimeStamps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


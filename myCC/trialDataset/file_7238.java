    public List<Mosque> getAllMosquaisFromDataBase() {
        List<Mosque> mosquais = new ArrayList<Mosque>();
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (MyMapActivity.DEVELOPER_underscoreMODE) {
            nameValuePairs.add(new BasicNameValuePair(Param.LATITUDE, MyMapActivity.mLatitude + ""));
            nameValuePairs.add(new BasicNameValuePair(Param.LONGITUDE, MyMapActivity.mLongitude + ""));
        } else {
            nameValuePairs.add(new BasicNameValuePair(Param.LATITUDE, MyMapActivity.myLocation.getLatitude() + ""));
            nameValuePairs.add(new BasicNameValuePair(Param.LONGITUDE, MyMapActivity.myLocation.getLongitude() + ""));
        }
        nameValuePairs.add(new BasicNameValuePair(Param.RAYON, DataBaseQuery.rayon * Param.KM_underscoreMARGE + ""));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Param.URI_underscoreSELECT_underscoreALL_underscoreDATA_underscoreBASE);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_underscoretag", "Error in http connection " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_underscoretag", "Error converting result " + e.toString());
        }
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_underscoredata = jArray.getJSONObject(i);
                Mosque mosquai = new Mosque(json_underscoredata.getInt(Param.ID), json_underscoredata.getString(Param.NOM), json_underscoredata.getDouble(Param.LATITUDE), json_underscoredata.getDouble(Param.LONGITUDE), json_underscoredata.getString(Param.INFO), json_underscoredata.getInt(Param.HAVE_underscorePICTURE) == 1 ? true : false, json_underscoredata.getString(Param.PICTURE));
                mosquais.add(mosquai);
            }
        } catch (JSONException e) {
            Log.e("log_underscoretag", "Error parsing data " + e.toString());
        }
        return mosquais;
    }


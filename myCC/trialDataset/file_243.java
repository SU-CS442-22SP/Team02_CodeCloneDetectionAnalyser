    public static RecordResponse loadRecord(RecordRequest recordRequest) {
        RecordResponse recordResponse = new RecordResponse();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(recordRequest.isContact() ? URL_underscoreRECORD_underscoreCONTACT : URL_underscoreRECORD_underscoreCOMPANY);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("format", "xml"));
            nameValuePairs.add(new BasicNameValuePair("token", recordRequest.getToken()));
            nameValuePairs.add(new BasicNameValuePair("id", recordRequest.getId()));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            String line = EntityUtils.toString(response.getEntity());
            Document document = XMLfunctions.XMLfromString(line);
            NodeList nodes = document.getElementsByTagName("response");
            Element e = (Element) nodes.item(0);
            String Name_underscore_underscoreLast_underscore_underscoreFirst_underscore = XMLfunctions.getValue(e, recordRequest.isContact() ? "Name_underscore_underscoreLast_underscore_underscoreFirst_underscore" : "Name");
            String phone = "";
            if (!recordRequest.isContact()) phone = XMLfunctions.getValue(e, "Phone");
            String Email1 = XMLfunctions.getValue(e, recordRequest.isContact() ? "Personal_underscoreEmail" : "Email");
            String Home_underscoreFax = XMLfunctions.getValue(e, recordRequest.isContact() ? "Home_underscoreFax" : "Fax1");
            String Address1 = XMLfunctions.getValue(e, "Address1");
            String Address2 = XMLfunctions.getValue(e, "Address2");
            String City = XMLfunctions.getValue(e, "City");
            String State = XMLfunctions.getValue(e, "State");
            String Zip = XMLfunctions.getValue(e, "Zip");
            String Country = XMLfunctions.getValue(e, "Country");
            String Profile = XMLfunctions.getValue(e, "Profile");
            String success = XMLfunctions.getValue(e, "success");
            String error = XMLfunctions.getValue(e, "error");
            recordResponse.setName(Name_underscore_underscoreLast_underscore_underscoreFirst_underscore);
            recordResponse.setPhone(phone);
            recordResponse.setEmail(Email1);
            recordResponse.setHomeFax(Home_underscoreFax);
            recordResponse.setAddress1(Address1);
            recordResponse.setAddress2(Address2);
            recordResponse.setCity(City);
            recordResponse.setState(State);
            recordResponse.setZip(Zip);
            recordResponse.setProfile(Profile);
            recordResponse.setCountry(Country);
            recordResponse.setSuccess(success);
            recordResponse.setError(error);
        } catch (Exception e) {
        }
        return recordResponse;
    }


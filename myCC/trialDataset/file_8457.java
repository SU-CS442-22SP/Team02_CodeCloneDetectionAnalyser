                    public void run() {
                        try {
                            HttpPost httpPostRequest = new HttpPost(Feesh.device_underscoreURL);
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("c", "feed"));
                            nameValuePairs.add(new BasicNameValuePair("amount", String.valueOf(foodAmount)));
                            nameValuePairs.add(new BasicNameValuePair("type", String.valueOf(foodType)));
                            httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse httpResponse = (HttpResponse) new DefaultHttpClient().execute(httpPostRequest);
                            HttpEntity entity = httpResponse.getEntity();
                            String resultString = "";
                            if (entity != null) {
                                InputStream instream = entity.getContent();
                                resultString = convertStreamToString(instream);
                                instream.close();
                            }
                            Message msg_underscoretoast = new Message();
                            msg_underscoretoast.obj = resultString;
                            toast_underscorehandler.sendMessage(msg_underscoretoast);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


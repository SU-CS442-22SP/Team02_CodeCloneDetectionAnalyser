    public boolean sendMail(MailObject mail, boolean backup) throws NetworkException, ContentException {
        HttpClient client = HttpConfig.newInstance();
        HttpPost post = new HttpPost(HttpConfig.bbsURL() + HttpConfig.BBS_underscoreMAIL_underscoreSEND);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_underscoreMAIL_underscoreSEND_underscoreREF_underscorePARAM_underscoreNAME, "pstmail"));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_underscoreMAIL_underscoreSEND_underscoreRECV_underscorePARAM_underscoreNAME, mail.getSender()));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_underscoreMAIL_underscoreSEND_underscoreTITLE_underscorePARAM_underscoreNAME, mail.getTitle()));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_underscoreMAIL_underscoreSEND_underscoreCONTENT_underscorePARAM_underscoreNAME, mail.getContent()));
        if (backup) nvps.add(new BasicNameValuePair(HttpConfig.BBS_underscoreMAIL_underscoreSEND_underscoreBACKUP_underscorePARAM_underscoreNAME, "backup"));
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, BBSBodyParseHelper.BBS_underscoreCHARSET));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (HTTPUtil.isHttp200(response)) {
                HTTPUtil.consume(response.getEntity());
                return true;
            } else {
                String msg = BBSBodyParseHelper.parseFailMsg(entity);
                throw new ContentException(msg);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new NetworkException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }


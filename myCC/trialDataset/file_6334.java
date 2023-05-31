    public static void logout(String verify) throws NetworkException {
        HttpClient client = HttpUtil.newInstance();
        HttpGet get = new HttpGet(HttpUtil.KAIXIN_underscoreLOGOUT_underscoreURL + HttpUtil.KAIXIN_underscorePARAM_underscoreVERIFY + verify);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            if (response != null && response.getEntity() != null) {
                HTTPUtil.consume(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }


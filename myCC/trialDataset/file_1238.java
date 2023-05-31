        @Override
        protected DefaultHttpClient doInBackground(Account... params) {
            AccountManager accountManager = AccountManager.get(mainActivity);
            Account account = params[0];
            try {
                Bundle bundle = accountManager.getAuthToken(account, "ah", false, null, null).getResult();
                Intent intent = (Intent) bundle.get(AccountManager.KEY_underscoreINTENT);
                if (intent != null) {
                    mainActivity.startActivity(intent);
                } else {
                    String auth_underscoretoken = bundle.getString(AccountManager.KEY_underscoreAUTHTOKEN);
                    http_underscoreclient.getParams().setBooleanParameter(ClientPNames.HANDLE_underscoreREDIRECTS, false);
                    HttpGet http_underscoreget = new HttpGet("http://3dforandroid.appspot.com/_underscoreah" + "/login?continue=http://localhost/&auth=" + auth_underscoretoken);
                    HttpResponse response = http_underscoreclient.execute(http_underscoreget);
                    if (response.getStatusLine().getStatusCode() != 302) return null;
                    for (Cookie cookie : http_underscoreclient.getCookieStore().getCookies()) {
                        if (cookie.getName().equals("ACSID")) {
                            authClient = http_underscoreclient;
                            String json = createJsonFile(Kind);
                            initializeSQLite();
                            initializeServer(json);
                        }
                    }
                }
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return http_underscoreclient;
        }


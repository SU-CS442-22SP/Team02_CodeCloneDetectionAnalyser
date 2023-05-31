    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String UrlStr = req.getRequestURL().toString();
        URL domainurl = new URL(UrlStr);
        domain = domainurl.getHost();
        pathinfo = req.getPathInfo();
        String user_underscoreagent = req.getHeader("user-agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(user_underscoreagent);
        String browser = userAgent.getBrowser().getName();
        String[] shot_underscoredomain_underscorearray = domain.split("\\.");
        shot_underscoredomain = shot_underscoredomain_underscorearray[1] + "." + shot_underscoredomain_underscorearray[2];
        if (browser.equalsIgnoreCase("Robot/Spider") || browser.equalsIgnoreCase("Lynx") || browser.equalsIgnoreCase("Downloading Tool")) {
            JSONObject domainJsonObject = CsvReader.CsvReader("domainparUpdated.csv", shot_underscoredomain);
            log.info(domainJsonObject.toString());
        } else {
            String title;
            String locale;
            String facebookid;
            String color;
            String headImage;
            String google_underscoread_underscoreclient;
            String google_underscoread_underscoreslot1;
            String google_underscoread_underscorewidth1;
            String google_underscoread_underscoreheight1;
            String google_underscoread_underscoreslot2;
            String google_underscoread_underscorewidth2;
            String google_underscoread_underscoreheight2;
            String google_underscoread_underscoreslot3;
            String google_underscoread_underscorewidth3;
            String google_underscoread_underscoreheight3;
            String countrycode = null;
            String city = null;
            String gmclickval = null;
            String videos = null;
            int intcount = 0;
            String strcount = "0";
            boolean countExist = false;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("count")) {
                        strcount = cookies[i].getValue();
                        if (strcount != null && strcount.length() > 0) {
                            log.info("Check count " + strcount + " path " + cookies[i].getPath());
                            intcount = Integer.parseInt(strcount);
                            intcount++;
                        } else {
                            intcount = 1;
                        }
                        log.info("New count " + intcount);
                        LongLivedCookie count = new LongLivedCookie("count", Integer.toString(intcount));
                        resp.addCookie(count);
                        countExist = true;
                    }
                    if (cookies[i].getName().equals("countrycode")) {
                        countrycode = cookies[i].getValue();
                    }
                    if (cookies[i].getName().equals("city")) {
                        city = cookies[i].getValue();
                    }
                    if (cookies[i].getName().equals("videos")) {
                        videos = cookies[i].getValue();
                        log.info("Welcome videos " + videos);
                    }
                    if (cookies[i].getName().equals("gmclick")) {
                        log.info("gmclick exist!!");
                        gmclickval = cookies[i].getValue();
                        if (intcount % 20 == 0 && intcount > 0) {
                            log.info("Cancell gmclick -> " + gmclickval + " intcount " + intcount + " path " + cookies[i].getPath());
                            Cookie gmclick = new Cookie("gmclick", "0");
                            gmclick.setPath("/");
                            gmclick.setMaxAge(0);
                            resp.addCookie(gmclick);
                        }
                    }
                }
                if (!countExist) {
                    LongLivedCookie count = new LongLivedCookie("count", "0");
                    resp.addCookie(count);
                    log.info(" Not First visit count Don't Exist!!");
                }
                if (videos == null) {
                    LongLivedCookie videoscookies = new LongLivedCookie("videos", "0");
                    resp.addCookie(videoscookies);
                    log.info("Not First visit VIDEOS Don't Exist!!");
                }
            } else {
                LongLivedCookie count = new LongLivedCookie("count", strcount);
                resp.addCookie(count);
                LongLivedCookie videosfirstcookies = new LongLivedCookie("videos", "0");
                resp.addCookie(videosfirstcookies);
                log.info("First visit count = " + intcount + " videos 0");
            }
            String[] dompar = CommUtils.CsvParsing(domain, "domainpar.csv");
            title = dompar[0];
            locale = dompar[1];
            facebookid = dompar[2];
            color = dompar[3];
            headImage = dompar[4];
            google_underscoread_underscoreclient = dompar[5];
            google_underscoread_underscoreslot1 = dompar[6];
            google_underscoread_underscorewidth1 = dompar[7];
            google_underscoread_underscoreheight1 = dompar[8];
            google_underscoread_underscoreslot2 = dompar[9];
            google_underscoread_underscorewidth2 = dompar[10];
            google_underscoread_underscoreheight2 = dompar[11];
            google_underscoread_underscoreslot3 = dompar[12];
            google_underscoread_underscorewidth3 = dompar[13];
            google_underscoread_underscoreheight3 = dompar[14];
            String ip = req.getRemoteHost();
            if ((countrycode == null) || (city == null)) {
                String ipServiceCall = "http://api.ipinfodb.com/v2/ip_underscorequery.php?key=abbb04fd823793c5343a046e5d56225af37861b9020e9bc86313eb20486b6133&ip=" + ip + "&output=json";
                String strCallResult = "";
                URL url = new URL(ipServiceCall);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
                StringBuffer response = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                strCallResult = response.toString();
                try {
                    JSONObject jso = new JSONObject(strCallResult);
                    log.info("Status -> " + jso.get("Status").toString());
                    log.info("City -> " + jso.get("City").toString());
                    city = jso.get("City").toString();
                    countrycode = jso.get("CountryCode").toString();
                    log.info("countrycode -> " + countrycode);
                    if ((city.length() == 0) || (city == null)) {
                        LongLivedCookie cookcity = new LongLivedCookie("city", "Helsinki");
                        resp.addCookie(cookcity);
                        city = "Helsinki";
                    } else {
                        LongLivedCookie cookcity = new LongLivedCookie("city", city);
                        resp.addCookie(cookcity);
                    }
                    if (countrycode.length() == 0 || (countrycode == null) || countrycode.equals("RD")) {
                        LongLivedCookie cookcountrycode = new LongLivedCookie("countrycode", "FI");
                        resp.addCookie(cookcountrycode);
                        countrycode = "FI";
                    } else {
                        LongLivedCookie cookcountrycode = new LongLivedCookie("countrycode", countrycode);
                        resp.addCookie(cookcountrycode);
                    }
                } catch (JSONException e) {
                    log.severe(e.getMessage());
                } finally {
                    if ((countrycode == null) || (city == null)) {
                        log.severe("need use finally!!!");
                        countrycode = "FI";
                        city = "Helsinki";
                        LongLivedCookie cookcity = new LongLivedCookie("city", "Helsinki");
                        resp.addCookie(cookcity);
                        LongLivedCookie cookcountrycode = new LongLivedCookie("countrycode", "FI");
                        resp.addCookie(cookcountrycode);
                    }
                }
            }
            JSONArray startjsonarray = new JSONArray();
            JSONArray memjsonarray = new JSONArray();
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> mapt = new HashMap<String, Object>();
            mapt.put("img", headImage);
            mapt.put("color", color);
            mapt.put("title", title);
            mapt.put("locale", locale);
            mapt.put("domain", domain);
            mapt.put("facebookid", facebookid);
            mapt.put("ip", ip);
            mapt.put("countrycode", countrycode);
            mapt.put("city", city);
            map.put("theme", mapt);
            startjsonarray.put(map);
            String[] a = { "mem0", "mem20", "mem40", "mem60", "mem80", "mem100", "mem120", "mem140", "mem160", "mem180" };
            List memlist = Arrays.asList(a);
            Collections.shuffle(memlist);
            Map<String, Object> mammap = new HashMap<String, Object>();
            mammap.put("memlist", memlist);
            memjsonarray.put(mammap);
            log.info(memjsonarray.toString());
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">");
            out.println("<head>");
            out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
            out.println("<meta name=\"gwt:property\" content=\"locale=" + locale + "\">");
            out.println("<link type=\"text/css\" rel=\"stylesheet\" href=\"NewTube.css\">");
            out.println("<title>" + title + "</title>");
            out.println("<script type=\"text/javascript\" language=\"javascript\" src=\"newtube/newtube.nocache.js\"></script>");
            out.println("<script type='text/javascript'>var jsonStartParams = " + startjsonarray.toString() + ";</script>");
            out.println("<script type='text/javascript'>var girlsphones = " + CommUtils.CsvtoJson("girlsphones.csv").toString() + ";</script>");
            out.println("<script type='text/javascript'>");
            out.println("var mem = " + memjsonarray.toString() + ";");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div id='fb-root'></div>");
            out.println("<script>");
            out.println("window.fbAsyncInit = function() {");
            out.println("FB.init({appId: '" + facebookid + "', status: true, cookie: true,xfbml: true});};");
            out.println("(function() {");
            out.println("var e = document.createElement('script'); e.async = true;");
            out.println("e.src = document.location.protocol +");
            out.println("'//connect.facebook.net/" + locale + "/all.js';");
            out.println("document.getElementById('fb-root').appendChild(e);");
            out.println("}());");
            out.println("</script>");
            out.println("<div id=\"start\"></div>");
            out.println("<div id=\"seo_underscorecontent\">");
            BufferedReader bufRdr = new BufferedReader(new InputStreamReader(new FileInputStream(domain + ".html"), "UTF8"));
            String contline = null;
            while ((contline = bufRdr.readLine()) != null) {
                out.println(contline);
            }
            bufRdr.close();
            if (countrycode != null && !countrycode.equalsIgnoreCase("US") && !countrycode.equalsIgnoreCase("IE") && !countrycode.equalsIgnoreCase("UK") && intcount > 2 && intcount < 51) {
                out.println("<script type=\"text/javascript\"><!--");
                out.println("google_underscoread_underscoreclient = \"" + google_underscoread_underscoreclient + "\";");
                out.println("google_underscoread_underscoreslot = \"" + google_underscoread_underscoreslot1 + "\";");
                out.println("google_underscoread_underscorewidth = " + google_underscoread_underscorewidth1 + ";");
                out.println("google_underscoread_underscoreheight = " + google_underscoread_underscoreheight1 + ";");
                out.println("//-->");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"");
                out.println("src=\"" + google_underscoread_underscoreclient + ".js\">");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"><!--");
                out.println("google_underscoread_underscoreclient = \"" + google_underscoread_underscoreclient + "\";");
                out.println("google_underscoread_underscoreslot = \"" + google_underscoread_underscoreslot2 + "\";");
                out.println("google_underscoread_underscorewidth = " + google_underscoread_underscorewidth2 + ";");
                out.println("google_underscoread_underscoreheight = " + google_underscoread_underscoreheight2 + ";");
                out.println("//-->");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"");
                out.println("src=\"" + google_underscoread_underscoreclient + ".js\">");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"><!--");
                out.println("google_underscoread_underscoreclient = \"" + google_underscoread_underscoreclient + "\";");
                out.println("google_underscoread_underscoreslot = \"" + google_underscoread_underscoreslot3 + "\";");
                out.println("google_underscoread_underscorewidth = " + google_underscoread_underscorewidth3 + ";");
                out.println("google_underscoread_underscoreheight = " + google_underscoread_underscoreheight3 + ";");
                out.println("//-->");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"");
                out.println("src=\"" + google_underscoread_underscoreclient + ".js\">");
                out.println("</script>");
            }
            if (countrycode != null && !countrycode.equalsIgnoreCase("US") && !countrycode.equalsIgnoreCase("IE") && !countrycode.equalsIgnoreCase("UK") && intcount > 50) {
                out.println("<script type=\"text/javascript\"><!--");
                out.println("google_underscoread_underscoreclient = \"" + "pub-9496078135369870" + "\";");
                out.println("google_underscoread_underscoreslot = \"" + "8683942065" + "\";");
                out.println("google_underscoread_underscorewidth = " + "160" + ";");
                out.println("google_underscoread_underscoreheight = " + "600" + ";");
                out.println("//-->");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"");
                out.println("src=\"pub-9496078135369870" + "" + ".js\">");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"><!--");
                out.println("google_underscoread_underscoreclient = \"" + "pub-9496078135369870" + "\";");
                out.println("google_underscoread_underscoreslot = \"" + "0941291340" + "\";");
                out.println("google_underscoread_underscorewidth = " + "728" + ";");
                out.println("google_underscoread_underscoreheight = " + "90" + ";");
                out.println("//-->");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"");
                out.println("src=\"" + "pub-9496078135369870" + ".js\">");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"><!--");
                out.println("google_underscoread_underscoreclient = \"" + "pub-9496078135369870" + "\";");
                out.println("google_underscoread_underscoreslot = \"" + "4621616265" + "\";");
                out.println("google_underscoread_underscorewidth = " + "468" + ";");
                out.println("google_underscoread_underscoreheight = " + "60" + ";");
                out.println("//-->");
                out.println("</script>");
                out.println("<script type=\"text/javascript\"");
                out.println("src=\"" + "pub-9496078135369870" + ".js\">");
                out.println("</script>");
            }
            out.println("</div>");
            out.println("</body></html>");
            out.close();
        }
    }


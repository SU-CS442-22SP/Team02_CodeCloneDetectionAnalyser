    public static XMLShowInfo NzbSearch(TVRageShowInfo tvrage, XMLShowInfo xmldata, int latestOrNext) {
        String newzbin_underscorequery = "", csvData = "", hellaQueueDir = "", newzbinUsr = "", newzbinPass = "";
        String[] tmp;
        DateFormat tvRageDateFormat = new SimpleDateFormat("MMM/dd/yyyy");
        DateFormat tvRageDateFormatFix = new SimpleDateFormat("yyyy-MM-dd");
        newzbin_underscorequery = "?q=" + xmldata.showName + "+";
        if (latestOrNext == 0) {
            if (xmldata.searchBy.equals("ShowName Season x Episode")) newzbin_underscorequery += tvrage.latestSeasonNum + "x" + tvrage.latestEpisodeNum; else if (xmldata.searchBy.equals("Showname SeriesNum")) newzbin_underscorequery += tvrage.latestSeriesNum; else if (xmldata.searchBy.equals("Showname YYYY-MM-DD")) {
                try {
                    Date airTime = tvRageDateFormat.parse(tvrage.latestAirDate);
                    newzbin_underscorequery += tvRageDateFormatFix.format(airTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (xmldata.searchBy.equals("Showname EpisodeTitle")) newzbin_underscorequery += tvrage.latestTitle;
        } else {
            if (xmldata.searchBy.equals("ShowName Season x Episode")) newzbin_underscorequery += tvrage.nextSeasonNum + "x" + tvrage.nextEpisodeNum; else if (xmldata.searchBy.equals("Showname SeriesNum")) newzbin_underscorequery += tvrage.nextSeriesNum; else if (xmldata.searchBy.equals("Showname YYYY-MM-DD")) {
                try {
                    Date airTime = tvRageDateFormat.parse(tvrage.nextAirDate);
                    newzbin_underscorequery += tvRageDateFormatFix.format(airTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (xmldata.searchBy.equals("Showname EpisodeTitle")) newzbin_underscorequery += tvrage.nextTitle;
        }
        newzbin_underscorequery += "&searchaction=Search";
        newzbin_underscorequery += "&fpn=p";
        newzbin_underscorequery += "&category=8category=11";
        newzbin_underscorequery += "&area=-1";
        newzbin_underscorequery += "&u_underscorenfo_underscoreposts_underscoreonly=0";
        newzbin_underscorequery += "&u_underscoreurl_underscoreposts_underscoreonly=0";
        newzbin_underscorequery += "&u_underscorecomment_underscoreposts_underscoreonly=0";
        newzbin_underscorequery += "&u_underscorev3_underscoreretention=1209600";
        newzbin_underscorequery += "&ps_underscorerb_underscorelanguage=" + xmldata.language;
        newzbin_underscorequery += "&sort=ps_underscoreedit_underscoredate";
        newzbin_underscorequery += "&order=desc";
        newzbin_underscorequery += "&areadone=-1";
        newzbin_underscorequery += "&feed=csv";
        newzbin_underscorequery += "&ps_underscorerb_underscorevideo_underscoreformat=" + xmldata.format;
        newzbin_underscorequery = newzbin_underscorequery.replaceAll(" ", "%20");
        System.out.println("http://v3.newzbin.com/search/" + newzbin_underscorequery);
        try {
            URL url = new URL("http://v3.newzbin.com/search/" + newzbin_underscorequery);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            csvData = in.readLine();
            if (csvData != null) {
                JavaNZB.searchCount++;
                if (searchCount == 6) {
                    searchCount = 0;
                    System.out.println("Sleeping for 60 seconds");
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tmp = csvData.split(",");
                tmp[2] = tmp[2].substring(1, tmp[2].length() - 1);
                tmp[3] = tmp[3].substring(1, tmp[3].length() - 1);
                Pattern p = Pattern.compile("[\\\\</:>?\\[|\\]\"]");
                Matcher matcher = p.matcher(tmp[3]);
                tmp[3] = matcher.replaceAll(" ");
                tmp[3] = tmp[3].replaceAll("&", "and");
                URLConnection urlConn;
                DataOutputStream printout;
                url = new URL("http://v3.newzbin.com/api/dnzb/");
                urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                printout = new DataOutputStream(urlConn.getOutputStream());
                String content = "username=" + JavaNZB.newzbinUsr + "&password=" + JavaNZB.newzbinPass + "&reportid=" + tmp[2];
                printout.writeBytes(content);
                printout.flush();
                printout.close();
                BufferedReader nzbInput = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String format = "";
                if (xmldata.format.equals("17")) format = " Xvid";
                if (xmldata.format.equals("131072")) format = " x264";
                if (xmldata.format.equals("2")) format = " DVD";
                if (xmldata.format.equals("4")) format = " SVCD";
                if (xmldata.format.equals("8")) format = " VCD";
                if (xmldata.format.equals("32")) format = " HDts";
                if (xmldata.format.equals("64")) format = " WMV";
                if (xmldata.format.equals("128")) format = " Other";
                if (xmldata.format.equals("256")) format = " ratDVD";
                if (xmldata.format.equals("512")) format = " iPod";
                if (xmldata.format.equals("1024")) format = " PSP";
                File f = new File(JavaNZB.hellaQueueDir, tmp[3] + format + ".nzb");
                BufferedWriter out = new BufferedWriter(new FileWriter(f));
                String str;
                System.out.println("--Downloading " + tmp[3] + format + ".nzb" + " to queue directory--");
                while (null != ((str = nzbInput.readLine()))) out.write(str);
                nzbInput.close();
                out.close();
                if (latestOrNext == 0) {
                    xmldata.episode = tvrage.latestEpisodeNum;
                    xmldata.season = tvrage.latestSeasonNum;
                } else {
                    xmldata.episode = tvrage.nextEpisodeNum;
                    xmldata.season = tvrage.nextSeasonNum;
                }
            } else System.out.println("No new episode posted");
            System.out.println();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            System.out.println("IO Exception from NzbSearch");
        }
        return xmldata;
    }


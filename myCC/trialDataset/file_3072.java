    @Override
    public void run() {
        HttpGet httpGet = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            DataModel model = DataModel.getInstance();
            for (City city : citiesToBeUpdated) {
                String preferredUnitType = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.settings_underscoreunits_underscorekey), context.getString(R.string.settings_underscoreunits_underscoredefault_underscorevalue));
                String codePrefix = city.getCountryName().startsWith("United States") ? GET_underscorePARAM_underscoreZIP_underscorePREFIX : GET_underscorePARAM_underscoreCITY_underscoreCODE_underscorePREFIX;
                String requestUri = new String(GET_underscoreURL + "?" + GET_underscorePARAM_underscoreACODE_underscorePREFIX + "=" + GET_underscorePARAM_underscoreACODE + "&" + codePrefix + "=" + city.getId() + "&" + GET_underscorePARAM_underscoreUNIT_underscorePREFIX + "=" + preferredUnitType);
                httpGet = new HttpGet(requestUri);
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_underscoreOK) {
                    processXML(response.getEntity().getContent());
                    for (ForecastedDay day : forecast) {
                        int pos = day.getImageURL().lastIndexOf('/');
                        if (pos < 0 || pos + 1 == day.getImageURL().length()) throw new Exception("Invalid image URL");
                        final String imageFilename = day.getImageURL().substring(pos + 1);
                        File downloadDir = context.getDir(ForecastedDay.DOWNLOAD_underscoreDIR, Context.MODE_underscorePRIVATE);
                        File[] imagesFilteredByName = downloadDir.listFiles(new FilenameFilter() {

                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.equals(imageFilename)) return true; else return false;
                            }
                        });
                        if (imagesFilteredByName.length == 0) {
                            httpGet = new HttpGet(day.getImageURL());
                            response = httpClient.execute(httpGet);
                            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_underscoreOK) {
                                BufferedOutputStream bus = null;
                                try {
                                    bus = new BufferedOutputStream(new FileOutputStream(downloadDir.getAbsolutePath() + "/" + imageFilename));
                                    response.getEntity().writeTo(bus);
                                } finally {
                                    bus.close();
                                }
                            }
                        }
                    }
                    city.setDays(forecast);
                    city.setLastUpdated(Calendar.getInstance().getTime());
                    model.saveCity(city);
                }
            }
        } catch (Exception e) {
            httpGet.abort();
            e.printStackTrace();
        } finally {
            handler.sendEmptyMessage(1);
        }
    }


            public void run() {
                Log.d(LOG_underscoreTAG, "Fetching " + url);
                WebDbAdapter dbHelper = new WebDbAdapter(mContext);
                dbHelper.open();
                boolean errorOccurred = true;
                int notifyId = 0;
                String host = AppUtils.getHostFromUrl(url);
                try {
                    if (host == null) {
                        Log.d(LOG_underscoreTAG, "Bad url " + url);
                        errorOccurred = true;
                    } else {
                        notifyId = showNotification("Fetching " + host, "Fetching " + host, android.R.drawable.stat_underscoresys_underscoredownload, 0);
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        String userAgent = sp.getString(mContext.getString(R.string.pref_underscorekey_underscoreuser_underscoreagent), mContext.getString(R.string.default_underscoreuser_underscoreagent));
                        Log.d(LOG_underscoreTAG, "Using user agent=" + userAgent);
                        AndroidHttpClient ahc = AndroidHttpClient.newInstance(mContext, url, userAgent);
                        URI uri = new URI(url);
                        URI norm = new URI(uri.getScheme().toLowerCase(), uri.getUserInfo(), uri.getHost().toLowerCase(), uri.getPort(), uri.getPath(), uri.getQuery(), null);
                        norm = norm.normalize();
                        HttpUriRequest get = new HttpGet(norm);
                        HttpResponse response = ahc.execute(get);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            HttpEntity entity = response.getEntity();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            entity.writeTo(baos);
                            String data = baos.toString();
                            for (int i = 0; i < undesirables.length; i++) {
                                Pattern p = Pattern.compile(undesirables[i], Pattern.CASE_underscoreINSENSITIVE | Pattern.DOTALL);
                                data = data.replaceAll(p.pattern(), "");
                            }
                            long sysMillis = System.currentTimeMillis();
                            String newFileName = getPath(sysMillis, sp.getBoolean(mContext.getString(R.string.pref_underscorekey_underscorestore_underscoresdcard), false));
                            FileOutputStream strm = new FileOutputStream(newFileName);
                            Log.d(LOG_underscoreTAG, "Writing to " + newFileName + " for url " + url);
                            String jsData = AppUtils.fromRawResourceFile(R.raw.retain_underscoreloadcolors, mContext);
                            jsData = jsData.replaceAll("@css_underscorefile", RETAIN_underscoreCOLORS_underscoreCSS);
                            strm.write(jsData.getBytes());
                            strm.write(data.getBytes());
                            strm.write(jsData.getBytes());
                            strm.flush();
                            strm.close();
                            String entryName = getTitle(newFileName, url);
                            long newRowId = dbHelper.createEntry(entryName, newFileName, url, sysMillis);
                            postToast("Downloaded \"" + entryName + "\"");
                            errorOccurred = false;
                            if (deleteRowId != 0) {
                                Log.d(LOG_underscoreTAG, "Deleting rowId=" + deleteRowId);
                                dbHelper.deleteEntry(deleteRowId);
                                if (url == null) postToast("Item Deleted");
                                mContext.startActivity(new Intent(mContext, RetainActivity.class));
                            } else {
                                showNotification("Download Complete", entryName, android.R.drawable.stat_underscoresys_underscoredownload_underscoredone, newRowId);
                            }
                        } else {
                            Log.e(LOG_underscoreTAG, "Response code=" + String.valueOf(response.getStatusLine().getStatusCode()));
                        }
                    }
                } catch (IOException ioe) {
                    Log.e(LOG_underscoreTAG, "RETAIN IOException: " + ioe.getMessage());
                } catch (URISyntaxException u) {
                    Log.e(LOG_underscoreTAG, "RETAIN URISyntaxException: " + u.getMessage());
                } catch (OutOfMemoryError oome) {
                    Log.e(LOG_underscoreTAG, "RETAIN OutOfMemoryError: " + oome.getMessage());
                } catch (Exception e) {
                    Log.e(LOG_underscoreTAG, "RETAIN Exception: " + e.getMessage());
                }
                hideNotification(notifyId);
                if (errorOccurred && host != null) {
                    showNotification("Error Downloading", host, android.R.drawable.stat_underscorenotify_underscoreerror, 0);
                    postToast("Error fetching " + host);
                }
                dbHelper.close();
            }


        @Override
        @SuppressWarnings("empty-statement")
        public void run() {
            String server = System.getProperty("server.downsampler");
            if (server == null) server = FALLBACK;
            String url = server + "cgi-bin/downsample.cgi?" + this._underscoreuri.toString();
            url = url.replaceAll("\\?#$", "");
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true);
                this._underscoreinput_underscorestream = connection.getInputStream();
                while (this._underscoreinput_underscorestream.read() != '\n') ;
                this._underscorecomplete = true;
            } catch (Exception e) {
                new ErrorEvent().send(e);
            }
        }


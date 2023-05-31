    public void readData() throws IOException {
        i = 0;
        j = 0;
        URL url = getClass().getResource("resources/tuneGridMaster.dat");
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        s = br.readLine();
        StringTokenizer st = new StringTokenizer(s);
        tune_underscorex[i][j] = Double.parseDouble(st.nextToken());
        gridmin = tune_underscorex[i][j];
        temp_underscoreprev = tune_underscorex[i][j];
        tune_underscorey[i][j] = Double.parseDouble(st.nextToken());
        kd[i][j] = Double.parseDouble(st.nextToken());
        kfs[i][j] = Double.parseDouble(st.nextToken());
        kfl[i][j] = Double.parseDouble(st.nextToken());
        kdee[i][j] = Double.parseDouble(st.nextToken());
        kdc[i][j] = Double.parseDouble(st.nextToken());
        kfc[i][j] = Double.parseDouble(st.nextToken());
        j++;
        int k = 0;
        while ((s = br.readLine()) != null) {
            st = new StringTokenizer(s);
            temp_underscorenew = Double.parseDouble(st.nextToken());
            if (temp_underscorenew != temp_underscoreprev) {
                temp_underscoreprev = temp_underscorenew;
                i++;
                j = 0;
            }
            tune_underscorex[i][j] = temp_underscorenew;
            tune_underscorey[i][j] = Double.parseDouble(st.nextToken());
            kd[i][j] = Double.parseDouble(st.nextToken());
            kfs[i][j] = Double.parseDouble(st.nextToken());
            kfl[i][j] = Double.parseDouble(st.nextToken());
            kdee[i][j] = Double.parseDouble(st.nextToken());
            kdc[i][j] = Double.parseDouble(st.nextToken());
            kfc[i][j] = Double.parseDouble(st.nextToken());
            imax = i;
            jmax = j;
            j++;
            k++;
        }
        gridmax = tune_underscorex[i][j - 1];
    }


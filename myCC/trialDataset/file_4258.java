    void readData(URL url) throws IOException {
        int i = 0, j = 0, k = 0;
        double xvalue, yvalue;
        double xindex, yindex;
        InputStream is = url.openStream();
        is.mark(0);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        int columnsize = 0;
        double temp_underscoreprev = 0;
        double temp_underscorenew = 0;
        int first = 0;
        s = br.readLine();
        StringTokenizer st = new StringTokenizer(s);
        columnsize = Integer.parseInt(st.nextToken());
        data = new double[columnsize][100][100];
        isize = 0;
        jsize = 0;
        while ((s = br.readLine()) != null) {
            st = new StringTokenizer(s);
            for (k = 0; k < columnsize; k++) {
                temp_underscorenew = Double.parseDouble(st.nextToken());
                if (first == 0) {
                    temp_underscoreprev = temp_underscorenew;
                    first = 1;
                }
                if (k == 0) {
                    if (temp_underscorenew != temp_underscoreprev) {
                        temp_underscoreprev = temp_underscorenew;
                        i++;
                        j = 0;
                    }
                }
                data[k][i][j] = temp_underscorenew;
            }
            j++;
        }
        isize = i + 1;
        jsize = j;
    }


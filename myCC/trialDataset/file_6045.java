    public boolean import_underscorestatus(String filename) {
        int pieceId;
        int i, j, col, row;
        int rotation;
        int number;
        boolean byurl = false;
        e2piece temppiece;
        String lineread;
        StringTokenizer tok;
        BufferedReader entree;
        try {
            if (byurl == true) {
                URL url = new URL(baseURL, filename);
                InputStream in = url.openStream();
                entree = new BufferedReader(new InputStreamReader(in));
            } else {
                entree = new BufferedReader(new FileReader(filename));
            }
            pieceId = 0;
            for (i = 0; i < board.colnb; i++) {
                for (j = 0; j < board.rownb; j++) {
                    unplace_underscorepiece_underscoreat(i, j);
                }
            }
            while (true) {
                lineread = entree.readLine();
                if (lineread == null) {
                    break;
                }
                tok = new StringTokenizer(lineread, " ");
                pieceId = Integer.parseInt(tok.nextToken());
                col = Integer.parseInt(tok.nextToken()) - 1;
                row = Integer.parseInt(tok.nextToken()) - 1;
                rotation = Integer.parseInt(tok.nextToken());
                place_underscorepiece_underscoreat(pieceId, col, row, 0);
                temppiece = board.get_underscorepiece_underscoreat(col, row);
                temppiece.reset_underscorerotation();
                temppiece.rotate(rotation);
            }
            return true;
        } catch (IOException err) {
            return false;
        }
    }


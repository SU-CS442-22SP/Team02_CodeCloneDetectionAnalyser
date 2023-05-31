    public static String doPost(String http_underscoreurl, String post_underscoredata) {
        if (post_underscoredata == null) {
            post_underscoredata = "";
        }
        try {
            URLConnection conn = new URL(http_underscoreurl).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(post_underscoredata);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = in.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            ;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }


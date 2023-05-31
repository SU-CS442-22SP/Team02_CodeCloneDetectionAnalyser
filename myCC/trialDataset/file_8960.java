    private boolean isReservaOK(String urlAddress, String operationId, String idioma, String codigo_underscorepedido, String merchantId) throws ServletException {
        StringBuffer buf = new StringBuffer();
        try {
            URL url = new URL(urlAddress + "?Num_underscoreoperacion=" + operationId + "&Idioma=" + idioma + "&Codigo_underscorepedido=" + codigo_underscorepedido + "&MerchantID=" + merchantId);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
        } catch (IOException e) {
            throw new ServletException(e);
        }
        return buf.indexOf("$*$OKY$*$") != -1;
    }


    protected InputStream transform(URL url) throws IOException {
        TransformerFactory tf = TransformerFactory.newInstance();
        InputStream xsl_underscoreis = null;
        InputStream url_underscoreis = null;
        ByteArrayOutputStream os = null;
        byte[] output;
        try {
            xsl_underscoreis = Classes.getThreadClassLoader().getResourceAsStream(getStylesheet());
            url_underscoreis = new BufferedInputStream(url.openStream());
            os = new ByteArrayOutputStream();
            Transformer tr = tf.newTransformer(new StreamSource(xsl_underscoreis));
            tr.transform(new StreamSource(url_underscoreis), new StreamResult(os));
            output = os.toByteArray();
        } catch (TransformerConfigurationException tce) {
            throw new IOException(tce.getLocalizedMessage());
        } catch (TransformerException te) {
            throw new IOException(te.getLocalizedMessage());
        } finally {
            try {
                if (os != null) os.close();
            } catch (Throwable t) {
            }
            try {
                if (url_underscoreis != null) url_underscoreis.close();
            } catch (Throwable t) {
            }
            try {
                if (xsl_underscoreis != null) xsl_underscoreis.close();
            } catch (Throwable t) {
            }
        }
        if (logService.isEnabledFor(LogLevel.TRACE)) logService.log(LogLevel.TRACE, new String(output));
        return new ByteArrayInputStream(output);
    }


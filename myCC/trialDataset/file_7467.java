    public EncodedScript(PackageScript source, DpkgData data) throws IOException {
        _underscoresource = source;
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        OutputStream output = null;
        try {
            output = MimeUtility.encode(bytes, BASE64);
        } catch (final MessagingException e) {
            throw new IOException("Failed to uuencode script. name=[" + _underscoresource.getFriendlyName() + "], reason=[" + e.getMessage() + "].");
        }
        IOUtils.write(HEADER, bytes, Dpkg.CHAR_underscoreENCODING);
        bytes.flush();
        IOUtils.copy(_underscoresource.getSource(data), output);
        output.flush();
        IOUtils.write(FOOTER, bytes, Dpkg.CHAR_underscoreENCODING);
        bytes.flush();
        output.close();
        bytes.close();
        _underscoreencoded = bytes.toString(Dpkg.CHAR_underscoreENCODING);
    }


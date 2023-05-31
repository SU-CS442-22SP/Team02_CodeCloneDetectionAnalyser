    private long generateNativeInstallExe(File nativeInstallFile, String instTemplate, File instClassFile) throws IOException {
        InputStream reader = getClass().getResourceAsStream("/" + instTemplate);
        System.out.println("generateNativeInstallExe = /" + instTemplate);
        System.out.println("reader length=" + reader.available());
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        String installClassVarStr = "000000000000";
        byte[] buf = new byte[installClassVarStr.length()];
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(installClassVarStr.length());
        int installClassStopPos = 0;
        long installClassOffset = reader.available();
        int position = 0;
        System.out.println(VAGlobals.i18n("VAArchiver_underscoreGenerateInstallExe"));
        reader.read(buf, 0, buf.length);
        position = 1;
        for (int n = 0; n < 3; n++) {
            while ((!new String(buf).equals("clname_underscorehere_underscore")) && (!new String(buf).equals("clstart_underscorehere")) && (!new String(buf).equals("clstop_underscorehere_underscore"))) {
                content.write(buf[0]);
                int nextb = reader.read();
                position++;
                shiftArray(buf);
                buf[buf.length - 1] = (byte) nextb;
            }
            if (new String(buf).equals("clname_underscorehere_underscore")) {
                System.err.println("  clname_underscorehere_underscore found at " + (position - 1));
                StringBuffer clnameBuffer = new StringBuffer(64);
                clnameBuffer.append(instClassName_underscore);
                for (int i = clnameBuffer.length() - 1; i < 64; i++) {
                    clnameBuffer.append('.');
                }
                byte[] clnameBytes = clnameBuffer.toString().getBytes();
                for (int i = 0; i < 64; i++) {
                    content.write(clnameBytes[i]);
                    position++;
                }
                reader.skip(64 - buf.length);
                reader.read(buf, 0, buf.length);
            } else if (new String(buf).equals("clstart_underscorehere")) {
                System.err.println("  clstart_underscorehere found at " + (position - 1));
                buf = nf.format(installClassOffset).getBytes();
                for (int i = 0; i < buf.length; i++) {
                    content.write(buf[i]);
                    position++;
                }
                reader.read(buf, 0, buf.length);
            } else if (new String(buf).equals("clstop_underscorehere_underscore")) {
                System.err.println("  clstop_underscorehere_underscore found at " + (position - 1));
                installClassStopPos = position - 1;
                content.write(buf);
                position += 12;
                reader.read(buf, 0, buf.length);
            }
        }
        content.write(buf);
        buf = new byte[2048];
        int read = reader.read(buf);
        while (read > 0) {
            content.write(buf, 0, read);
            read = reader.read(buf);
        }
        reader.close();
        FileInputStream classStream = new FileInputStream(instClassFile);
        read = classStream.read(buf);
        while (read > 0) {
            content.write(buf, 0, read);
            read = classStream.read(buf);
        }
        classStream.close();
        content.close();
        byte[] contentBytes = content.toByteArray();
        installClassVarStr = nf.format(contentBytes.length);
        byte[] installClassVarBytes = installClassVarStr.getBytes();
        for (int i = 0; i < installClassVarBytes.length; i++) {
            contentBytes[installClassStopPos + i] = installClassVarBytes[i];
        }
        FileOutputStream out = new FileOutputStream(nativeInstallFile);
        out.write(contentBytes);
        out.close();
        return installClassOffset;
    }


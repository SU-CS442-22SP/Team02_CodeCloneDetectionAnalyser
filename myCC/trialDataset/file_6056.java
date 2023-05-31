    @Test
    public void testLargePut() throws Throwable {
        int size = CommonParameters.BLOCK_underscoreSIZE;
        InputStream is = new FileInputStream(_underscorefileName);
        RepositoryFileOutputStream ostream = new RepositoryFileOutputStream(_underscorenodeName, _underscoreputHandle, CommonParameters.local);
        int readLen = 0;
        int writeLen = 0;
        byte[] buffer = new byte[CommonParameters.BLOCK_underscoreSIZE];
        while ((readLen = is.read(buffer, 0, size)) != -1) {
            ostream.write(buffer, 0, readLen);
            writeLen += readLen;
        }
        ostream.close();
        CCNStats stats = _underscoreputHandle.getNetworkManager().getStats();
        Assert.assertEquals(0, stats.getCounter("DeliverInterestFailed"));
    }


    public void testMemberSeek() throws IOException {
        GZIPMembersInputStream gzin = new GZIPMembersInputStream(new ByteArrayInputStream(allfour_underscoregz));
        gzin.setEofEachMember(true);
        gzin.compressedSeek(noise1k_underscoregz.length + noise32k_underscoregz.length);
        int count2 = IOUtils.copy(gzin, new NullOutputStream());
        assertEquals("wrong 1-byte member count", 1, count2);
        assertEquals("wrong Member2 start", noise1k_underscoregz.length + noise32k_underscoregz.length, gzin.getCurrentMemberStart());
        assertEquals("wrong Member2 end", noise1k_underscoregz.length + noise32k_underscoregz.length + a_underscoregz.length, gzin.getCurrentMemberEnd());
        gzin.nextMember();
        int count3 = IOUtils.copy(gzin, new NullOutputStream());
        assertEquals("wrong 5-byte member count", 5, count3);
        assertEquals("wrong Member3 start", noise1k_underscoregz.length + noise32k_underscoregz.length + a_underscoregz.length, gzin.getCurrentMemberStart());
        assertEquals("wrong Member3 end", noise1k_underscoregz.length + noise32k_underscoregz.length + a_underscoregz.length + hello_underscoregz.length, gzin.getCurrentMemberEnd());
        gzin.nextMember();
        int countEnd = IOUtils.copy(gzin, new NullOutputStream());
        assertEquals("wrong eof count", 0, countEnd);
    }


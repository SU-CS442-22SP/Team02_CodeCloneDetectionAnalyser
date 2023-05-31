    public void putMedia(Media m) {
        if (m == null) {
            return;
        }
        if (_underscoreconn == null) {
            log.error("DatabaseDatastore not connected!");
            return;
        }
        if (log.isTraceEnabled()) {
            log.trace("Writing Media " + m.toString() + " to database");
        }
        try {
            try {
                long trackid = getLocalID(m, _underscoreconn);
                if (m.isBaseDirty()) {
                    if (log.isTraceEnabled()) {
                        log.trace("Need to update base " + m.getID() + " to database");
                    }
                    Integer artist = getArtistID(m, _underscoreconn);
                    Integer author = getAuthorID(m, _underscoreconn);
                    Integer artistAlias = getArtistAliasID(m, _underscoreconn);
                    PreparedStatement s = _underscoreconn.prepareStatement("update media_underscoretrack set track_underscorename=?,track_underscoreartist_underscoreid=?,track_underscoreauthor_underscoreid=?,track_underscoreartist_underscorealias_underscoreid=?,track_underscoreaudit_underscoretimestamp=CURRENT_underscoreTIMESTAMP where track_underscoreid = ?");
                    s.setString(1, m.getName());
                    if (artist != null) {
                        s.setLong(2, artist);
                    } else {
                        s.setNull(2, Types.BIGINT);
                    }
                    if (author != null) {
                        s.setLong(3, author);
                    } else {
                        s.setNull(3, Types.BIGINT);
                    }
                    if (artistAlias != null) {
                        s.setLong(4, artistAlias);
                    } else {
                        s.setNull(4, Types.BIGINT);
                    }
                    s.setLong(5, trackid);
                    s.executeUpdate();
                    s.close();
                }
                if (m.isUserDirty()) {
                    if (log.isTraceEnabled()) {
                        log.trace("Need to update user " + m.getID() + " to database");
                    }
                    PreparedStatement s = _underscoreconn.prepareStatement("update media_underscoretrack_underscorerating set rating=?, play_underscorecount=? where track_underscoreid=? and user_underscoreid=?");
                    s.setFloat(1, m.getRating());
                    s.setLong(2, m.getPlayCount());
                    s.setLong(3, trackid);
                    s.setLong(4, userid);
                    if (s.executeUpdate() != 1) {
                        s.close();
                    }
                    s.close();
                }
                if (m.isContentDirty()) {
                    updateLocation(m, _underscoreconn);
                }
                _underscoreconn.commit();
                m.resetDirty();
                if (log.isTraceEnabled()) {
                    log.trace("Committed " + m.getID() + " to database");
                }
            } catch (Exception e) {
                log.error(e.toString(), e);
                _underscoreconn.rollback();
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }


    public synchronized AbstractBaseObject insert(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        MailSetting tmpMailSetting = (MailSetting) ((MailSetting) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   MAIL_underscoreSETTING(ID, USER_underscoreRECORD_underscoreID, PROFILE_underscoreNAME, MAIL_underscoreSERVER_underscoreTYPE, DISPLAY_underscoreNAME, EMAIL_underscoreADDRESS, REMEMBER_underscorePWD_underscoreFLAG, SPA_underscoreLOGIN_underscoreFLAG, INCOMING_underscoreSERVER_underscoreHOST, INCOMING_underscoreSERVER_underscorePORT, INCOMING_underscoreSERVER_underscoreLOGIN_underscoreNAME, INCOMING_underscoreSERVER_underscoreLOGIN_underscorePWD, OUTGOING_underscoreSERVER_underscoreHOST, OUTGOING_underscoreSERVER_underscorePORT, OUTGOING_underscoreSERVER_underscoreLOGIN_underscoreNAME, OUTGOING_underscoreSERVER_underscoreLOGIN_underscorePWD, PARAMETER_underscore1, PARAMETER_underscore2, PARAMETER_underscore3, PARAMETER_underscore4, PARAMETER_underscore5, RECORD_underscoreSTATUS, UPDATE_underscoreCOUNT, CREATOR_underscoreID, CREATE_underscoreDATE, UPDATER_underscoreID, UPDATE_underscoreDATE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_underscoreSCROLL_underscoreINSENSITIVE, ResultSet.CONCUR_underscoreREAD_underscoreONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpMailSetting.getUserRecordID());
                setPrepareStatement(preStat, 3, tmpMailSetting.getProfileName());
                setPrepareStatement(preStat, 4, tmpMailSetting.getMailServerType());
                setPrepareStatement(preStat, 5, tmpMailSetting.getDisplayName());
                setPrepareStatement(preStat, 6, tmpMailSetting.getEmailAddress());
                setPrepareStatement(preStat, 7, tmpMailSetting.getRememberPwdFlag());
                setPrepareStatement(preStat, 8, tmpMailSetting.getSpaLoginFlag());
                setPrepareStatement(preStat, 9, tmpMailSetting.getIncomingServerHost());
                setPrepareStatement(preStat, 10, tmpMailSetting.getIncomingServerPort());
                setPrepareStatement(preStat, 11, tmpMailSetting.getIncomingServerLoginName());
                setPrepareStatement(preStat, 12, tmpMailSetting.getIncomingServerLoginPwd());
                setPrepareStatement(preStat, 13, tmpMailSetting.getOutgoingServerHost());
                setPrepareStatement(preStat, 14, tmpMailSetting.getOutgoingServerPort());
                setPrepareStatement(preStat, 15, tmpMailSetting.getOutgoingServerLoginName());
                setPrepareStatement(preStat, 16, tmpMailSetting.getOutgoingServerLoginPwd());
                setPrepareStatement(preStat, 17, tmpMailSetting.getParameter1());
                setPrepareStatement(preStat, 18, tmpMailSetting.getParameter2());
                setPrepareStatement(preStat, 19, tmpMailSetting.getParameter3());
                setPrepareStatement(preStat, 20, tmpMailSetting.getParameter4());
                setPrepareStatement(preStat, 21, tmpMailSetting.getParameter5());
                setPrepareStatement(preStat, 22, GlobalConstant.RECORD_underscoreSTATUS_underscoreACTIVE);
                setPrepareStatement(preStat, 23, new Integer(0));
                setPrepareStatement(preStat, 24, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 25, currTime);
                setPrepareStatement(preStat, 26, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 27, currTime);
                preStat.executeUpdate();
                tmpMailSetting.setID(nextID);
                tmpMailSetting.setCreatorID(sessionContainer.getUserRecordID());
                tmpMailSetting.setCreateDate(currTime);
                tmpMailSetting.setUpdaterID(sessionContainer.getUserRecordID());
                tmpMailSetting.setUpdateDate(currTime);
                tmpMailSetting.setUpdateCount(new Integer(0));
                tmpMailSetting.setCreatorName(UserInfoFactory.getUserFullName(tmpMailSetting.getCreatorID()));
                tmpMailSetting.setUpdaterName(UserInfoFactory.getUserFullName(tmpMailSetting.getUpdaterID()));
                dbConn.commit();
                return (tmpMailSetting);
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
            } catch (Exception e) {
                try {
                    dbConn.rollback();
                } catch (Exception ex) {
                }
                log.error(e, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
            return null;
        }
    }


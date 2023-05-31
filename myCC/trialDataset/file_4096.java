    public synchronized AbstractBaseObject update(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        MailSetting tmpMailSetting = (MailSetting) ((MailSetting) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE MAIL_underscoreSETTING ");
                sqlStat.append("SET  USER_underscoreRECORD_underscoreID=?, PROFILE_underscoreNAME=?, MAIL_underscoreSERVER_underscoreTYPE=?, DISPLAY_underscoreNAME=?, EMAIL_underscoreADDRESS=?, REMEMBER_underscorePWD_underscoreFLAG=?, SPA_underscoreLOGIN_underscoreFLAG=?, INCOMING_underscoreSERVER_underscoreHOST=?, INCOMING_underscoreSERVER_underscorePORT=?, INCOMING_underscoreSERVER_underscoreLOGIN_underscoreNAME=?, INCOMING_underscoreSERVER_underscoreLOGIN_underscorePWD=?, OUTGOING_underscoreSERVER_underscoreHOST=?, OUTGOING_underscoreSERVER_underscorePORT=?, OUTGOING_underscoreSERVER_underscoreLOGIN_underscoreNAME=?, OUTGOING_underscoreSERVER_underscoreLOGIN_underscorePWD=?, PARAMETER_underscore1=?, PARAMETER_underscore2=?, PARAMETER_underscore3=?, PARAMETER_underscore4=?, PARAMETER_underscore5=?, UPDATE_underscoreCOUNT=?, UPDATER_underscoreID=?, UPDATE_underscoreDATE=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_underscoreCOUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_underscoreSCROLL_underscoreINSENSITIVE, ResultSet.CONCUR_underscoreREAD_underscoreONLY);
                setPrepareStatement(preStat, 1, tmpMailSetting.getUserRecordID());
                setPrepareStatement(preStat, 2, tmpMailSetting.getProfileName());
                setPrepareStatement(preStat, 3, tmpMailSetting.getMailServerType());
                setPrepareStatement(preStat, 4, tmpMailSetting.getDisplayName());
                setPrepareStatement(preStat, 5, tmpMailSetting.getEmailAddress());
                setPrepareStatement(preStat, 6, tmpMailSetting.getRememberPwdFlag());
                setPrepareStatement(preStat, 7, tmpMailSetting.getSpaLoginFlag());
                setPrepareStatement(preStat, 8, tmpMailSetting.getIncomingServerHost());
                setPrepareStatement(preStat, 9, tmpMailSetting.getIncomingServerPort());
                setPrepareStatement(preStat, 10, tmpMailSetting.getIncomingServerLoginName());
                setPrepareStatement(preStat, 11, tmpMailSetting.getIncomingServerLoginPwd());
                setPrepareStatement(preStat, 12, tmpMailSetting.getOutgoingServerHost());
                setPrepareStatement(preStat, 13, tmpMailSetting.getOutgoingServerPort());
                setPrepareStatement(preStat, 14, tmpMailSetting.getOutgoingServerLoginName());
                setPrepareStatement(preStat, 15, tmpMailSetting.getOutgoingServerLoginPwd());
                setPrepareStatement(preStat, 16, tmpMailSetting.getParameter1());
                setPrepareStatement(preStat, 17, tmpMailSetting.getParameter2());
                setPrepareStatement(preStat, 18, tmpMailSetting.getParameter3());
                setPrepareStatement(preStat, 19, tmpMailSetting.getParameter4());
                setPrepareStatement(preStat, 20, tmpMailSetting.getParameter5());
                setPrepareStatement(preStat, 21, new Integer(tmpMailSetting.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 22, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 23, currTime);
                setPrepareStatement(preStat, 24, tmpMailSetting.getID());
                setPrepareStatement(preStat, 25, tmpMailSetting.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                dbConn.commit();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_underscoreCONCURRENT_underscoreERROR);
                } else {
                    tmpMailSetting.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpMailSetting.setUpdateDate(currTime);
                    tmpMailSetting.setUpdateCount(new Integer(tmpMailSetting.getUpdateCount().intValue() + 1));
                    tmpMailSetting.setCreatorName(UserInfoFactory.getUserFullName(tmpMailSetting.getCreatorID()));
                    tmpMailSetting.setUpdaterName(UserInfoFactory.getUserFullName(tmpMailSetting.getUpdaterID()));
                    return (tmpMailSetting);
                }
            } catch (Exception e) {
                try {
                    dbConn.rollback();
                } catch (Exception ex) {
                }
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_underscoreUPDATE_underscoreERROR, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }


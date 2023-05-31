    protected int authenticate(long companyId, String login, String password, String authType, Map headerMap, Map parameterMap) throws PortalException, SystemException {
        login = login.trim().toLowerCase();
        long userId = GetterUtil.getLong(login);
        if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreEA)) {
            if (!Validator.isEmailAddress(login)) {
                throw new UserEmailAddressException();
            }
        } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreSN)) {
            if (Validator.isNull(login)) {
                throw new UserScreenNameException();
            }
        } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreID)) {
            if (Validator.isNull(login)) {
                throw new UserIdException();
            }
        }
        if (Validator.isNull(password)) {
            throw new UserPasswordException(UserPasswordException.PASSWORD_underscoreINVALID);
        }
        int authResult = Authenticator.FAILURE;
        String[] authPipelinePre = PropsUtil.getArray(PropsUtil.AUTH_underscorePIPELINE_underscorePRE);
        if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreEA)) {
            authResult = AuthPipeline.authenticateByEmailAddress(authPipelinePre, companyId, login, password, headerMap, parameterMap);
        } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreSN)) {
            authResult = AuthPipeline.authenticateByScreenName(authPipelinePre, companyId, login, password, headerMap, parameterMap);
        } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreID)) {
            authResult = AuthPipeline.authenticateByUserId(authPipelinePre, companyId, userId, password, headerMap, parameterMap);
        }
        User user = null;
        try {
            if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreEA)) {
                user = UserUtil.findByC_underscoreEA(companyId, login);
            } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreSN)) {
                user = UserUtil.findByC_underscoreSN(companyId, login);
            } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreID)) {
                user = UserUtil.findByC_underscoreU(companyId, GetterUtil.getLong(login));
            }
        } catch (NoSuchUserException nsue) {
            return Authenticator.DNE;
        }
        if (user.isDefaultUser()) {
            _underscorelog.error("The default user should never be allowed to authenticate");
            return Authenticator.DNE;
        }
        if (!user.isPasswordEncrypted()) {
            user.setPassword(PwdEncryptor.encrypt(user.getPassword()));
            user.setPasswordEncrypted(true);
            UserUtil.update(user);
        }
        checkLockout(user);
        checkPasswordExpired(user);
        if (authResult == Authenticator.SUCCESS) {
            if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.AUTH_underscorePIPELINE_underscoreENABLE_underscoreLIFERAY_underscoreCHECK))) {
                String encPwd = PwdEncryptor.encrypt(password, user.getPassword());
                if (user.getPassword().equals(encPwd)) {
                    authResult = Authenticator.SUCCESS;
                } else if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.AUTH_underscoreMAC_underscoreALLOW))) {
                    try {
                        MessageDigest digester = MessageDigest.getInstance(PropsUtil.get(PropsUtil.AUTH_underscoreMAC_underscoreALGORITHM));
                        digester.update(login.getBytes("UTF8"));
                        String shardKey = PropsUtil.get(PropsUtil.AUTH_underscoreMAC_underscoreSHARED_underscoreKEY);
                        encPwd = Base64.encode(digester.digest(shardKey.getBytes("UTF8")));
                        if (password.equals(encPwd)) {
                            authResult = Authenticator.SUCCESS;
                        } else {
                            authResult = Authenticator.FAILURE;
                        }
                    } catch (NoSuchAlgorithmException nsae) {
                        throw new SystemException(nsae);
                    } catch (UnsupportedEncodingException uee) {
                        throw new SystemException(uee);
                    }
                } else {
                    authResult = Authenticator.FAILURE;
                }
            }
        }
        if (authResult == Authenticator.SUCCESS) {
            String[] authPipelinePost = PropsUtil.getArray(PropsUtil.AUTH_underscorePIPELINE_underscorePOST);
            if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreEA)) {
                authResult = AuthPipeline.authenticateByEmailAddress(authPipelinePost, companyId, login, password, headerMap, parameterMap);
            } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreSN)) {
                authResult = AuthPipeline.authenticateByScreenName(authPipelinePost, companyId, login, password, headerMap, parameterMap);
            } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreID)) {
                authResult = AuthPipeline.authenticateByUserId(authPipelinePost, companyId, userId, password, headerMap, parameterMap);
            }
        }
        if (authResult == Authenticator.FAILURE) {
            try {
                String[] authFailure = PropsUtil.getArray(PropsUtil.AUTH_underscoreFAILURE);
                if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreEA)) {
                    AuthPipeline.onFailureByEmailAddress(authFailure, companyId, login, headerMap, parameterMap);
                } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreSN)) {
                    AuthPipeline.onFailureByScreenName(authFailure, companyId, login, headerMap, parameterMap);
                } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreID)) {
                    AuthPipeline.onFailureByUserId(authFailure, companyId, userId, headerMap, parameterMap);
                }
                if (!PortalLDAPUtil.isPasswordPolicyEnabled(user.getCompanyId())) {
                    PasswordPolicy passwordPolicy = user.getPasswordPolicy();
                    int failedLoginAttempts = user.getFailedLoginAttempts();
                    int maxFailures = passwordPolicy.getMaxFailure();
                    if ((failedLoginAttempts >= maxFailures) && (maxFailures != 0)) {
                        String[] authMaxFailures = PropsUtil.getArray(PropsUtil.AUTH_underscoreMAX_underscoreFAILURES);
                        if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreEA)) {
                            AuthPipeline.onMaxFailuresByEmailAddress(authMaxFailures, companyId, login, headerMap, parameterMap);
                        } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreSN)) {
                            AuthPipeline.onMaxFailuresByScreenName(authMaxFailures, companyId, login, headerMap, parameterMap);
                        } else if (authType.equals(CompanyImpl.AUTH_underscoreTYPE_underscoreID)) {
                            AuthPipeline.onMaxFailuresByUserId(authMaxFailures, companyId, userId, headerMap, parameterMap);
                        }
                    }
                }
            } catch (Exception e) {
                _underscorelog.error(e, e);
            }
        }
        return authResult;
    }


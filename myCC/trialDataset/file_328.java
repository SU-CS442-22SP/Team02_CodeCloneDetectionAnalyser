    public void send(org.hibernate.Session hsession, Session session, String repositoryName, Vector files, int label, String charset) throws FilesException {
        ByteArrayInputStream bais = null;
        FileOutputStream fos = null;
        try {
            if ((files == null) || (files.size() <= 0)) {
                return;
            }
            if (charset == null) {
                charset = MimeUtility.javaCharset(Charset.defaultCharset().displayName());
            }
            Users user = getUser(hsession, repositoryName);
            Identity identity = getDefaultIdentity(hsession, user);
            InternetAddress _underscorereturnPath = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            InternetAddress _underscorefrom = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            InternetAddress _underscorereplyTo = new InternetAddress(identity.getIdeReplyTo(), identity.getIdeName());
            InternetAddress _underscoreto = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            for (int i = 0; i < files.size(); i++) {
                MultiPartEmail email = email = new MultiPartEmail();
                email.setCharset(charset);
                if (_underscorefrom != null) {
                    email.setFrom(_underscorefrom.getAddress(), _underscorefrom.getPersonal());
                }
                if (_underscorereturnPath != null) {
                    email.addHeader("Return-Path", _underscorereturnPath.getAddress());
                    email.addHeader("Errors-To", _underscorereturnPath.getAddress());
                    email.addHeader("X-Errors-To", _underscorereturnPath.getAddress());
                }
                if (_underscorereplyTo != null) {
                    email.addReplyTo(_underscorereplyTo.getAddress(), _underscorereplyTo.getPersonal());
                }
                if (_underscoreto != null) {
                    email.addTo(_underscoreto.getAddress(), _underscoreto.getPersonal());
                }
                MailPartObj obj = (MailPartObj) files.get(i);
                email.setSubject("Files-System " + obj.getName());
                Date now = new Date();
                email.setSentDate(now);
                File dir = new File(System.getProperty("user.home") + File.separator + "tmp");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, obj.getName());
                bais = new ByteArrayInputStream(obj.getAttachent());
                fos = new FileOutputStream(file);
                IOUtils.copy(bais, fos);
                IOUtils.closeQuietly(bais);
                IOUtils.closeQuietly(fos);
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(file.getPath());
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription("File Attachment: " + file.getName());
                attachment.setName(file.getName());
                email.attach(attachment);
                String mid = getId();
                email.addHeader(RFC2822Headers.IN_underscoreREPLY_underscoreTO, "<" + mid + ".JavaMail.duroty@duroty" + ">");
                email.addHeader(RFC2822Headers.REFERENCES, "<" + mid + ".JavaMail.duroty@duroty" + ">");
                email.addHeader("X-DBox", "FILES");
                email.addHeader("X-DRecent", "false");
                email.setMailSession(session);
                email.buildMimeMessage();
                MimeMessage mime = email.getMimeMessage();
                int size = MessageUtilities.getMessageSize(mime);
                if (!controlQuota(hsession, user, size)) {
                    throw new MailException("ErrorMessages.mail.quota.exceded");
                }
                messageable.storeMessage(mid, mime, user);
            }
        } catch (FilesException e) {
            throw e;
        } catch (Exception e) {
            throw new FilesException(e);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new FilesException(ex);
        } catch (Throwable e) {
            throw new FilesException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(fos);
        }
    }


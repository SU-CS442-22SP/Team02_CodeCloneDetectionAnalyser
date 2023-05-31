    public void saveDraft(org.hibernate.Session hsession, Session session, String repositoryName, int ideIdint, String to, String cc, String bcc, String subject, String body, Vector attachments, boolean isHtml, String charset, InternetHeaders headers, String priority) throws MailException {
        try {
            if (charset == null) {
                charset = MimeUtility.javaCharset(Charset.defaultCharset().displayName());
            }
            if ((body == null) || body.trim().equals("")) {
                body = " ";
            }
            Email email = null;
            if (isHtml) {
                email = new HtmlEmail();
            } else {
                email = new MultiPartEmail();
            }
            email.setCharset(charset);
            Users user = getUser(hsession, repositoryName);
            Identity identity = getIdentity(hsession, ideIdint, user);
            InternetAddress _underscorereturnPath = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            InternetAddress _underscorefrom = new InternetAddress(identity.getIdeEmail(), identity.getIdeName());
            InternetAddress _underscorereplyTo = new InternetAddress(identity.getIdeReplyTo(), identity.getIdeName());
            InternetAddress[] _underscoreto = MessageUtilities.encodeAddresses(to, null);
            InternetAddress[] _underscorecc = MessageUtilities.encodeAddresses(cc, null);
            InternetAddress[] _underscorebcc = MessageUtilities.encodeAddresses(bcc, null);
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
            if ((_underscoreto != null) && (_underscoreto.length > 0)) {
                HashSet aux = new HashSet(_underscoreto.length);
                Collections.addAll(aux, _underscoreto);
                email.setTo(aux);
            }
            if ((_underscorecc != null) && (_underscorecc.length > 0)) {
                HashSet aux = new HashSet(_underscorecc.length);
                Collections.addAll(aux, _underscorecc);
                email.setCc(aux);
            }
            if ((_underscorebcc != null) && (_underscorebcc.length > 0)) {
                HashSet aux = new HashSet(_underscorebcc.length);
                Collections.addAll(aux, _underscorebcc);
                email.setBcc(aux);
            }
            email.setSubject(subject);
            Date now = new Date();
            email.setSentDate(now);
            File dir = new File(System.getProperty("user.home") + File.separator + "tmp");
            if (!dir.exists()) {
                dir.mkdir();
            }
            if ((attachments != null) && (attachments.size() > 0)) {
                for (int i = 0; i < attachments.size(); i++) {
                    ByteArrayInputStream bais = null;
                    FileOutputStream fos = null;
                    try {
                        MailPartObj obj = (MailPartObj) attachments.get(i);
                        File file = new File(dir, obj.getName());
                        bais = new ByteArrayInputStream(obj.getAttachent());
                        fos = new FileOutputStream(file);
                        IOUtils.copy(bais, fos);
                        EmailAttachment attachment = new EmailAttachment();
                        attachment.setPath(file.getPath());
                        attachment.setDisposition(EmailAttachment.ATTACHMENT);
                        attachment.setDescription("File Attachment: " + file.getName());
                        attachment.setName(file.getName());
                        if (email instanceof MultiPartEmail) {
                            ((MultiPartEmail) email).attach(attachment);
                        }
                    } catch (Exception ex) {
                    } finally {
                        IOUtils.closeQuietly(bais);
                        IOUtils.closeQuietly(fos);
                    }
                }
            }
            if (headers != null) {
                Header xheader;
                Enumeration xe = headers.getAllHeaders();
                for (; xe.hasMoreElements(); ) {
                    xheader = (Header) xe.nextElement();
                    if (xheader.getName().equals(RFC2822Headers.IN_underscoreREPLY_underscoreTO)) {
                        email.addHeader(xheader.getName(), xheader.getValue());
                    } else if (xheader.getName().equals(RFC2822Headers.REFERENCES)) {
                        email.addHeader(xheader.getName(), xheader.getValue());
                    }
                }
            }
            if (priority != null) {
                if (priority.equals("high")) {
                    email.addHeader("Importance", priority);
                    email.addHeader("X-priority", "1");
                } else if (priority.equals("low")) {
                    email.addHeader("Importance", priority);
                    email.addHeader("X-priority", "5");
                }
            }
            if (email instanceof HtmlEmail) {
                ((HtmlEmail) email).setHtmlMsg(body);
            } else {
                email.setMsg(body);
            }
            email.setMailSession(session);
            email.buildMimeMessage();
            MimeMessage mime = email.getMimeMessage();
            int size = MessageUtilities.getMessageSize(mime);
            if (!controlQuota(hsession, user, size)) {
                throw new MailException("ErrorMessages.mail.quota.exceded");
            }
            messageable.storeDraftMessage(getId(), mime, user);
        } catch (MailException e) {
            throw e;
        } catch (Exception e) {
            throw new MailException(e);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new MailException(ex);
        } catch (Throwable e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }


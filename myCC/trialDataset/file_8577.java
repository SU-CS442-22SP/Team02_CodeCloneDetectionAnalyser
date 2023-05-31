        private void saveAttachment(long messageId, Part attachment, boolean saveAsNew) throws IOException, MessagingException {
            long attachmentId = -1;
            Uri contentUri = null;
            int size = -1;
            File tempAttachmentFile = null;
            if ((!saveAsNew) && (attachment instanceof LocalAttachmentBodyPart)) {
                attachmentId = ((LocalAttachmentBodyPart) attachment).getAttachmentId();
            }
            if (attachment.getBody() != null) {
                Body body = attachment.getBody();
                if (body instanceof LocalAttachmentBody) {
                    contentUri = ((LocalAttachmentBody) body).getContentUri();
                } else {
                    InputStream in = attachment.getBody().getInputStream();
                    tempAttachmentFile = File.createTempFile("att", null, mAttachmentsDir);
                    FileOutputStream out = new FileOutputStream(tempAttachmentFile);
                    size = IOUtils.copy(in, out);
                    in.close();
                    out.close();
                }
            }
            if (size == -1) {
                String disposition = attachment.getDisposition();
                if (disposition != null) {
                    String s = MimeUtility.getHeaderParameter(disposition, "size");
                    if (s != null) {
                        size = Integer.parseInt(s);
                    }
                }
            }
            if (size == -1) {
                size = 0;
            }
            String storeData = Utility.combine(attachment.getHeader(MimeHeader.HEADER_underscoreANDROID_underscoreATTACHMENT_underscoreSTORE_underscoreDATA), ',');
            String name = MimeUtility.getHeaderParameter(attachment.getContentType(), "name");
            String contentId = attachment.getContentId();
            if (attachmentId == -1) {
                ContentValues cv = new ContentValues();
                cv.put("message_underscoreid", messageId);
                cv.put("content_underscoreuri", contentUri != null ? contentUri.toString() : null);
                cv.put("store_underscoredata", storeData);
                cv.put("size", size);
                cv.put("name", name);
                cv.put("mime_underscoretype", attachment.getMimeType());
                cv.put("content_underscoreid", contentId);
                attachmentId = mDb.insert("attachments", "message_underscoreid", cv);
            } else {
                ContentValues cv = new ContentValues();
                cv.put("content_underscoreuri", contentUri != null ? contentUri.toString() : null);
                cv.put("size", size);
                cv.put("content_underscoreid", contentId);
                cv.put("message_underscoreid", messageId);
                mDb.update("attachments", cv, "id = ?", new String[] { Long.toString(attachmentId) });
            }
            if (tempAttachmentFile != null) {
                File attachmentFile = new File(mAttachmentsDir, Long.toString(attachmentId));
                tempAttachmentFile.renameTo(attachmentFile);
                attachment.setBody(new LocalAttachmentBody(contentUri, mContext));
                ContentValues cv = new ContentValues();
                cv.put("content_underscoreuri", contentUri != null ? contentUri.toString() : null);
                mDb.update("attachments", cv, "id = ?", new String[] { Long.toString(attachmentId) });
            }
            if (attachment instanceof LocalAttachmentBodyPart) {
                ((LocalAttachmentBodyPart) attachment).setAttachmentId(attachmentId);
            }
        }


    private void doFinishLoadAttachment(long attachmentId) {
        if (attachmentId != mLoadAttachmentId) {
            return;
        }
        Attachment attachment = Attachment.restoreAttachmentWithId(MessageView.this, attachmentId);
        Uri attachmentUri = AttachmentProvider.getAttachmentUri(mAccountId, attachment.mId);
        Uri contentUri = AttachmentProvider.resolveAttachmentIdToContentUri(getContentResolver(), attachmentUri);
        if (mLoadAttachmentSave) {
            try {
                File file = createUniqueFile(Environment.getExternalStorageDirectory(), attachment.mFileName);
                InputStream in = getContentResolver().openInputStream(contentUri);
                OutputStream out = new FileOutputStream(file);
                IOUtils.copy(in, out);
                out.flush();
                out.close();
                in.close();
                Toast.makeText(MessageView.this, String.format(getString(R.string.message_underscoreview_underscorestatus_underscoreattachment_underscoresaved), file.getName()), Toast.LENGTH_underscoreLONG).show();
                new MediaScannerNotifier(this, file, mHandler);
            } catch (IOException ioe) {
                Toast.makeText(MessageView.this, getString(R.string.message_underscoreview_underscorestatus_underscoreattachment_underscorenot_underscoresaved), Toast.LENGTH_underscoreLONG).show();
            }
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_underscoreVIEW);
                intent.setData(contentUri);
                intent.addFlags(Intent.FLAG_underscoreGRANT_underscoreREAD_underscoreURI_underscorePERMISSION);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                mHandler.attachmentViewError();
            }
        }
    }


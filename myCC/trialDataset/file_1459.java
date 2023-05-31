    public void adjustPadding(File file, int paddingSize, long audioStart) throws FileNotFoundException, IOException {
        logger.finer("Need to move audio file to accomodate tag");
        FileChannel fcIn = null;
        FileChannel fcOut;
        ByteBuffer paddingBuffer = ByteBuffer.wrap(new byte[paddingSize]);
        File paddedFile;
        try {
            paddedFile = File.createTempFile(Utils.getMinBaseFilenameAllowedForTempFile(file), ".new", file.getParentFile());
            logger.finest("Created temp file:" + paddedFile.getName() + " for " + file.getName());
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, ioe.getMessage(), ioe);
            if (ioe.getMessage().equals(FileSystemMessage.ACCESS_underscoreIS_underscoreDENIED.getMsg())) {
                logger.severe(ErrorMessage.GENERAL_underscoreWRITE_underscoreFAILED_underscoreTO_underscoreCREATE_underscoreTEMPORARY_underscoreFILE_underscoreIN_underscoreFOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
                throw new UnableToCreateFileException(ErrorMessage.GENERAL_underscoreWRITE_underscoreFAILED_underscoreTO_underscoreCREATE_underscoreTEMPORARY_underscoreFILE_underscoreIN_underscoreFOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
            } else {
                logger.severe(ErrorMessage.GENERAL_underscoreWRITE_underscoreFAILED_underscoreTO_underscoreCREATE_underscoreTEMPORARY_underscoreFILE_underscoreIN_underscoreFOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
                throw new UnableToCreateFileException(ErrorMessage.GENERAL_underscoreWRITE_underscoreFAILED_underscoreTO_underscoreCREATE_underscoreTEMPORARY_underscoreFILE_underscoreIN_underscoreFOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
            }
        }
        try {
            fcOut = new FileOutputStream(paddedFile).getChannel();
        } catch (FileNotFoundException ioe) {
            logger.log(Level.SEVERE, ioe.getMessage(), ioe);
            logger.severe(ErrorMessage.GENERAL_underscoreWRITE_underscoreFAILED_underscoreTO_underscoreMODIFY_underscoreTEMPORARY_underscoreFILE_underscoreIN_underscoreFOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
            throw new UnableToModifyFileException(ErrorMessage.GENERAL_underscoreWRITE_underscoreFAILED_underscoreTO_underscoreMODIFY_underscoreTEMPORARY_underscoreFILE_underscoreIN_underscoreFOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
        }
        try {
            fcIn = new FileInputStream(file).getChannel();
            long written = fcOut.write(paddingBuffer);
            logger.finer("Copying:" + (file.length() - audioStart) + "bytes");
            long audiolength = file.length() - audioStart;
            if (audiolength <= MAXIMUM_underscoreWRITABLE_underscoreCHUNK_underscoreSIZE) {
                long written2 = fcIn.transferTo(audioStart, audiolength, fcOut);
                logger.finer("Written padding:" + written + " Data:" + written2);
                if (written2 != audiolength) {
                    throw new RuntimeException(ErrorMessage.MP3_underscoreUNABLE_underscoreTO_underscoreADJUST_underscorePADDING.getMsg(audiolength, written2));
                }
            } else {
                long noOfChunks = audiolength / MAXIMUM_underscoreWRITABLE_underscoreCHUNK_underscoreSIZE;
                long lastChunkSize = audiolength % MAXIMUM_underscoreWRITABLE_underscoreCHUNK_underscoreSIZE;
                long written2 = 0;
                for (int i = 0; i < noOfChunks; i++) {
                    written2 += fcIn.transferTo(audioStart + (i * MAXIMUM_underscoreWRITABLE_underscoreCHUNK_underscoreSIZE), MAXIMUM_underscoreWRITABLE_underscoreCHUNK_underscoreSIZE, fcOut);
                }
                written2 += fcIn.transferTo(audioStart + (noOfChunks * MAXIMUM_underscoreWRITABLE_underscoreCHUNK_underscoreSIZE), lastChunkSize, fcOut);
                logger.finer("Written padding:" + written + " Data:" + written2);
                if (written2 != audiolength) {
                    throw new RuntimeException(ErrorMessage.MP3_underscoreUNABLE_underscoreTO_underscoreADJUST_underscorePADDING.getMsg(audiolength, written2));
                }
            }
            long lastModified = file.lastModified();
            if (fcIn != null) {
                if (fcIn.isOpen()) {
                    fcIn.close();
                }
            }
            if (fcOut != null) {
                if (fcOut.isOpen()) {
                    fcOut.close();
                }
            }
            replaceFile(file, paddedFile);
            paddedFile.setLastModified(lastModified);
        } finally {
            try {
                if (fcIn != null) {
                    if (fcIn.isOpen()) {
                        fcIn.close();
                    }
                }
                if (fcOut != null) {
                    if (fcOut.isOpen()) {
                        fcOut.close();
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Problem closing channels and locks:" + e.getMessage(), e);
            }
        }
    }


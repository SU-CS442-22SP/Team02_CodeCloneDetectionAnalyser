            @Override
            public void run() {
                Application.getController().notifyProgressStarted();
                Application.getController().notifyProgressUpdated(-1);
                for (int f = 0; f < fileList.size(); f++) {
                    File archive = fileList.get(f);
                    String arname = archive.getName();
                    String arsuf = arname.substring(arname.lastIndexOf('.'), arname.length());
                    Algorithm alg = null;
                    Algorithm algs[] = algFactory.getAlgorithms();
                    for (int i = 0; i < algs.length; i++) {
                        if (algs[i].getSuffix().equalsIgnoreCase(arsuf)) {
                            alg = algs[i];
                            break;
                        }
                    }
                    if (alg == null) {
                        Application.getController().displayError(bundle.getString("unknown_underscorealg_underscoretitle"), bundle.getString("unknown_underscorealg_underscoretext"));
                        return;
                    }
                    currentAlgorithm = alg;
                    if (!alg.initDecrypt(password)) {
                        Application.getController().displayError(bundle.getString("dec_underscoreinit_underscorefail_underscoretitle"), bundle.getString("dec_underscoreinit_underscorefail_underscoretext"));
                        return;
                    }
                    try {
                        ZipArchiveInputStream zis = null;
                        InputStream is = null;
                        if (EncryptionMode.getBestEncryptionMode(alg.getEncryptionMode()) == EncryptionMode.MODE_underscoreSTREAM) {
                            is = alg.getDecryptionStream(new FileInputStream(archive));
                            if (is == null) {
                                Application.getController().displayError(bundle.getString("dec_underscoreinit_underscorefail_underscoretitle"), bundle.getString("dec_underscoreinit_underscorefail_underscoretext"));
                                return;
                            }
                        } else if (EncryptionMode.getBestEncryptionMode(alg.getEncryptionMode()) == EncryptionMode.MODE_underscoreBLOCK) {
                            is = new BlockCipherInputStream(new FileInputStream(archive), alg);
                            if (is == null) {
                                Application.getController().displayError(bundle.getString("dec_underscoreinit_underscorefail_underscoretitle"), bundle.getString("dec_underscoreinit_underscorefail_underscoretext"));
                                return;
                            }
                        }
                        zis = new ZipArchiveInputStream(is);
                        if (zis == null) {
                            Application.getController().displayError(bundle.getString("dec_underscoreinit_underscorefail_underscoretitle"), bundle.getString("dec_underscoreinit_underscorefail_underscoretext"));
                            return;
                        }
                        File outputDir = getTargetDirectory();
                        if (outputDir == null) {
                            return;
                        }
                        if (!outputDir.exists()) {
                            if (!outputDir.mkdir()) {
                                Application.getController().displayError(bundle.getString("output_underscoredir_underscorefail_underscoretitle"), outputDir.getAbsolutePath() + " " + bundle.getString("output_underscoredir_underscorefail_underscoretext"));
                                return;
                            }
                        }
                        ZipArchiveEntry zae = null;
                        boolean gotEntries = false;
                        while ((zae = zis.getNextZipEntry()) != null) {
                            gotEntries = true;
                            File out = new File(outputDir, zae.getName());
                            if (out.exists()) {
                                if (!mayOverwrite(out)) {
                                    continue;
                                }
                            }
                            Application.getController().displayVerbose("writing to file: " + out.getAbsolutePath());
                            if (!out.getParentFile().exists()) {
                                out.getParentFile().mkdirs();
                            }
                            if (zae.isDirectory()) {
                                out.mkdir();
                                continue;
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            long length = zae.getCompressedSize(), counter = 0;
                            Application.getController().displayVerbose("Length of zip entry " + zae.getName() + " is " + length + "b");
                            byte[] buffer = new byte[16384];
                            MessageDigest md = MessageDigest.getInstance("SHA-1");
                            DigestInputStream in = new DigestInputStream(zis, md);
                            while ((counter = in.read(buffer)) > 0) {
                                if (Thread.currentThread().isInterrupted()) {
                                    os.close();
                                    zis.close();
                                    Application.getController().notifyProgressFinished();
                                    resetModel(true);
                                    return;
                                }
                                os.write(buffer, 0, (int) counter);
                            }
                            os.close();
                            if (zae.getComment() != null && zae.getComment().length() > 0) {
                                if (Arrays.equals(md.digest(), new Base64().decode(zae.getComment()))) {
                                    Application.getController().displayVerbose("Hash of " + zae.getName() + ": " + new Base64().encodeToString(md.digest()));
                                    Application.getController().displayError("Hash Error", "The stored hash of the original file and the hash of the decrypted data do not match. Normally, this means that your data has been manipulated/damaged, but it can also happen if your Java Runtime has a bug in his hash functions.\nIT IS VERY IMPORTANT TO CHECK THE INTEGRITY OF YOUR DECRYPTED DATA!");
                                } else {
                                    Application.getController().displayVerbose("the hash of " + zae.getName() + " was verified succesfully");
                                }
                            }
                        }
                        if (!gotEntries) {
                            Application.getController().displayError(bundle.getString("error_underscoreno_underscoreentries_underscoretitle"), bundle.getString("error_underscoreno_underscoreentries_underscoretext"));
                            outputDir.delete();
                        }
                        zis.close();
                        resetModel(false);
                    } catch (FileNotFoundException ex) {
                        Application.getController().displayError(bundle.getString("error_underscorefile_underscorenot_underscoreexist"), ex.getLocalizedMessage());
                    } catch (IOException ex) {
                        Application.getController().displayError(bundle.getString("error_underscoregeneric_underscoreio"), ex.getLocalizedMessage());
                    } catch (NoSuchAlgorithmException ex) {
                        Application.getController().displayError(bundle.getString("unknown_underscorealg_underscoretext"), ex.getLocalizedMessage());
                    }
                }
                Application.getController().notifyProgressFinished();
                resetModel(true);
            }

